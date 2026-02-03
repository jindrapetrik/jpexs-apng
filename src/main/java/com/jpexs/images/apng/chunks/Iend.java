package com.jpexs.images.apng.chunks;

/**
 * IEND Image End Chunk.
 * <p>
 * Marks the end of the PNG datastream. This chunk must appear last and contains
 * no data.
 * </p>
 *
 * @author JPEXS
 */
public class Iend extends Chunk {

    /**
     * Chunk type identifier.
     */
    public static final String TYPE = "IEND";

    /**
     * Constructs an IEND chunk from raw data.
     *
     * @param data the raw chunk data (should be empty)
     */
    public Iend(byte[] data) {
        super(TYPE, data);
    }

    /**
     * Constructs an IEND chunk with no data.
     */
    public Iend() {
        super(TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[IEND]";
    }
}
