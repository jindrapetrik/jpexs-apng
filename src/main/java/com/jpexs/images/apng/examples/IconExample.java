package com.jpexs.images.apng.examples;

import com.jpexs.images.apng.AnimatedPngIcon;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author JPEXS
 */
public class IconExample {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        
        JPanel panel = new JPanel();
        
        
        //Get sample image from the internet
        URL url = URI.create("https://apng.onevcat.com/assets/elephant.png").toURL();
        InputStream is = url.openStream();
        
        
        panel.add(new JLabel("Elephant", new AnimatedPngIcon(is), SwingConstants.LEFT));
        
        frame.setContentPane(panel);
        
        frame.setVisible(true);
    }
}
