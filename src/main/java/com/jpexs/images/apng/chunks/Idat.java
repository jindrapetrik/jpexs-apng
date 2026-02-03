package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.IOException;

/**
 * IDAT Image Data Chunk.
 * <p>
 * Contains the compressed image data. Multiple IDAT chunks may be present in a
 * PNG file, and their contents should be concatenated to form the compressed
 * datastream.
 * </p>
 *
 * @author JPEXS
 */
public class Idat extends Chunk {

    /**
     * Chunk type identifier.
     */
    public static final String TYPE = "IDAT";

    /**
     * The compressed image data.
     */
    private byte[] frameData;

    /**
     * Constructs an IDAT chunk from raw data.
     *
     * @param data the raw chunk data
     */
    public Idat(byte[] data) {
        super(TYPE, data);
        create(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseData(PngInputStream pis) throws IOException {
        byte[] frameData = pis.readBytes(pis.available());
        create(frameData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeData(PngOutputStream os) throws IOException {
        os.write(frameData);
    }

    private void create(byte[] frameData) {
        this.frameData = frameData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[IDAT dataLength=" + frameData.length + "]";
    }

    /**
     * Sets the compressed image data.
     *
     * @param frameData the compressed image data
     */
    public void setFrameData(byte[] frameData) {
        this.frameData = frameData;
    }

    /**
     * Returns the compressed image data.
     *
     * @return the compressed image data
     */
    public byte[] getFrameData() {
        return frameData;
    }
}
