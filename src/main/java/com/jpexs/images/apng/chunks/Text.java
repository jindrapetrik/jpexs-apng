package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author JPEXS
 */
public class Text extends Chunk {

    public static final String TYPE = "tEXt";

    private String keyword;
    private String text;

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
    public void writeData(PngOutputStream os) throws IOException {
        String keywordTrunc = keyword;
        if (keywordTrunc.length() > 79) {
            keywordTrunc = keywordTrunc.substring(0, 79);
        }
        os.write(keywordTrunc.getBytes("ISO_8859-1"));
        os.write(0);
        os.write(text.getBytes("ISO_8859-1"));
    }

    @Override
    public String toString() {
        return "[tEXt keyword=\"" + keyword.replace("\"", "\"\"") + "\" text=\"" + text.replace("\"", "\"\"") + "\"]";
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
