package com.jpexs.images.apng;

import com.jpexs.images.apng.data.AnimatedPngData;
import com.jpexs.images.apng.data.AnimationFrameData;
import com.jpexs.images.apng.chunks.Actl;
import com.jpexs.images.apng.chunks.Chunk;
import com.jpexs.images.apng.chunks.Fctl;
import com.jpexs.images.apng.chunks.Fdat;
import com.jpexs.images.apng.chunks.Idat;
import com.jpexs.images.apng.chunks.Iend;
import com.jpexs.images.apng.chunks.Ihdr;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Decoder for Animated PNG (APNG) images.
 * <p>
 * This class provides functionality to decode animated PNG files into
 * {@link AnimatedPngData} objects containing individual frames and animation
 * metadata.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 * <pre>{@code
 * InputStream is = new FileInputStream("animation.png");
 * AnimatedPngData data = AnimatedPngDecoder.decode(is);
 * for (AnimationFrameData frame : data.getFrames()) {
 *     // Process each frame
 * }
 * }</pre>
 *
 * @author JPEXS
 * @see AnimatedPngData
 * @see AnimatedPngEncoder
 */
public class AnimatedPngDecoder {

    /**
     * Decodes an Animated PNG from the given input stream.
     * <p>
     * This method reads the entire PNG file and extracts all animation frames
     * along with their timing information. For non-animated PNGs, the returned
     * {@link AnimatedPngData} will have an empty frame list but will contain a
     * backup image.
     * </p>
     *
     * @param is the input stream to read the PNG data from
     * @return an {@link AnimatedPngData} object containing the decoded animation
     * @throws IOException if an I/O error occurs during reading or if the PNG
     *                     data is invalid
     */
    public static AnimatedPngData decode(InputStream is) throws IOException {
        List<AnimationFrameData> frames = new ArrayList<>();
        Png png = new Png(is);

        int width = 0;
        int height = 0;
        int numPlays = 0;

        Ihdr hdr = null;
        Actl actl = null;
        Fctl fctl = null;
        List<Chunk> otherChunks = new ArrayList<>();
        BufferedImage outputBuffer = null;
        Idat idat = null;
        for (Chunk chunk : png.getChunks()) {
            if (chunk instanceof Ihdr) {
                hdr = (Ihdr) chunk;
                outputBuffer = new BufferedImage((int) hdr.getWidth(), (int) hdr.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = outputBuffer.getGraphics();
                g.setColor(new Color(0, 0, 0, 0));
                g.fillRect(0, 0, outputBuffer.getWidth(), outputBuffer.getHeight());
                width = (int) hdr.getWidth();
                height = (int) hdr.getHeight();
            } else if (chunk instanceof Idat) {
                idat = (Idat) chunk;
            } else if (chunk instanceof Fctl) {
                fctl = (Fctl) chunk;
            } else if (chunk instanceof Fdat) {
                Fdat fdat = (Fdat) chunk;
                Png outPng = new Png();
                outPng.addChunk(new Ihdr(fctl.getWidth(), fctl.getHeight(), hdr.getBitDepth(), hdr.getColorType(), hdr.getCompressionMethod(), hdr.getFilterMethod(), hdr.getInterlaceMethod()));
                outPng.addAllChunks(otherChunks);
                outPng.addChunk(new Idat(fdat.getFrameData()));
                outPng.addChunk(new Iend());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                outPng.writeTo(baos);
                BufferedImage subFrame = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
                BufferedImage newImage = new BufferedImage((int) hdr.getWidth(), (int) hdr.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = newImage.createGraphics();
                g.setComposite(AlphaComposite.Src);
                g.drawImage(outputBuffer, 0, 0, null);
                g.setComposite(fctl.getBlendOp() == Fctl.BLEND_OP_SOURCE ? AlphaComposite.Src : AlphaComposite.SrcOver);
                g.drawImage(subFrame,
                        (int) fctl.getxOffset(), (int) fctl.getyOffset(), (int) fctl.getxOffset() + (int) fctl.getWidth(), (int) fctl.getyOffset() + (int) fctl.getHeight(),
                        0, 0, (int) fctl.getWidth(), (int) fctl.getHeight(), null
                );

                switch (fctl.getDisposeOp()) {
                    case Fctl.DISPOSE_OP_NONE:
                        outputBuffer = newImage;
                        break;
                    case Fctl.DISPOSE_OP_BACKGROUND:
                        outputBuffer = new BufferedImage((int) hdr.getWidth(), (int) hdr.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = outputBuffer.createGraphics();
                        g2.setComposite(AlphaComposite.Src);
                        g2.setColor(new Color(0, 0, 0, 0));
                        g2.fillRect(0, 0, outputBuffer.getWidth(), outputBuffer.getHeight());
                        break;
                    case Fctl.DISPOSE_OP_PREVIOUS:
                        break;
                }

                frames.add(new AnimationFrameData(newImage, fctl.getDelayNum(), fctl.getDelayDen()));
                fctl = null;
            } else if (chunk instanceof Iend) {
                break;
            } else if (chunk instanceof Actl) {
                actl = (Actl) chunk;
                numPlays = (int) actl.getNumPlays();
            } else {
                otherChunks.add(chunk);
            }
        }

        BufferedImage backupImage = null;
        if (frames.isEmpty()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            png.writeTo(baos);
            backupImage = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
        } else if (idat != null && hdr != null) {
            Png newPng = new Png();
            newPng.addChunk(hdr);
            newPng.addAllChunks(otherChunks);
            newPng.addChunk(idat);
            newPng.addChunk(new Iend());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newPng.writeTo(baos);
            backupImage = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
        }

        return new AnimatedPngData(width, height, numPlays, backupImage, frames);
    }
}
