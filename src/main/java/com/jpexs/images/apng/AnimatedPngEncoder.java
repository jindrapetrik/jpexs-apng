package com.jpexs.images.apng;

import com.jpexs.images.apng.chunks.Actl;
import com.jpexs.images.apng.chunks.Chunk;
import com.jpexs.images.apng.chunks.Fctl;
import com.jpexs.images.apng.chunks.Fdat;
import com.jpexs.images.apng.chunks.Ihdr;
import com.jpexs.images.apng.data.AnimatedPngData;
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
        BufferedImage backupImage = data.backupImage;
        if (backupImage == null && !data.frames.isEmpty()) {
            backupImage = data.frames.get(0).image;
        }
        ImageIO.write(backupImage, "PNG", baos);
        byte[] backupImageData = baos.toByteArray();
        Png backupPng = new Png(new ByteArrayInputStream(backupImageData));
        List<Chunk> targetChunks = new ArrayList<>();
        
        targetChunks.addAll(backupPng.chunks);
        
        if (!data.frames.isEmpty()) {
            long sequenceNumber = 0;
            int frame = 0;
            for (int i = 0; i < targetChunks.size(); i++) {
                Chunk chunk = targetChunks.get(i);
                if (chunk instanceof Ihdr) {
                    targetChunks.add(i + 1, new Actl(data.frames.size(), data.numPlays));                    
                }
                if ("IDAT".equals(chunk.getType())) {
                    targetChunks.add(i, new Fctl(sequenceNumber, data.width, data.height, 0, 0, 
                            data.frames.get(frame).delayNumerator, data.frames.get(frame).delayDenominator, Fctl.DISPOSE_OP_BACKGROUND, Fctl.BLEND_OP_SOURCE));
                    frame++;
                    sequenceNumber++;
                    i++;
                    for (;frame < data.frames.size(); frame++) {
                        targetChunks.add(i, new Fctl(sequenceNumber, data.width, data.height, 0, 0, 
                            data.frames.get(frame).delayNumerator, data.frames.get(frame).delayDenominator, Fctl.DISPOSE_OP_BACKGROUND, Fctl.BLEND_OP_SOURCE));
                        sequenceNumber++;
                        i++;
                        
                        baos = new ByteArrayOutputStream();
                        ImageIO.write(data.frames.get(frame).image, "PNG", baos);
                        Png framePng = new Png(new ByteArrayInputStream(baos.toByteArray()));
                        for (int j = 0; j < framePng.chunks.size(); j++) {
                            if ("IDAT".equals(framePng.chunks.get(j).getType())) {
                                targetChunks.add(i, new Fdat(sequenceNumber, framePng.chunks.get(j).getData()));
                                sequenceNumber++;
                                i++;
                            }
                        }                                                
                    }
                }
            }
        }      
        
        Png targetPng = new Png();
        targetPng.chunks = targetChunks;
        targetPng.writeTo(os);
    }
}
