package com.jpexs.images.apng;

import com.jpexs.images.apng.chunks.Actl;
import com.jpexs.images.apng.chunks.Chunk;
import com.jpexs.images.apng.chunks.Fctl;
import com.jpexs.images.apng.chunks.Fdat;
import com.jpexs.images.apng.chunks.Ihdr;
import com.jpexs.images.apng.chunks.Text;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Input stream for Png data.
 *
 * @author JPEXS
 */
public class PngInputStream extends InputStream {

    private final InputStream is;

    /**
     * Constructs a new PngInputStream wrapping the specified input stream.
     *
     * @param is the input stream to wrap
     */
    public PngInputStream(InputStream is) {
        this.is = is;

    }

    /**
     * Reads an unsigned 32-bit integer (big-endian) from the stream.
     *
     * @return the unsigned integer value
     * @throws IOException if an I/O error occurs
     */
    public long readUnsignedInt() throws IOException {
        long ret = (readUnsignedByte() << 24) + (readUnsignedByte() << 16) + (readUnsignedByte() << 8) + readUnsignedByte();
        return ret & 0xFFFFFFFFL;
    }

    /**
     * Reads an unsigned 16-bit integer (big-endian) from the stream.
     *
     * @return the unsigned short value
     * @throws IOException if an I/O error occurs
     */
    public int readUnsignedShort() throws IOException {
        return (readUnsignedByte() << 8) + readUnsignedByte();
    }

    /**
     * Reads an unsigned byte from the stream.
     *
     * @return the unsigned byte value (0-255)
     * @throws IOException  if an I/O error occurs
     * @throws EOFException if the end of stream is reached
     */
    public int readUnsignedByte() throws IOException {
        int ret = read();
        if (ret == -1) {
            throw new EOFException();
        }
        return ret;
    }

    /**
     * Reads the specified number of bytes from the stream.
     *
     * @param number the number of bytes to read
     * @return a byte array containing the read bytes
     * @throws IOException if an I/O error occurs
     */
    public byte[] readBytes(int number) throws IOException {
        byte[] buffer = new byte[number];
        int pos = 0;
        while (pos < number) {
            pos += is.read(buffer, pos, number - pos);
        }
        return buffer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        return is.read();
    }

    /**
     * Reads a single PNG chunk from the stream.
     *
     * @return the parsed chunk
     * @throws IOException if an I/O error occurs or the chunk CRC is invalid
     */
    public Chunk readChunk() throws IOException {
        long length = readUnsignedInt();
        byte[] chunkTypeBytes = readBytes(4);
        String chunkType = new String(chunkTypeBytes, StandardCharsets.US_ASCII);
        byte[] chunkData = length == 0 ? new byte[0] : readBytes((int) length); //Note: This is limited by Java byte[] that can handle only int length
        long crc = readUnsignedInt();
        long calculatedCrc = Crc.calculate(chunkTypeBytes, chunkData);
        if (crc != calculatedCrc) {
            throw new IOException("Invalid CRC value: " + calculatedCrc + " expected but " + crc + " found in chunk of type " + chunkType);
        }

        Chunk ret;
        switch (chunkType) {
            case Ihdr.TYPE:
                ret = new Ihdr(chunkData);
                break;
            case Actl.TYPE:
                ret = new Actl(chunkData);
                break;
            case Fctl.TYPE:
                ret = new Fctl(chunkData);
                break;
            case Fdat.TYPE:
                ret = new Fdat(chunkData);
                break;
            case Text.TYPE:
                ret = new Text(chunkData);
                break;
            default:
                ret = new Chunk(chunkType, chunkData);
                break;
        }
        ret.parseData();
        return ret;
    }

    /**
     * Reads all chunks from the stream until the IEND chunk is reached.
     *
     * @return a list of all chunks read, including the IEND chunk
     * @throws IOException if an I/O error occurs
     */
    public List<Chunk> readChunkList() throws IOException {
        List<Chunk> ret = new ArrayList<>();
        Chunk chunk;
        do {
            chunk = readChunk();
            ret.add(chunk);
        } while (!"IEND".equals(chunk.getType()));
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available() throws IOException {
        return is.available();
    }
}
