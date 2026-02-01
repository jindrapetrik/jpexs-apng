package com.jpexs.images.apng.chunks;

import com.jpexs.images.apng.PngInputStream;
import java.io.IOException;

/**
 * acTL Animation Control Chunk
 *
 * @author JPEXS
 */
public class Actl extends Chunk {

    public static final String TYPE = "acTL";

    /**
     * Indicates the total number of frames in the animation. This must equal
     * the number of fcTL chunks. 0 is not a valid value. 1 is a valid value,
     * for a single-frame PNG. If this value does not equal the actual number of
     * frames it should be treated as an error.
     */
    public long numFrames;

    /**
     * Indicates the number of times that this animation should play; if it is
     * 0, the animation should play indefinitely. If nonzero, the animation
     * should come to rest on the final frame at the end of the last play.
     */
    public long numPlays; //0 = indefinitely

    public Actl(byte[] data) {
        super(TYPE, data);
    }

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

    @Override
    public void parseData(PngInputStream pis) throws IOException {
        numFrames = pis.readUnsignedInt();
        numPlays = pis.readUnsignedInt();
        create(numFrames, numPlays);
    }

    @Override
    public String toString() {
        return "[acTL numFrames=" + numFrames + " numPlays=" + numPlays + "]";
    }
}
