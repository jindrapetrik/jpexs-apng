package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import com.jpexs.images.apng.PngOutputStream;
import java.io.IOException;

/**
 * acTL Animation Control Chunk.
 * <p>
 * Contains animation metadata including the total number of frames and the
 * number of times the animation should play.
 * </p>
 *
 * @author JPEXS
 */
public class Actl extends Chunk {

    /**
     * Chunk type identifier.
     */
    public static final String TYPE = "acTL";

    /**
     * Indicates the total number of frames in the animation. This must equal
     * the number of fcTL chunks. 0 is not a valid value. 1 is a valid value,
     * for a single-frame PNG. If this value does not equal the actual number of
     * frames it should be treated as an error.
     */
    private long numFrames;

    /**
     * Indicates the number of times that this animation should play; if it is
     * 0, the animation should play indefinitely. If nonzero, the animation
     * should come to rest on the final frame at the end of the last play.
     */
    private long numPlays; //0 = indefinitely

    /**
     * Constructs an Actl chunk from raw data.
     *
     * @param data the raw chunk data
     */
    public Actl(byte[] data) {
        super(TYPE, data);
    }

    /**
     * Constructs an Actl chunk with the specified parameters.
     *
     * @param numFrames the total number of frames in the animation
     * @param numPlays the number of times to play the animation (0 for
     *     infinite)
     * @throws IllegalArgumentException if numFrames is 0
     */
    public Actl(long numFrames, long numPlays) {
        super(TYPE);
        create(numFrames, numPlays);
    }

    private void create(long numFrames, long numPlays) {
        if (numFrames == 0) {
            throw new IllegalArgumentException("Invalid numFrames 0");
        }
        this.numFrames = numFrames;
        this.numPlays = numPlays;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseData(PngInputStream pis) throws IOException {
        long numFrames = pis.readUnsignedInt();
        long numPlays = pis.readUnsignedInt();
        create(numFrames, numPlays);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeData(PngOutputStream os) throws IOException {
        os.writeUnsignedInt(numFrames);
        os.writeUnsignedInt(numPlays);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[acTL numFrames=" + numFrames + " numPlays=" + numPlays + "]";
    }

    /**
     * Returns the total number of frames.
     *
     * @return the number of frames
     */
    public long getNumFrames() {
        return numFrames;
    }

    /**
     * Sets the total number of frames.
     *
     * @param numFrames the number of frames
     */
    public void setNumFrames(long numFrames) {
        this.numFrames = numFrames;
    }

    /**
     * Returns the number of times the animation should play.
     *
     * @return the number of plays (0 means infinite)
     */
    public long getNumPlays() {
        return numPlays;
    }

    /**
     * Sets the number of times the animation should play.
     *
     * @param numPlays the number of plays (0 for infinite)
     */
    public void setNumPlays(long numPlays) {
        this.numPlays = numPlays;
    }

}
