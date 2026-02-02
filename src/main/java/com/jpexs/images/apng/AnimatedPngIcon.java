package com.jpexs.images.apng;

import com.jpexs.images.apng.data.AnimatedPngData;
import com.jpexs.images.apng.data.AnimationFrameData;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Icon;

/**
 * A Swing {@link Icon} implementation for displaying Animated PNG (APNG)
 * images.
 * <p>
 * This class automatically animates the image when painted in a Swing
 * component. It manages its own timer for animation playback and handles
 * multiple host components displaying the same icon.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 * <pre>{@code
 * InputStream is = new FileInputStream("animation.png");
 * AnimatedPngIcon icon = new AnimatedPngIcon(is);
 * JLabel label = new JLabel(icon);
 * }</pre>
 *
 * @author JPEXS
 * @see AnimatedPngDecoder
 * @see AnimatedPngData
 */
public class AnimatedPngIcon implements Icon {

    /**
     * The decoded animated PNG data.
     */
    private final AnimatedPngData apng;

    /**
     * List of weak references to host components displaying this icon.
     */
    private final CopyOnWriteArrayList<WeakReference<Component>> hosts = new CopyOnWriteArrayList<>();

    /**
     * Timer for animation playback.
     */
    private Timer timer;

    /**
     * Index of the currently displayed frame.
     */
    private int currentFrame = 0;

    /**
     * Start time of the current animation cycle.
     */
    private Long startTime = null;

    /**
     * Remaining number of animation plays.
     */
    private int remainingNumPlays;

    /**
     * Creates an AnimatedPngIcon from a file.
     *
     * @param file the PNG file to load
     * @throws IOException if an I/O error occurs during reading
     */
    public AnimatedPngIcon(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            apng = AnimatedPngDecoder.decode(fis);
        }
    }

    /**
     * Creates an AnimatedPngIcon from an input stream.
     *
     * @param is the input stream to read the PNG data from
     * @throws IOException if an I/O error occurs during reading
     */
    public AnimatedPngIcon(InputStream is) throws IOException {
        apng = AnimatedPngDecoder.decode(is);
    }

    /**
     * Creates an AnimatedPngIcon from pre-decoded animation data.
     *
     * @param apng the animated PNG data to display
     * @throws IOException if an I/O error occurs
     */
    public AnimatedPngIcon(AnimatedPngData apng) throws IOException {
        this.apng = apng;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIconWidth() {
        return apng.getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIconHeight() {
        return apng.getHeight();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Paints the current animation frame and starts the animation timer if not
     * already running.
     * </p>
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        BufferedImage img = apng.hasFrames() ? apng.getFrame(currentFrame).getImage() : apng.getBackupImage();
        g.drawImage(img, x, y, null);

        if (apng.getFrameCount() > 1) {
            registerHost(c);
            ensureRunning();
        }
    }

    private void registerHost(Component c) {
        // Already registered?
        for (WeakReference<Component> ref : hosts) {
            Component existing = ref.get();
            if (existing == c) {
                return;
            }
        }
        hosts.add(new WeakReference<>(c));
    }

    private void ensureRunning() {

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    onTick();
                }
            }, 20, 20);
        }
    }

    private void onTick() {

        if (startTime == null) {
            startTime = System.currentTimeMillis();
            currentFrame = 0;
            if (apng.getNumPlays() == 0) {
                remainingNumPlays = -1;
            } else {
                remainingNumPlays = apng.getNumPlays();
            }
        }

        long currentTime = System.currentTimeMillis();

        long currentDelay = currentTime - startTime;

        int f = 0;
        long t = 0;
        int newFrame = -1;
        for (AnimationFrameData fr : apng.getFrames()) {
            long d = fr.getDelayInMs();
            if (currentDelay >= t && currentDelay <= t + d) {
                newFrame = f;
                break;
            }
            t += d;
            f++;
        }
        if (newFrame < 0) {
            if (remainingNumPlays > 0) {
                remainingNumPlays--;
            }
            if (remainingNumPlays == -1 || remainingNumPlays > 0) {
                newFrame = 0;
                startTime = currentTime;
            } else {
                return;
            }

        }

        currentFrame = newFrame;

        boolean anyAlive = false;

        for (WeakReference<Component> ref : hosts) {
            Component host = ref.get();
            if (host == null) {
                hosts.remove(ref);
                continue;
            }

            if (!host.isDisplayable()) {
                hosts.remove(ref);
                continue;
            }

            anyAlive = true;

            host.repaint();
        }

        if (!anyAlive) {
            stop();
        }
    }

    /**
     * Stops the animation timer.
     * <p>
     * Call this method to halt animation playback. The animation can be
     * restarted by calling {@link #paintIcon(Component, Graphics, int, int)} again.
     * </p>
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
