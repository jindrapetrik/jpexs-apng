package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.IOException;

/**
 * IHDR Image header
 *
 * @author JPEXS
 */
public class Ihdr extends Chunk {

    public static final int COLOR_TYPE_GREYSCALE = 0;
    public static final int COLOR_TYPE_TRUECOLOR = 2;
    public static final int COLOR_TYPE_INDEXEDCOLOR = 3;
    public static final int COLOR_TYPE_GREYSCALE_WITH_ALPHA = 4;
    public static final int COLOR_TYPE_TRUECOLOR_WITH_ALPHA = 6;

    public static final int INTERLACE_METHOD_NOINTERLACE = 0;
    public static final int INTERLACE_METHOD_ADAM7 = 1;

    public static final String TYPE = "IHDR";

    private long width;
    private long height;
    private int bitDepth;
    private int colorType;
    private int compressionMethod;
    private int filterMethod;
    private int interlaceMethod;

    public Ihdr(byte[] data) throws IOException {
        super(TYPE, data);
    }

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

    public int getSampleDepth() {
        if (colorType == COLOR_TYPE_INDEXEDCOLOR) {
            return 8;
        }
        return bitDepth;
    }

    @Override
    public String toString() {
        return "[IHDR width=" + width + " height=" + height + " bitDepth=" + bitDepth + " colorType=" + colorType + " compressionMethod=" + compressionMethod + " filterMethod=" + filterMethod + " interlaceMethod=" + interlaceMethod + "]";
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    public int getColorType() {
        return colorType;
    }

    public void setColorType(int colorType) {
        this.colorType = colorType;
    }

    public int getCompressionMethod() {
        return compressionMethod;
    }

    public void setCompressionMethod(int compressionMethod) {
        this.compressionMethod = compressionMethod;
    }

    public int getFilterMethod() {
        return filterMethod;
    }

    public void setFilterMethod(int filterMethod) {
        this.filterMethod = filterMethod;
    }

    public int getInterlaceMethod() {
        return interlaceMethod;
    }

    public void setInterlaceMethod(int interlaceMethod) {
        this.interlaceMethod = interlaceMethod;
    }

}
