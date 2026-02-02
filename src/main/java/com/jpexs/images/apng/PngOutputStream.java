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

    /**
     * Constructs a new PngOutputStream wrapping the specified output stream.
     *
     * @param os the output stream to wrap
     */
    public PngOutputStream(OutputStream os) {
        this.os = os;
    }

    /**
     * Writes an unsigned 32-bit integer (big-endian) to the stream.
     *
     * @param val the unsigned integer value to write
     * @throws IOException if an I/O error occurs
     */
    public void writeUnsignedInt(long val) throws IOException {
        os.write((int) ((val >> 24) & 0xff));
        os.write((int) ((val >> 16) & 0xff));
        os.write((int) ((val >> 8) & 0xff));
        os.write((int) (val & 0xff));
    }

    /**
     * Writes an unsigned 16-bit integer (big-endian) to the stream.
     *
     * @param val the unsigned short value to write
     * @throws IOException if an I/O error occurs
     */
    public void writeUnsignedShort(int val) throws IOException {
        os.write((val >> 8) & 0xff);
        os.write(val & 0xff);
    }

    /**
     * Writes an unsigned byte to the stream.
     *
     * @param val the unsigned byte value to write (0-255)
     * @throws IOException if an I/O error occurs
     */
    public void writeUnsignedByte(int val) throws IOException {
        os.write(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(int b) throws IOException {
        os.write(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] b) throws IOException {
        os.write(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        os.write(b, off, len);
    }

    /**
     * Writes a PNG chunk to the stream.
     * <p>
     * This method writes the chunk length, type, data, and CRC.
     * </p>
     *
     * @param chunk the chunk to write
     * @throws IOException if an I/O error occurs
     */
    public void writeChunk(Chunk chunk) throws IOException {
        byte[] dataBytes = chunk.getData();
        writeUnsignedInt(dataBytes.length);
        byte[] typeBytes = chunk.getType().getBytes(StandardCharsets.US_ASCII);
        write(typeBytes);
        write(dataBytes);
        writeUnsignedInt(Crc.calculate(typeBytes, dataBytes));
    }
}
