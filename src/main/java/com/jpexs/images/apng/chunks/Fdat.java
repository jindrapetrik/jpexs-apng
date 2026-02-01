package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import java.io.IOException;

/**
 *
 * @author JPEXS
 */
public class Fdat extends Chunk {
    public static final String TYPE = "fdAT";
    
    public long sequenceNumber;
    public byte[] frameData;

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
        sequenceNumber = pis.readUnsignedInt();
        frameData = pis.readBytes(pis.available());
        create(sequenceNumber, frameData);       
    }     

    @Override
    public String toString() {
        return "[fdAT sequenceNumber=" + sequenceNumber+" frameData.length=" + frameData.length +"]";
    }        
}
