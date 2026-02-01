package com.jpexs.images.apng.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JPEXS
 */
public class AnimatedPngData {
    
    /**
     * Image width
     */
    public int width;
    
    /**
     * Image height
     */
    public int height;
    
    /**
     * Indicates the number of times that this animation should play;
     * if it is 0, the animation should play indefinitely.
     * If nonzero, the animation should come to rest on the final frame at the end of the last play.
     */
    public int numPlays;
    
    /**
     * Still image when animation is not available.
     */
    public BufferedImage backupImage;
    
    /**
     * Animation frames
     */
    public List<AnimationFrameData> frames = new ArrayList<>();
}
