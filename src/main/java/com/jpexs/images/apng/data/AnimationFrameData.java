package com.jpexs.images.apng.data;

import java.awt.image.BufferedImage;

/**
 * Represents single animation frame.
 * @author JPEXS
 */
public class AnimationFrameData {
    /**
     * The rendered image
     */
    public BufferedImage image;
    /**
     * Delay numerator
     */
    public int delayNumerator;
    /**
     * Delay denominator. 0 == 100
     */
    public int delayDenominator;
    
    
    /**
     * Gets value as double value in seconds.
     * @return Seconds value of delay as double
     */
    public double getDelayAsDouble() {
        return delayNumerator / (double) (delayDenominator == 0 ? 100 : delayDenominator);
    }
    
    /**
     * Gets delay value in milliseconds.
     * @return Ms value of delay
     */
    public long getDelayInMs() {
        return (long) Math.round(delayNumerator * 1000 / (double) (delayDenominator == 0 ? 100 : delayDenominator));
    }
}
