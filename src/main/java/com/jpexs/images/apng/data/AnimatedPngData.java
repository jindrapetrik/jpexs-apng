package com.jpexs.images.apng.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 *
 * @author JPEXS
 */
public class AnimatedPngData {

    /**
     * Image width
     */
    private int width;

    /**
     * Image height
     */
    private int height;

    /**
     * Indicates the number of times that this animation should play; if it is
     * 0, the animation should play indefinitely. If nonzero, the animation
     * should come to rest on the final frame at the end of the last play.
     */
    private int numPlays;

    /**
     * Still image when animation is not available.
     */
    private BufferedImage backupImage;

    /**
     * Animation frames
     */
    private List<AnimationFrameData> frames = new ArrayList<>();

    public AnimatedPngData(int width, int height, int numPlays, BufferedImage backupImage) {
        this(width, height, numPlays, backupImage, new ArrayList<>());
    }

    public AnimatedPngData(int width, int height, int numPlays, BufferedImage backupImage, List<AnimationFrameData> frames) {
        if (width <= 0) {
            throw new IllegalArgumentException("Invalid width " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Invalid height " + height);
        }
        this.width = width;
        this.height = height;
        this.numPlays = numPlays;
        this.backupImage = backupImage;
        this.frames = new ArrayList<>(frames);
    }

    public int getFrameCount() {
        return frames.size();
    }

    public List<AnimationFrameData> getFrames() {
        return new ArrayList<>(frames);
    }

    public void setFrames(List<AnimationFrameData> frames) {
        this.frames = new ArrayList<>(frames);
    }

    public boolean hasFrames() {
        return !frames.isEmpty();
    }

    public void addFrame(AnimationFrameData chunk) {
        frames.add(chunk);
    }

    public void addAllFrames(Collection<? extends AnimationFrameData> chunk) {
        frames.addAll(chunk);
    }

    public void addAllFrames(int index, Collection<? extends AnimationFrameData> chunk) {
        frames.addAll(index, chunk);
    }

    public void addFrame(int index, AnimationFrameData chunk) {
        frames.add(index, chunk);
    }

    public AnimationFrameData getFrame(int index) {
        return frames.get(index);
    }

    public AnimationFrameData removeFrame(int index) {
        return frames.remove(index);
    }

    public void clearFrames() {
        frames.clear();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getNumPlays() {
        return numPlays;
    }

    public void setNumPlays(int numPlays) {
        this.numPlays = numPlays;
    }

    public BufferedImage getBackupImage() {
        return backupImage;
    }

    public void setBackupImage(BufferedImage backupImage) {
        this.backupImage = backupImage;
    }

}
