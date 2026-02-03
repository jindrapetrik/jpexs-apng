package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.IOException;

/**
 * tRNS Transparency Chunk.
 * <p>
 * Specifies transparency information for the image. The format of the data
 * depends on the color type:
 * </p>
 * <ul>
 * <li>Color type 0 (Greyscale): 2 bytes (grey sample value)</li>
 * <li>Color type 2 (Truecolor): 6 bytes (red, green, blue sample values)</li>
 * <li>Color type 3 (Indexed): 1 byte per palette entry (alpha values)</li>
 * </ul>
 *
 * @author JPEXS
 */
public class Trns extends Chunk {

    /**
     * Chunk type identifier.
     */
    public static final String TYPE = "tRNS";

    /**
     * The transparency data.
     */
    private byte[] transparencyData;

    /**
     * Constructs a tRNS chunk from raw data.
     *
     * @param data the raw chunk data
     */
    public Trns(byte[] data) {
        super(TYPE, data);
        create(transparencyData);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeData(PngOutputStream os) throws IOException {
        os.write(transparencyData);
    }

    private void create(byte[] transparencyData) {
        this.transparencyData = transparencyData;
    }

    /**
     * Returns the transparency data.
     *
     * @return the transparency data
     */
    public byte[] getTransparencyData() {
        return transparencyData;
    }

    /**
     * Sets the transparency data.
     *
     * @param transparencyData the transparency data
     */
    public void setTransparencyData(byte[] transparencyData) {
        this.transparencyData = transparencyData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[tRNS dataLength=" + transparencyData.length + "]";
    }
}
