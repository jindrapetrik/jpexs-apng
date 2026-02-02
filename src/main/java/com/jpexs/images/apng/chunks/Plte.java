package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.IOException;

/**
 * PLTE Palette.
 * @author JPEXS
 */
public class Plte extends Chunk {
    public static final String TYPE = "PLTE";
    
    byte[] paletteData;

    public Plte(byte[] data) {
        super(TYPE, data);
        create(data);
    }

    @Override
    public void parseData(PngInputStream pis) throws IOException {
        byte[] paletteData = pis.readBytes(pis.available());
        create(paletteData);
    }

    @Override
    public void writeData(PngOutputStream os) throws IOException {
        os.write(paletteData);
    }        
    
    
    private void create(byte[] paletteData) {
        if ((paletteData.length % 3) != 0) {
            throw new IllegalArgumentException("Incorrect palette length: " + this.paletteData.length + " (not multiple of 3)");
        }
        this.paletteData = paletteData;
    }

    @Override
    public String toString() {
        return "[PLTE entryCount=" + (paletteData.length / 3)+ "]";
    }   

    public byte[] getPaletteData() {
        return paletteData;
    }

    public void setPaletteData(byte[] paletteData) {
        if ((paletteData.length % 3) != 0) {
            throw new IllegalArgumentException("Incorrect palette length: " + this.paletteData.length + " (not multiple of 3)");
        }
        this.paletteData = paletteData;
    }        
}
