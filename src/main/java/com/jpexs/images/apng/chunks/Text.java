package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author JPEXS
 */
public class Text extends Chunk {
    public static final String TYPE = "tEXt";
    
    public String keyword;
    public String text;

    public Text(byte[] data) {
        super(TYPE, data);        
    }

    public Text(String keyword, String text) {
        super(TYPE);
        create(keyword, text);
    }        

    private void create(String keyword, String text) {
        this.keyword = keyword;
        this.text = text;
    }

    @Override
    public void parseData(PngInputStream pis) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < 79; i++) {
            int b = pis.readUnsignedByte();
            if (b == 0) {
                break;
            }
            baos.write(b);;
        }
        String keyword = new String(baos.toByteArray(), "ISO_8859-1");
        
        String text = "";
        if (pis.available() > 0) {
            text = new String(pis.readBytes(pis.available()), "ISO_8859-1");
        }
        create(keyword, text);
    }       

    @Override
    public String toString() {
        return "[tEXt keyword=\"" + keyword.replace("\"", "\"\"") +"\" text=\"" + text.replace("\"", "\"\"")  +"\"]";
    }        
}
