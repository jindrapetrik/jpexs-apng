package com.jpexs.images.apng.data;

import java.awt.image.BufferedImage;

/**
 * Represents single animation frame.
 *
 * @author JPEXS
 */
public class AnimationFrameData {

    /**
     * The rendered image
     */
    private BufferedImage image;
    /**
     * Delay numerator
     */
    private int delayNumerator;
    /**
     * Delay denominator. 0 == 100
     */
    private int delayDenominator;

    /**
     * Constructs a new AnimationFrameData with the specified image and delay
     * values.
     *
     * @param image the rendered frame image
     * @param delayNumerator the numerator of the delay fraction (in seconds)
     * @param delayDenominator the denominator of the delay fraction (0 is
     *     treated as 100)
     */
    public AnimationFrameData(BufferedImage image, int delayNumerator, int delayDenominator) {
        this.image = image;
        this.delayNumerator = delayNumerator;
        this.delayDenominator = delayDenominator;
    }

    /**
     * Gets value as double value in seconds.
     *
     * @return Seconds value of delay as double
     */
    public double getDelayAsDouble() {
        return delayNumerator / (double) (delayDenominator == 0 ? 100 : delayDenominator);
    }

    /**
     * Gets delay value in milliseconds.
     *
     * @return Ms value of delay
     */
    public long getDelayInMs() {
        return (long) Math.round(delayNumerator * 1000 / (double) (delayDenominator == 0 ? 100 : delayDenominator));
    }

    /**
     * Returns the rendered frame image.
     *
     * @return the frame image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Sets the rendered frame image.
     *
     * @param image the frame image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Returns the delay numerator.
     *
     * @return the delay numerator
     */
    public int getDelayNumerator() {
        return delayNumerator;
    }

    /**
     * Sets the delay numerator.
     *
     * @param delayNumerator the delay numerator
     */
    public void setDelayNumerator(int delayNumerator) {
        this.delayNumerator = delayNumerator;
    }

    /**
     * Returns the delay denominator.
     *
     * @return the delay denominator (0 means 100)
     */
    public int getDelayDenominator() {
        return delayDenominator;
    }

    /**
     * Sets the delay denominator.
     *
     * @param delayDenominator the delay denominator (0 is treated as 100)
     */
    public void setDelayDenominator(int delayDenominator) {
        this.delayDenominator = delayDenominator;
    }

}
