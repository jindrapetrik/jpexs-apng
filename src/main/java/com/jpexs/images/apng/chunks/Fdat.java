package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.IOException;

/**
 * fdAT Frame Data Chunk.
 * <p>
 * Contains the actual animation frame image data. The frame data has the same
 * structure as an IDAT chunk, except that it is preceded by a sequence number.
 * </p>
 *
 * @author JPEXS
 */
public class Fdat extends Chunk {

    /**
     * Chunk type identifier.
     */
    public static final String TYPE = "fdAT";

    /**
     * The sequence number of this chunk.
     */
    private long sequenceNumber;

    /**
     * The frame image data.
     */
    private byte[] frameData;

    /**
     * Constructs an Fdat chunk from raw data.
     *
     * @param data the raw chunk data
     */
    public Fdat(byte[] data) {
        super(TYPE, data);
    }

    /**
     * Constructs an Fdat chunk with the specified sequence number and frame
     * data.
     *
     * @param sequenceNumber the sequence number
     * @param frameData      the frame image data
     */
    public Fdat(long sequenceNumber, byte[] frameData) {
        super(TYPE);
        create(sequenceNumber, frameData);
    }

    private void create(long sequenceNumber, byte[] frameData) {
        this.sequenceNumber = sequenceNumber;
        this.frameData = frameData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseData(PngInputStream pis) throws IOException {
        long sequenceNumber = pis.readUnsignedInt();
        byte[] frameData = pis.readBytes(pis.available());
        create(sequenceNumber, frameData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeData(PngOutputStream os) throws IOException {
        os.writeUnsignedInt(sequenceNumber);
        os.write(frameData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[fdAT sequenceNumber=" + sequenceNumber + " frameData.length=" + frameData.length + "]";
    }

    /**
     * Returns the sequence number.
     *
     * @return the sequence number
     */
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the sequence number.
     *
     * @param sequenceNumber the sequence number
     */
    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Returns the frame image data.
     *
     * @return the frame data
     */
    public byte[] getFrameData() {
        return frameData;
    }

    /**
     * Sets the frame image data.
     *
     * @param frameData the frame data
     */
    public void setFrameData(byte[] frameData) {
        this.frameData = frameData;
    }

}
