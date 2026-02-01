package com.jpexs.images.apng;

import com.jpexs.images.apng.chunks.Chunk;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PNG container - header and chunks.
 * @author JPEXS
 */
public class Png {
    public static final byte[] SIGNATURE = new byte[] {
        (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47,
        (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A
    };
    
    
    public List<Chunk> chunks = new ArrayList<>();
    
    public Png() {
        
    }
    
    public Png(InputStream is) throws IOException {
        PngInputStream pis = new PngInputStream(is);
        byte[] signature = pis.readBytes(8);
        if (!Arrays.equals(signature, SIGNATURE)) {
            throw new IOException("Invalid PNG signature");
        }
        chunks = pis.readChunkList();
    }
    
    /**
     * Writes png to stream.
     * @param os
     * @throws IOException 
     */
    public void writeTo(OutputStream os) throws IOException {
        PngOutputStream pos = new PngOutputStream(os);
        pos.write(SIGNATURE);
        for (Chunk chunk : chunks) {
            pos.writeChunk(chunk);
        }
    }
    
    public static void dumpPng(InputStream is) throws IOException {
        Png png = new Png(is);
        for (Chunk chunk : png.chunks) {
            System.out.println(chunk.toString());
        }
    }
}
