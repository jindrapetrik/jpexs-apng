package com.jpexs.images.apng.examples;

import com.jpexs.images.apng.AnimatedPngDecoder;
import com.jpexs.images.apng.AnimatedPngEncoder;
import com.jpexs.images.apng.Png;
import com.jpexs.images.apng.data.AnimatedPngData;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 *
 * @author JPEXS
 */
public class ReencodeExample {
    public static void main(String[] args) throws IOException {
        //Get sample image from the internet
        URL url = URI.create("https://apng.onevcat.com/assets/elephant.png").toURL();
        InputStream is = url.openStream();
        
        AnimatedPngData data = AnimatedPngDecoder.decode(is);
        
        try (FileOutputStream fos = new FileOutputStream("out.png")) {
            AnimatedPngEncoder.encode(data, fos);
        }
        
        Png.dumpPng(new FileInputStream("out.png"));        
    }
}
