package com.jpexs.images.apng.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * Container for Animated PNG data.
 * <p>
 * This class holds all the data needed to represent an animated PNG image,
 * including dimensions, frame data, and playback configuration.
 * </p>
 *
 * @author JPEXS
 * @see AnimationFrameData
 * @see com.jpexs.images.apng.AnimatedPngDecoder
 * @see com.jpexs.images.apng.AnimatedPngEncoder
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

    /**
     * Constructs a new AnimatedPngData with the specified dimensions and
     * playback settings.
     *
     * @param width the width of the animation in pixels
     * @param height the height of the animation in pixels
     * @param numPlays the number of times the animation should play (0 for
     *     infinite)
     * @param backupImage the fallback image for viewers that do not support
     *     APNG
     * @throws IllegalArgumentException if width or height is less than or equal
     *     to zero
     */
    public AnimatedPngData(int width, int height, int numPlays, BufferedImage backupImage) {
        this(width, height, numPlays, backupImage, new ArrayList<>());
    }

    /**
     * Constructs a new AnimatedPngData with the specified dimensions, playback
     * settings, and initial frames.
     *
     * @param width the width of the animation in pixels
     * @param height the height of the animation in pixels
     * @param numPlays the number of times the animation should play (0 for
     *     infinite)
     * @param backupImage the fallback image for viewers that do not support
     *     APNG
     * @param frames the list of animation frames
     * @throws IllegalArgumentException if width or height is less than or equal
     *     to zero
     */
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

    /**
     * Returns the number of frames in the animation.
     *
     * @return the frame count
     */
    public int getFrameCount() {
        return frames.size();
    }

    /**
     * Returns a copy of the list of animation frames.
     *
     * @return a new list containing all frames
     */
    public List<AnimationFrameData> getFrames() {
        return new ArrayList<>(frames);
    }

    /**
     * Sets the animation frames.
     *
     * @param frames the list of frames to set
     */
    public void setFrames(List<AnimationFrameData> frames) {
        this.frames = new ArrayList<>(frames);
    }

    /**
     * Checks if this animation has any frames.
     *
     * @return {@code true} if there is at least one frame, {@code false}
     *         otherwise
     */
    public boolean hasFrames() {
        return !frames.isEmpty();
    }

    /**
     * Adds a frame to the end of the animation.
     *
     * @param chunk the frame to add
     */
    public void addFrame(AnimationFrameData chunk) {
        frames.add(chunk);
    }

    /**
     * Adds all frames from the specified collection to the end of the
     * animation.
     *
     * @param chunk the collection of frames to add
     */
    public void addAllFrames(Collection<? extends AnimationFrameData> chunk) {
        frames.addAll(chunk);
    }

    /**
     * Inserts all frames from the specified collection at the specified
     * position.
     *
     * @param index the position at which to insert the first frame
     * @param chunk the collection of frames to add
     */
    public void addAllFrames(int index, Collection<? extends AnimationFrameData> chunk) {
        frames.addAll(index, chunk);
    }

    /**
     * Inserts a frame at the specified position.
     *
     * @param index the position at which to insert the frame
     * @param chunk the frame to add
     */
    public void addFrame(int index, AnimationFrameData chunk) {
        frames.add(index, chunk);
    }

    /**
     * Returns the frame at the specified index.
     *
     * @param index the index of the frame to return
     * @return the frame at the specified index
     */
    public AnimationFrameData getFrame(int index) {
        return frames.get(index);
    }

    /**
     * Removes the frame at the specified index.
     *
     * @param index the index of the frame to remove
     * @return the removed frame
     */
    public AnimationFrameData removeFrame(int index) {
        return frames.remove(index);
    }

    /**
     * Removes all frames from the animation.
     */
    public void clearFrames() {
        frames.clear();
    }

    /**
     * Returns the width of the animation.
     *
     * @return the width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the animation.
     *
     * @param width the width in pixels
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns the height of the animation.
     *
     * @return the height in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the animation.
     *
     * @param height the height in pixels
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Returns the number of times the animation should play.
     *
     * @return the number of plays (0 means infinite)
     */
    public int getNumPlays() {
        return numPlays;
    }

    /**
     * Sets the number of times the animation should play.
     *
     * @param numPlays the number of plays (0 for infinite)
     */
    public void setNumPlays(int numPlays) {
        this.numPlays = numPlays;
    }

    /**
     * Returns the backup image used when animation is not available.
     *
     * @return the backup image, or {@code null} if not set
     */
    public BufferedImage getBackupImage() {
        return backupImage;
    }

    /**
     * Sets the backup image used when animation is not available.
     *
     * @param backupImage the backup image
     */
    public void setBackupImage(BufferedImage backupImage) {
        this.backupImage = backupImage;
    }

}
