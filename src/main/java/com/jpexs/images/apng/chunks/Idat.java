package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.IOException;

/**
 * Image data.
 * @author JPEXS
 */
public class Idat extends Chunk {
    public static final String TYPE = "IDAT";
    
    private byte[] frameData;

    public Idat(byte[] data) {
        super(TYPE, data);
        create(data);
    }

    @Override
    public void parseData(PngInputStream pis) throws IOException {
        byte[] frameData = pis.readBytes(pis.available());
        create(frameData);
    }

    @Override
    public void writeData(PngOutputStream os) throws IOException {
        os.write(frameData);
    }        
    
    private void create(byte[] frameData) {
        this.frameData = frameData;
    }

    @Override
    public String toString() {
        return "[IDAT dataLength=" + frameData.length + "]";
    }   

    public void setFrameData(byte[] frameData) {
        this.frameData = frameData;
    }

    public byte[] getFrameData() {
        return frameData;
    }           
}
