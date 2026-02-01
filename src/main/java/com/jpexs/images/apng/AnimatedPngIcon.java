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
 *
 * @author JPEXS
 */
public class AnimatedPngIcon implements Icon {

    private final AnimatedPngData apng;

    private final CopyOnWriteArrayList<WeakReference<Component>> hosts = new CopyOnWriteArrayList<>();

    private Timer timer;

    private int currentFrame = 0;

    private Long startTime = null;

    private int remainingNumPlays;

    public AnimatedPngIcon(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            apng = AnimatedPngDecoder.decode(fis);
        }
    }

    public AnimatedPngIcon(InputStream is) throws IOException {
        apng = AnimatedPngDecoder.decode(is);
    }

    public AnimatedPngIcon(AnimatedPngData apng) throws IOException {
        this.apng = apng;
    }

    @Override
    public int getIconWidth() {
        return apng.width;
    }

    @Override
    public int getIconHeight() {
        return apng.height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        BufferedImage img = apng.frames.isEmpty() ? apng.backupImage : apng.frames.get(currentFrame).image;
        g.drawImage(img, x, y, null);

        if (apng.frames.size() > 1) {
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
            if (apng.numPlays == 0) {
                remainingNumPlays = -1;
            } else {
                remainingNumPlays = apng.numPlays;
            }
        }       

        long currentTime = System.currentTimeMillis();

        long currentDelay = currentTime - startTime;

        int f = 0;
        long t = 0;
        int newFrame = -1;
        for (AnimationFrameData fr : apng.frames) {
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

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
       
}
