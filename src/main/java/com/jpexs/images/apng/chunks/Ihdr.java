package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.IOException;

/**
 * IHDR Image header.
 * <p>
 * Contains critical image information including dimensions, bit depth, color
 * type, compression method, filter method, and interlace method.
 * </p>
 *
 * @author JPEXS
 */
public class Ihdr extends Chunk {

    /**
     * Greyscale color type (0).
     */
    public static final int COLOR_TYPE_GREYSCALE = 0;

    /**
     * Truecolor (RGB) color type (2).
     */
    public static final int COLOR_TYPE_TRUECOLOR = 2;

    /**
     * Indexed color (palette) color type (3).
     */
    public static final int COLOR_TYPE_INDEXEDCOLOR = 3;

    /**
     * Greyscale with alpha color type (4).
     */
    public static final int COLOR_TYPE_GREYSCALE_WITH_ALPHA = 4;

    /**
     * Truecolor (RGB) with alpha color type (6).
     */
    public static final int COLOR_TYPE_TRUECOLOR_WITH_ALPHA = 6;

    /**
     * No interlace method (0).
     */
    public static final int INTERLACE_METHOD_NOINTERLACE = 0;

    /**
     * Adam7 interlace method (1).
     */
    public static final int INTERLACE_METHOD_ADAM7 = 1;

    /**
     * Chunk type identifier.
     */
    public static final String TYPE = "IHDR";

    private long width;
    private long height;
    private int bitDepth;
    private int colorType;
    private int compressionMethod;
    private int filterMethod;
    private int interlaceMethod;

    /**
     * Constructs an IHDR chunk from raw data.
     *
     * @param data the raw chunk data
     * @throws IOException if an I/O error occurs
     */
    public Ihdr(byte[] data) throws IOException {
        super(TYPE, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseData(PngInputStream is) throws IOException {
        long width = is.readUnsignedInt();

        long height = is.readUnsignedInt();
        int bitDepth = is.readUnsignedByte();

        int colorType = is.readUnsignedByte();
        int compressionMethod = is.readUnsignedByte();
        int filterMethod = is.readUnsignedByte();
        int interlaceMethod = is.readUnsignedByte();

        create(width, height, bitDepth, colorType, compressionMethod, filterMethod, interlaceMethod);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeData(PngOutputStream os) throws IOException {
        os.writeUnsignedInt(width);
        os.writeUnsignedInt(height);
        os.writeUnsignedByte(bitDepth);
        os.writeUnsignedByte(colorType);
        os.writeUnsignedByte(compressionMethod);
        os.writeUnsignedByte(filterMethod);
        os.writeUnsignedByte(interlaceMethod);
    }

    /**
     * Constructs an IHDR chunk with the specified image parameters.
     *
     * @param width the image width in pixels
     * @param height the image height in pixels
     * @param bitDepth the bit depth (1, 2, 4, 8, or 16)
     * @param colorType the color type
     * @param compressionMethod the compression method (must be 0)
     * @param filterMethod the filter method (must be 0)
     * @param interlaceMethod the interlace method (0 or 1)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Ihdr(
            long width,
            long height,
            int bitDepth,
            int colorType,
            int compressionMethod,
            int filterMethod,
            int interlaceMethod
    ) {
        super(TYPE);
        create(width, height, bitDepth, colorType, compressionMethod, filterMethod, interlaceMethod);
    }

    private void create(
            long width,
            long height,
            int bitDepth,
            int colorType,
            int compressionMethod,
            int filterMethod,
            int interlaceMethod
    ) {

        if (width == 0) {
            throw new IllegalArgumentException("Invalid width 0");
        }
        if (height == 0) {
            throw new IllegalArgumentException("Invalid height 0");
        }

        switch (colorType) {
            case COLOR_TYPE_GREYSCALE:
                if (bitDepth != 1
                        && bitDepth != 2
                        && bitDepth != 4
                        && bitDepth != 8
                        && bitDepth != 16) {
                    throw new IllegalArgumentException("Invalid bitDepth " + bitDepth);
                }
                break;
            case COLOR_TYPE_TRUECOLOR:
            case COLOR_TYPE_GREYSCALE_WITH_ALPHA:
            case COLOR_TYPE_TRUECOLOR_WITH_ALPHA:
                if (bitDepth != 8 && bitDepth != 16) {
                    throw new IllegalArgumentException("Invalid combination of colorType " + colorType + " and bitDepth " + bitDepth);
                }
                break;
            case COLOR_TYPE_INDEXEDCOLOR:
                if (bitDepth != 1
                        && bitDepth != 2
                        && bitDepth != 4
                        && bitDepth != 8) {
                    throw new IllegalArgumentException("Invalid combination of colorType " + colorType + " and bitDepth " + bitDepth);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid colorType " + colorType);
        }
        if (compressionMethod != 0) {
            throw new IllegalArgumentException("Invalid compressionMethod " + compressionMethod);
        }
        if (filterMethod != 0) {
            throw new IllegalArgumentException("Invalid filterMethod " + filterMethod);
        }
        if (interlaceMethod > INTERLACE_METHOD_ADAM7) {
            throw new IllegalArgumentException("Invalid interlaceMethod " + interlaceMethod);
        }

        this.width = width;
        this.height = height;
        this.bitDepth = bitDepth;
        this.colorType = colorType;
        this.compressionMethod = compressionMethod;
        this.filterMethod = filterMethod;
        this.interlaceMethod = interlaceMethod;
    }

    /**
     * Returns the sample depth.
     * <p>
     * For indexed color images, the sample depth is always 8. For other color
     * types, it equals the bit depth.
     * </p>
     *
     * @return the sample depth
     */
    public int getSampleDepth() {
        if (colorType == COLOR_TYPE_INDEXEDCOLOR) {
            return 8;
        }
        return bitDepth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[IHDR width=" + width + " height=" + height + " bitDepth=" + bitDepth + " colorType=" + colorType + " compressionMethod=" + compressionMethod + " filterMethod=" + filterMethod + " interlaceMethod=" + interlaceMethod + "]";
    }

    /**
     * Returns the image width.
     *
     * @return the width in pixels
     */
    public long getWidth() {
        return width;
    }

    /**
     * Sets the image width.
     *
     * @param width the width in pixels
     */
    public void setWidth(long width) {
        this.width = width;
    }

    /**
     * Returns the image height.
     *
     * @return the height in pixels
     */
    public long getHeight() {
        return height;
    }

    /**
     * Sets the image height.
     *
     * @param height the height in pixels
     */
    public void setHeight(long height) {
        this.height = height;
    }

    /**
     * Returns the bit depth.
     *
     * @return the bit depth
     */
    public int getBitDepth() {
        return bitDepth;
    }

    /**
     * Sets the bit depth.
     *
     * @param bitDepth the bit depth
     */
    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    /**
     * Returns the color type.
     *
     * @return the color type
     */
    public int getColorType() {
        return colorType;
    }

    /**
     * Sets the color type.
     *
     * @param colorType the color type
     */
    public void setColorType(int colorType) {
        this.colorType = colorType;
    }

    /**
     * Returns the compression method.
     *
     * @return the compression method
     */
    public int getCompressionMethod() {
        return compressionMethod;
    }

    /**
     * Sets the compression method.
     *
     * @param compressionMethod the compression method
     */
    public void setCompressionMethod(int compressionMethod) {
        this.compressionMethod = compressionMethod;
    }

    /**
     * Returns the filter method.
     *
     * @return the filter method
     */
    public int getFilterMethod() {
        return filterMethod;
    }

    /**
     * Sets the filter method.
     *
     * @param filterMethod the filter method
     */
    public void setFilterMethod(int filterMethod) {
        this.filterMethod = filterMethod;
    }

    /**
     * Returns the interlace method.
     *
     * @return the interlace method
     */
    public int getInterlaceMethod() {
        return interlaceMethod;
    }

    /**
     * Sets the interlace method.
     *
     * @param interlaceMethod the interlace method
     */
    public void setInterlaceMethod(int interlaceMethod) {
        this.interlaceMethod = interlaceMethod;
    }

}
