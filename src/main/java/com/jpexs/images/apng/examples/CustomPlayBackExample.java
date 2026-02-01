package com.jpexs.images.apng.examples;

import com.jpexs.images.apng.AnimatedPngDecoder;
import com.jpexs.images.apng.data.AnimationFrameData;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author JPEXS
 */
public class CustomPlayBackExample {

    private static int frameNum = 0;
    private static List<AnimationFrameData> frames;
    private static JFrame frame;
    private static JLabel numFrameLabel;
    private static boolean playing = false;

    public static void main(String[] args) throws IOException {

        //Get sample image from the internet
        URL url = URI.create("https://apng.onevcat.com/assets/elephant.png").toURL();
        InputStream is = url.openStream();

        frames = AnimatedPngDecoder.decode(is).frames;

        frame = new JFrame("Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel cnt = new JPanel(new BorderLayout());
        cnt.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (frameNum < 0) {
                    return;
                }
                g.drawImage(frames.get(frameNum).image, 0, 0, null);
            }

        }, BorderLayout.CENTER);

        numFrameLabel = new JLabel("" + frameNum);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("Prev");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameNum = (frameNum - 1) % frames.size();
                if (frameNum < 0) {
                    frameNum += frames.size();
                }
                frame.repaint();
                numFrameLabel.setText("" + frameNum);
            }
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameNum = (frameNum + 1) % frames.size();
                frame.repaint();
                numFrameLabel.setText("" + frameNum);
            }
        });

        JButton pausePlayButton = new JButton("Pause/play");
        pausePlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playing = !playing;
                if (playing) {
                    play();
                }
            }
        });
        buttonsPanel.add(prevButton);
        buttonsPanel.add(numFrameLabel);
        buttonsPanel.add(pausePlayButton);
        buttonsPanel.add(nextButton);
        cnt.add(buttonsPanel, BorderLayout.SOUTH);
        frame.setContentPane(cnt);
        frame.setSize(800, 800);

        frame.setVisible(true);

    }

    private static void play() {
        playing = true;
        frameNum = (frameNum + 1) % frames.size();
        numFrameLabel.setText("" + frameNum);
        frame.repaint();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!playing) {
                    return;
                }
                play();
            }
        }, (long) Math.round(frames.get(frameNum).getDelayAsDouble() * 1000));
    }
}
