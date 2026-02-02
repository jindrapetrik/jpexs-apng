package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * tEXt Text Chunk.
 * <p>
 * Contains textual information that can be associated with the image, such as
 * comments, creation time, author, etc. The data consists of a keyword followed
 * by a null separator and the text string.
 * </p>
 *
 * @author JPEXS
 */
public class Text extends Chunk {

    /**
     * Chunk type identifier.
     */
    public static final String TYPE = "tEXt";

    /**
     * The keyword identifying the text content.
     */
    private String keyword;

    /**
     * The text content.
     */
    private String text;

    /**
     * Constructs a Text chunk from raw data.
     *
     * @param data the raw chunk data
     */
    public Text(byte[] data) {
        super(TYPE, data);
    }

    /**
     * Constructs a Text chunk with the specified keyword and text.
     *
     * @param keyword the keyword (max 79 characters)
     * @param text the text content
     */
    public Text(String keyword, String text) {
        super(TYPE);
        create(keyword, text);
    }

    private void create(String keyword, String text) {
        this.keyword = keyword;
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[tEXt keyword=\"" + keyword.replace("\"", "\"\"") + "\" text=\"" + text.replace("\"", "\"\"") + "\"]";
    }

    /**
     * Returns the keyword.
     *
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Sets the keyword.
     *
     * @param keyword the keyword (max 79 characters)
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns the text content.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text content.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

}
