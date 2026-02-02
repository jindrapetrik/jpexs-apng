package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.IOException;

/**
 *
 * @author JPEXS
 */
public class Fdat extends Chunk {

    public static final String TYPE = "fdAT";

    private long sequenceNumber;

    private byte[] frameData;

    public Fdat(byte[] data) {
        super(TYPE, data);
    }

    public Fdat(long sequenceNumber, byte[] frameData) {
        super(TYPE);
        create(sequenceNumber, frameData);
    }

    private void create(long sequenceNumber, byte[] frameData) {
        this.sequenceNumber = sequenceNumber;
        this.frameData = frameData;
    }

    @Override
    public void parseData(PngInputStream pis) throws IOException {
        long sequenceNumber = pis.readUnsignedInt();
        byte[] frameData = pis.readBytes(pis.available());
        create(sequenceNumber, frameData);
    }

    @Override
    public void writeData(PngOutputStream os) throws IOException {
        os.writeUnsignedInt(sequenceNumber);
        os.write(frameData);
    }

    @Override
    public String toString() {
        return "[fdAT sequenceNumber=" + sequenceNumber + " frameData.length=" + frameData.length + "]";
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public byte[] getFrameData() {
        return frameData;
    }

    public void setFrameData(byte[] frameData) {
        this.frameData = frameData;
    }

}
