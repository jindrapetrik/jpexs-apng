package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author JPEXS
 */
public class Chunk {

    private final String type;
    private final byte[] data;

    public Chunk(String type, byte[] data) {
        this.type = type;
        this.data = data;              
    }
    
    public Chunk(String type) {
        this(type, new byte[0]);
    }

    
    public void parseData(PngInputStream pis) throws IOException {
        
    }
    
    public final void parseData() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        PngInputStream pis = new PngInputStream(bis);
        parseData(pis);
    }
    
    public final String getType() {
        return type;
    }    

    public void writeData(PngOutputStream os) throws IOException {
        os.write(data);
    }
    
    public final byte[] getData() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PngOutputStream pos = new PngOutputStream(baos);
        writeData(pos);
        return baos.toByteArray();
    } 

    @Override
    public String toString() {
        return "[" + getType() + "]";
    }        
}
