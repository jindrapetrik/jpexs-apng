package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import java.io.IOException;

/**
 * Image end.
 * @author JPEXS
 */
public class Iend extends Chunk {
    public static final String TYPE = "IEND";
        
    public Iend(byte[] data) {
        super(TYPE, data);
    }

    public Iend() {
        super(TYPE);
    }                     

    @Override
    public String toString() {
        return "[IEND]";
    }        
}
