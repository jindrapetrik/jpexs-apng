package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.IOException;

/**
 * PLTE Palette Chunk.
 * <p>
 * Contains the color palette for indexed color images. Each palette entry
 * consists of three bytes (red, green, blue), so the total data length must be
 * a multiple of 3.
 * </p>
 *
 * @author JPEXS
 */
public class Plte extends Chunk {

    /**
     * Chunk type identifier.
     */
    public static final String TYPE = "PLTE";

    /**
     * The raw palette data (RGB triplets).
     */
    private byte[] paletteData;

    /**
     * Constructs a PLTE chunk from raw data.
     *
     * @param data the raw chunk data (must be a multiple of 3 bytes)
     * @throws IllegalArgumentException if data length is not a multiple of 3
     */
    public Plte(byte[] data) {
        super(TYPE, data);
        create(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseData(PngInputStream pis) throws IOException {
        byte[] paletteData = pis.readBytes(pis.available());
        create(paletteData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeData(PngOutputStream os) throws IOException {
        os.write(paletteData);
    }

    private void create(byte[] paletteData) {
        if ((paletteData.length % 3) != 0) {
            throw new IllegalArgumentException("Incorrect palette length: " + paletteData.length + " (not multiple of 3)");
        }
        this.paletteData = paletteData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[PLTE entryCount=" + (paletteData.length / 3) + "]";
    }

    /**
     * Returns the raw palette data.
     *
     * @return the palette data (RGB triplets)
     */
    public byte[] getPaletteData() {
        return paletteData;
    }

    /**
     * Sets the raw palette data.
     *
     * @param paletteData the palette data (must be a multiple of 3 bytes)
     * @throws IllegalArgumentException if data length is not a multiple of 3
     */
    public void setPaletteData(byte[] paletteData) {
        if ((paletteData.length % 3) != 0) {
            throw new IllegalArgumentException("Incorrect palette length: " + paletteData.length + " (not multiple of 3)");
        }
        this.paletteData = paletteData;
    }
}
