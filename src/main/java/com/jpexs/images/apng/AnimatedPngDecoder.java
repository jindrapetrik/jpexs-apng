package com.jpexs.images.apng;

import com.jpexs.images.apng.data.AnimatedPngData;
import com.jpexs.images.apng.data.AnimationFrameData;
import com.jpexs.images.apng.chunks.Actl;
import com.jpexs.images.apng.chunks.Chunk;
import com.jpexs.images.apng.chunks.Fctl;
import com.jpexs.images.apng.chunks.Fdat;
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
 *
 * @author JPEXS
 */
public class AnimatedPngDecoder {
    
    public static AnimatedPngData decode(InputStream is) throws IOException {
        AnimatedPngData ret = new AnimatedPngData();
        
        List<AnimationFrameData> frames = new ArrayList<>();
        Png png = new Png(is);

        Ihdr hdr = null;
        Actl actl = null;
        Fctl fctl = null;
        List<Chunk> otherChunks = new ArrayList<>();
        BufferedImage outputBuffer = null;
        Chunk idat = null;
        for (Chunk chunk : png.chunks) {            
            if (chunk instanceof Ihdr) {
                hdr = (Ihdr) chunk;
                outputBuffer = new BufferedImage((int) hdr.width, (int) hdr.height, BufferedImage.TYPE_INT_ARGB);
                Graphics g = outputBuffer.getGraphics();
                g.setColor(new Color(0, 0, 0, 0));
                g.fillRect(0, 0, outputBuffer.getWidth(), outputBuffer.getHeight());
                ret.width = (int) hdr.width;
                ret.height = (int) hdr.height;
            } else if ("IDAT".equals(chunk.getType())) {
                idat = chunk;
            } else if (chunk instanceof Fctl) {
                fctl = (Fctl) chunk;
            } else if (chunk instanceof Fdat) {
                Fdat fdat = (Fdat) chunk;
                Png outPng = new Png();
                outPng.chunks.add(new Ihdr(fctl.width, fctl.height, hdr.bitDepth, hdr.colorType, hdr.compressionMethod, hdr.filterMethod, hdr.interlaceMethod));
                outPng.chunks.addAll(otherChunks);
                outPng.chunks.add(new Chunk("IDAT", fdat.frameData));
                outPng.chunks.add(new Chunk("IEND"));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                outPng.writeTo(baos);
                BufferedImage subFrame = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
                BufferedImage newImage = new BufferedImage((int) hdr.width, (int) hdr.height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = newImage.createGraphics();
                g.setComposite(AlphaComposite.Src);
                g.drawImage(outputBuffer, 0, 0, null);
                g.setComposite(fctl.blendOp == Fctl.BLEND_OP_SOURCE ? AlphaComposite.Src : AlphaComposite.SrcOver);
                g.drawImage(subFrame,
                        (int) fctl.xOffset, (int) fctl.yOffset, (int) fctl.xOffset + (int) fctl.width, (int) fctl.yOffset + (int) fctl.height,
                        0, 0, (int) fctl.width, (int) fctl.height, null
                );

                switch (fctl.disposeOp) {
                    case Fctl.DISPOSE_OP_NONE:
                        outputBuffer = newImage;
                        break;
                    case Fctl.DISPOSE_OP_BACKGROUND:
                        outputBuffer = new BufferedImage((int) hdr.width, (int) hdr.height, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = outputBuffer.createGraphics();
                        g2.setComposite(AlphaComposite.Src);
                        g2.setColor(new Color(0, 0, 0, 0));
                        g2.fillRect(0, 0, outputBuffer.getWidth(), outputBuffer.getHeight());
                        break;
                    case Fctl.DISPOSE_OP_PREVIOUS:
                        break;
                }

                AnimationFrameData fr = new AnimationFrameData();
                fr.image = newImage;
                fr.delayNumerator = fctl.delayNum;
                fr.delayDenominator = fctl.delayDen;
                frames.add(fr);
                fctl = null;
            } else if ("IEND".equals(chunk.getType())) {
                break;
            } else if (chunk instanceof Actl) {
                actl = (Actl) chunk;
                ret.numPlays = (int) actl.numPlays;
            } else {
                otherChunks.add(chunk);
            }
        }

        if (ret.frames.isEmpty()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            png.writeTo(baos);            
            ret.backupImage = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
        } else if (idat != null && hdr != null) {
            Png newPng = new Png();
            newPng.chunks.add(hdr);
            newPng.chunks.addAll(otherChunks);
            newPng.chunks.add(idat);
            newPng.chunks.add(new Chunk("IEND"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newPng.writeTo(baos);            
            ret.backupImage = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
        }
        
        ret.frames = frames;
        return ret;
    }
}
