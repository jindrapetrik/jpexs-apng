package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import java.io.IOException;

/**
 * tRNS Transparency.
 * @author JPEXS
 */
public class Trns extends Chunk {
    public static final String TYPE = "tRNS";
    
    private byte[] transparencyData;

    public Trns(byte[] data) {
        super(TYPE, data);
        create(transparencyData);
    }

    @Override
    public void parseData(PngInputStream pis) throws IOException {
        byte[] transparencyData = pis.readBytes(pis.available());
        /*
        Color type 0: ushort
        Color type 2: 
            r ushort
            g ushort
            b ushort
        Color type 3:
            alpha[0] ubyte
            alpha[1] ubyte
            alpha[2] ubyte
            ...[paletteEntries.length - 1]
        */                
        create(transparencyData);
    }   
    
    private void create(byte[] transparencyData) {
        this.transparencyData = transparencyData;
    }

    public byte[] getTransparencyData() {
        return transparencyData;
    }

    public void setTransparencyData(byte[] transparencyData) {
        this.transparencyData = transparencyData;
    }

    @Override
    public String toString() {
        return "[tRNS dataLength=" + transparencyData.length + "]";
    }        
}
