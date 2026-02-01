package com.jpexs.images.apng;

import com.jpexs.images.apng.chunks.Chunk;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Output stream for png data.
 *
 * @author JPEXS
 */
public class PngOutputStream extends OutputStream {

    private final OutputStream os;

    public PngOutputStream(OutputStream os) {
        this.os = os;
    }

    public void writeUnsignedInt(long val) throws IOException {
        os.write((int) ((val >> 24) & 0xff));
        os.write((int) ((val >> 16) & 0xff));
        os.write((int) ((val >> 8) & 0xff));
        os.write((int) (val & 0xff));
    }

    public void writeUnsignedShort(int val) throws IOException {
        os.write((val >> 8) & 0xff);
        os.write(val & 0xff);
    }

    public void writeUnsignedByte(int val) throws IOException {
        os.write(val);
    }

    @Override
    public void write(int b) throws IOException {
        os.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        os.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        os.write(b, off, len);
    }

    public void writeChunk(Chunk chunk) throws IOException {
        byte[] dataBytes = chunk.getData();
        writeUnsignedInt(dataBytes.length);
        byte[] typeBytes = chunk.getType().getBytes(StandardCharsets.US_ASCII);
        write(typeBytes);
        write(dataBytes);
        writeUnsignedInt(Crc.calculate(typeBytes, dataBytes));
    }
}
