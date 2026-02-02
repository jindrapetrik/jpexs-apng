package com.jpexs.images.apng.examples;

import com.jpexs.images.apng.AnimatedPngIcon;
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author JPEXS
 */
public class IconExample {

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 800);

        JPanel panel = new JPanel(new BorderLayout());

        //Get sample image from the internet
        URL url = URI.create("https://apng.onevcat.com/assets/elephant.png").toURL();
        InputStream is = url.openStream();

        AnimatedPngIcon icon = new AnimatedPngIcon(is);

        panel.add(new JLabel(icon), BorderLayout.WEST);

        //Add another icon to demonstrate we can place it to two diffent labels and still will animate
        panel.add(new JLabel(icon), BorderLayout.EAST);

        frame.setContentPane(panel);

        frame.setVisible(true);
    }
}
