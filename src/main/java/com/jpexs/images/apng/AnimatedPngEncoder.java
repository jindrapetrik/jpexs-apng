package com.jpexs.images.apng;

import com.jpexs.images.apng.chunks.Actl;
import com.jpexs.images.apng.chunks.Chunk;
import com.jpexs.images.apng.chunks.Fctl;
import com.jpexs.images.apng.chunks.Fdat;
import com.jpexs.images.apng.chunks.Ihdr;
import com.jpexs.images.apng.data.AnimatedPngData;
import com.jpexs.images.apng.data.AnimationFrameData;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author JPEXS
 */
public class AnimatedPngEncoder {

    public static void encode(AnimatedPngData data, OutputStream os) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage backupImage = data.getBackupImage();
        if (backupImage == null && data.hasFrames()) {
            backupImage = data.getFrame(0).getImage();
        }
        ImageIO.write(backupImage, "PNG", baos);
        byte[] backupImageData = baos.toByteArray();
        Png backupPng = new Png(new ByteArrayInputStream(backupImageData));
        List<Chunk> targetChunks = new ArrayList<>();

        targetChunks.addAll(backupPng.getChunks());

        if (data.hasFrames()) {
            long sequenceNumber = 0;
            int frame = 0;
            for (int i = 0; i < targetChunks.size(); i++) {
                Chunk chunk = targetChunks.get(i);
                if (chunk instanceof Ihdr) {
                    targetChunks.add(i + 1, new Actl(data.getFrameCount(), data.getNumPlays()));
                }
                if ("IDAT".equals(chunk.getType())) {
                    AnimationFrameData fdata = data.getFrame(frame);

                    targetChunks.add(i, new Fctl(sequenceNumber, data.getWidth(), data.getHeight(), 0, 0,
                            fdata.getDelayNumerator(), fdata.getDelayDenominator(), Fctl.DISPOSE_OP_BACKGROUND, Fctl.BLEND_OP_SOURCE));
                    frame++;
                    sequenceNumber++;
                    i++;
                    for (; frame < data.getFrameCount(); frame++) {

                        fdata = data.getFrame(frame);
                        targetChunks.add(i, new Fctl(sequenceNumber, data.getWidth(), data.getHeight(), 0, 0,
                                fdata.getDelayNumerator(), fdata.getDelayDenominator(), Fctl.DISPOSE_OP_BACKGROUND, Fctl.BLEND_OP_SOURCE));
                        sequenceNumber++;
                        i++;

                        baos = new ByteArrayOutputStream();
                        ImageIO.write(fdata.getImage(), "PNG", baos);
                        Png framePng = new Png(new ByteArrayInputStream(baos.toByteArray()));
                        for (int j = 0; j < framePng.getChunkCount(); j++) {
                            if ("IDAT".equals(framePng.getChunk(j).getType())) {
                                targetChunks.add(i, new Fdat(sequenceNumber, framePng.getChunk(j).getData()));
                                sequenceNumber++;
                                i++;
                            }
                        }
                    }
                }
            }
        }

        Png targetPng = new Png(targetChunks);
        targetPng.writeTo(os);
    }
}
