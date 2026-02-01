package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import java.io.IOException;

/**
 * fcTL Frame Control Chunk
 *
 * @author JPEXS
 */
public class Fctl extends Chunk {

    public static final String TYPE = "fcTL";

    /**
     * No disposal is done on this frame before rendering the next; the contents
     * of the output buffer are left as is.
     */
    public static final int DISPOSE_OP_NONE = 0;
    /**
     * The frame's region of the output buffer is to be cleared to fully
     * transparent black before rendering the next frame.
     */
    public static final int DISPOSE_OP_BACKGROUND = 1;
    /**
     * The frame's region of the output buffer is to be reverted to the previous
     * contents before rendering the next frame.
     */
    public static final int DISPOSE_OP_PREVIOUS = 2;

    /**
     * All color components of the frame, including alpha, overwrite the current
     * contents of the frame's output buffer region.
     */
    public static final int BLEND_OP_SOURCE = 0;

    /**
     * The frame should be composited onto the output buffer based on its alpha,
     * using a simple OVER operation.
     */
    public static final int BLEND_OP_OVER = 1;

    /**
     * Defines the sequence number of the animation chunk, starting from 0.
     */
    public long sequenceNumber;
    /**
     * Defines the width of the following frame. Must be greater than zero.
     */
    public long width;
    /**
     * Defines the hidth of the following frame. Must be greater than zero.
     */
    public long height;
    /**
     * Defines the x position of the following frame. Zero is a valid value.
     */
    public long xOffset;
    /**
     * Defines the y position of the following frame. Zero is a valid value.
     */
    public long yOffset;
    /**
     * Defines numerator of delay fraction; indicating the time to display the
     * current frame, in seconds. If the the value of the numerator is 0 the
     * decoder should render the next frame as quickly as possible, though
     * viewers may impose a reasonable lower bound.
     */
    public int delayNum;
    /**
     * Defines denominator of delay fraction; indicating the time to display the
     * current frame, in seconds. If the denominator is 0, it is to be treated
     * as if it were 100 (that is, delay_num then specifies 1/100ths of a
     * second).
     */
    public int delayDen;
    /**
     * Defines the type of frame area disposal to be done after rendering this
     * frame; in other words, it specifies how the output buffer should be
     * changed at the end of the delay (before rendering the next frame).
     *
     * If the first fcTL chunk uses a dispose_op of DISPOSE_OP_PREVIOUS it
     * should be treated as DISPOSE_OP_BACKGROUND.
     */
    public int disposeOp;

    /**
     * Specifies whether the frame is to be alpha blended into the current
     * output buffer content, or whether it should completely replace its region
     * in the output buffer.
     */
    public int blendOp;

    /*
    The fcTL chunk corresponding to the default image, if it exists, has these restrictions:
The x_offset and y_offset fields must be 0.
The width and height fields must equal the corresponding fields from the IHDR chunk.
     */
    public Fctl(byte[] data) {
        super(TYPE, data);
    }

    @Override
    public void parseData(PngInputStream pis) throws IOException {
        sequenceNumber = pis.readUnsignedInt();
        width = pis.readUnsignedInt();
        height = pis.readUnsignedInt();
        xOffset = pis.readUnsignedInt();
        yOffset = pis.readUnsignedInt();
        delayNum = pis.readUnsignedShort();
        delayDen = pis.readUnsignedShort();
        disposeOp = pis.readUnsignedByte();
        blendOp = pis.readUnsignedByte();
    }

    public Fctl(long sequenceNumber, long width, long height, long xOffset, long yOffset, int delayNum, int delayDen, int disposeOp, int blendOp) {
        super(TYPE);
        this.sequenceNumber = sequenceNumber;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.delayNum = delayNum;
        this.delayDen = delayDen;
        this.disposeOp = disposeOp;
        this.blendOp = blendOp;
        create(sequenceNumber, width, height, xOffset, yOffset, delayNum, delayDen, disposeOp, blendOp);
    }

    private void create(long sequenceNumber, long width, long height, long xOffset, long yOffset, int delayNum, int delayDen, int disposeOp, int blendOp) {
        if (width <= 0) {
            throw new IllegalArgumentException("Invalid width " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Invalid height " + height);
        }
        if (disposeOp > DISPOSE_OP_PREVIOUS) {
            throw new IllegalArgumentException("Invalid disposeOp " + disposeOp);
        }
        if (blendOp > BLEND_OP_OVER) {
            throw new IllegalArgumentException("Invalid blendOp " + blendOp);
        }
        this.sequenceNumber = sequenceNumber;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.delayNum = delayNum;
        this.delayDen = delayDen;
        this.disposeOp = disposeOp;
        this.blendOp = blendOp;
    }

    @Override
    public String toString() {
        return "[fcTL sequenceNumber=" + sequenceNumber + " width=" + width + " height=" + height + " xOffset=" + xOffset + " yOffset=" + yOffset + " delayNum=" + delayNum + " delayDen=" + delayDen + " disposeOp=" + disposeOp + " blendOp=" + blendOp + "]";
    }
}
