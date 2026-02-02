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

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getDelayNumerator() {
        return delayNumerator;
    }

    public void setDelayNumerator(int delayNumerator) {
        this.delayNumerator = delayNumerator;
    }

    public int getDelayDenominator() {
        return delayDenominator;
    }

    public void setDelayDenominator(int delayDenominator) {
        this.delayDenominator = delayDenominator;
    }

}
