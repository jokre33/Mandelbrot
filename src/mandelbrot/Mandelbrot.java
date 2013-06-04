/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Max
 */
public class Mandelbrot {

    public static final int XRes = 1920;            //Width of Image and Window
    public static final int YRes = 1080;             //Height of Image and Window
    public static final double Scale = 3.723125388498394E-4;
    public static final double xOffset = -1.3931873958178334;
    public static final double yOffset = -0.007219406233900057;
    public static final double xScale = Scale;
    public static final double yScale = Scale / ((double)XRes / (double) YRes);
    public static final int Threads = 8;            //Number of Threads the Image generation should run
    public static final boolean showImage = true;   //Should the image be displayed on screen (not recommended at resolutions higher than that of your monitor
    public static final boolean saveImage = false;  //Should the image be saved to HDD?
    public static final String imagePath = "image.png"; //Path to save the image to
    public static int[][][] iterationMerge = new int[Threads][XRes / Threads][YRes];
    public static boolean[] ready = new boolean[Threads + 1];
    mathThread[] tSave = new mathThread[Threads];
    BufferedImage img = new BufferedImage(XRes, YRes, BufferedImage.TYPE_INT_RGB);
    mListener mouseListen = new mListener();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Mandelbrot mb = new Mandelbrot();
        mb.runGeneration();         //create and run the calculation threads
        if (showImage) {            //show the calculated image if requested
            mb.initFrame();
        }
        if (saveImage) {            //save the calculated image to the harddrive if requested
            mb.saveImage(imagePath);
        }
    }

    public void runGeneration() {
        //Fill all requested Threads with information and start them!
        //Set the ready state to false for all Threads
        for (int i = 0; i < Threads; i++) {
            tSave[i] = new mathThread(XRes, YRes, Threads, i);
            ready[i] = false;
            tSave[i].setScaleOffset(xScale, yScale, xOffset, yOffset);
            tSave[i].start();
        }

        //wait for the calculation threads
        while (!ready[Threads]) {
            try {
                Thread.sleep(1000);
                int readyInt = 0;
                for (int i = 0; i < Threads; i++) {
                    if (ready[i]) {
                        readyInt++;
                    }
                }
                System.out.println(readyInt + " Threads ready!");
                if (readyInt == Threads) {
                    ready[Threads] = true;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Mandelbrot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Create the actual image out of the calculated data
        //Colors can be modified here!
        //to avoid complications use a format like the following where no value should be over 255
        //img.setRGB(j2, k, colorToRGB(255, <RED>, <GREEN>, <BLUE>));
        //<im> is the iteration value use this to make color gradient
        
        System.out.println("Creating Image...");
        int j2 = 0;
        int im;
        for (int i = 0; i < Threads; i++) {
            for (int j = 0; j < XRes / Threads; j++, j2++) {
                for (int k = 0; k < YRes; k++) {
                    //mix your colors here!
                    im = iterationMerge[i][j][k];
                    if (im < 256) {
                        img.setRGB(j2, k, colorToRGB(255, 0, im, im));
                    } else if (im < 512) {
                        img.setRGB(j2, k, colorToRGB(255, 0, 255 - (im - 256), 255));
                    } else if (im < 768) {
                        img.setRGB(j2, k, colorToRGB(255, im - 512, im - 512, 255));
                    }
                }
            }
        }
    }
    
    /**
     * save the image at the path
     * @param path image will be saved here does not need a full path
     */
    public void saveImage(String path) {
        try {
            System.out.println("Saving...");
            ImageIO.write(img, "png", new File(path));
            System.out.println("Saved!");
        } catch (IOException ex) {
            Logger.getLogger(Mandelbrot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    //creates a window to display the image if requested
    public JFrame initFrame() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setSize(XRes, YRes);
        f.setLocation((1920 - XRes) / 2, (1080 - YRes) / 2);
        f.add(new DrawPanel(img));
        f.setVisible(true);
        f.addMouseListener(mouseListen);
        return f;
    }

    public Mandelbrot() {
        System.out.println("Resolution: " + XRes + "x" + YRes);
        System.out.println("Scale: " + Scale + " (" + xScale + " " + yScale + ")");
    }

    /**
     * sorts the 4 color values into one rbg int
     * @param alpha the visibility of the pixel
     * @param red   the amount of red in the pixel
     * @param green the amount of green in the pixel
     * @param blue  the amount of blue in the pixel
     * @return  the rbg int sorted into 2 bytes alpha, red, green, blue
     */
    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }
}

class DrawPanel extends JPanel {

    BufferedImage img;

    DrawPanel(BufferedImage img) {
        this.img = img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }
}
