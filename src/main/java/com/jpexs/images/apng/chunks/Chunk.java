package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * PNG Chunk.
 * <p>
 * Base class for all PNG chunks. Subclasses implement specific chunk types
 * such as IHDR, acTL, fcTL, etc.
 * </p>
 *
 * @author JPEXS
 */
public class Chunk {

    /**
     * The chunk type (4 characters).
     */
    private final String type;

    /**
     * The raw chunk data.
     */
    private final byte[] data;

    /**
     * Constructs a new chunk with the specified type and data.
     *
     * @param type the 4-character chunk type
     * @param data the chunk data
     */
    public Chunk(String type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Constructs a new chunk with the specified type and no data.
     *
     * @param type the 4-character chunk type
     */
    public Chunk(String type) {
        this(type, new byte[0]);
    }

    /**
     * Parses chunk-specific data from the input stream.
     * <p>
     * Subclasses should override this method to parse their specific data
     * format.
     * </p>
     *
     * @param pis the input stream containing the chunk data
     * @throws IOException if an I/O error occurs
     */
    public void parseData(PngInputStream pis) throws IOException {

    }

    /**
     * Parses the chunk data from the internal data array.
     *
     * @throws IOException if an I/O error occurs
     */
    public final void parseData() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        PngInputStream pis = new PngInputStream(bis);
        parseData(pis);
    }

    /**
     * Returns the chunk type.
     *
     * @return the 4-character chunk type
     */
    public final String getType() {
        return type;
    }

    /**
     * Writes the chunk data to the output stream.
     * <p>
     * Subclasses should override this method to write their specific data
     * format.
     * </p>
     *
     * @param os the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    public void writeData(PngOutputStream os) throws IOException {
        os.write(data);
    }

    /**
     * Returns the chunk data as a byte array.
     *
     * @return the chunk data
     * @throws IOException if an I/O error occurs during serialization
     */
    public final byte[] getData() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PngOutputStream pos = new PngOutputStream(baos);
        writeData(pos);
        return baos.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[" + getType() + "]";
    }
}
