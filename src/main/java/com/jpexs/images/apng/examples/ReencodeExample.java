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
 * Example demonstrating how to decode and re-encode an animated PNG.
 * <p>
 * This example downloads an APNG file, decodes it, and re-encodes it to a new
 * file.
 * </p>
 *
 * @author JPEXS
 */
public class ReencodeExample {

    /**
     * Main entry point for the example.
     *
     * @param args command line arguments (not used)
     * @throws IOException if an I/O error occurs
     */
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
