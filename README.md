# JPEXS APNG
Library for working with Animated PNGs (APNG) in Java.

# Usage

Displaying animated image (icon):
```java
import com.jpexs.images.apng.AnimatedPngIcon;
...

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
```

Parsing frames:
```java
import com.jpexs.images.apng.AnimatedPngDecoder;
import com.jpexs.images.apng.data.AnimatedPngData;
...

is = new FileInputStream("file.png");

AnimatedPngData data = AnimatedPngDecoder.decode(is);


//The class has these elements:
public class AnimatedPngData {
    
    /**
     * Image width
     */
    public int width;
    
    /**
     * Image height
     */
    public int height;
    
    /**
     * Indicates the number of times that this animation should play;
     * if it is 0, the animation should play indefinitely.
     * If nonzero, the animation should come to rest on the final frame at the end of the last play.
     */
    public int numPlays;
    
    /**
     * Still image when animation is not available.
     */
    public BufferedImage backupImage;
    
    /**
     * Animation frames
     */
    public List<AnimationFrameData> frames = new ArrayList<>();
}

/**
 * Represents single animation frame.
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
    public double getDelayAsDouble() {...}
    
    /**
     * Gets delay value in milliseconds.
     * @return Ms value of delay
     */
    public long getDelayInMs() {...}
}


```

Create animated PNG:
```java
import com.jpexs.images.apng.AnimatedPngEncoder;
import com.jpexs.images.apng.data.AnimatedPngData;
...
AnimatedPngData data = new AnimatedPngData();
...fill the data...;

FileOutputStream fos = new FileOutputStream("out.png");
AnimatedPngEncoder.encode(data, fos);

```

# Author
Jindra Petřík aka JPEXS

# License
MIT
