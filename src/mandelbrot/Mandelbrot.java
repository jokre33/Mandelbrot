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

    public static final int XRes = 15000;            //Width of Image and Window
    public static final int YRes = 10000;             //Height of Image and Window
    public static final double xScale = 3.723125388498394E-4;
    public static final double xOffset = -1.3931873958178334;
    public static final double yScale = 2.4820835923322626E-4;
    public static final double yOffset = -0.007219406233900057;
    public static final int Threads = 8;            //Number of Threads the Image generation should run
    public static final boolean showImage = false;   //Should the image be displayed on screen (not recommended at resolutions higher than that of your monitor
    public static final boolean saveImage = true;  //Should the image be saved to HDD?
    public static final String imagePath = "image.png"; //Path to save the image to
    public static int[][][] iterationMerge = new int[Threads][XRes / Threads][YRes];
    public static boolean[] ready = new boolean[Threads + 1];
    mathThread[] tSave = new mathThread[Threads];
    BufferedImage img = new BufferedImage(XRes, YRes, BufferedImage.TYPE_INT_RGB);
    mListener mouse = new mListener();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Mandelbrot mb = new Mandelbrot();
        mb.runGeneration();
        if (showImage) {
            mb.initFrame();
        }
        if (saveImage) {
            mb.saveImage(imagePath);
        }
    }

    public void runGeneration() {
        for (int i = 0; i < Threads; i++) {
            tSave[i] = new mathThread(XRes, YRes, Threads, i);
            ready[i] = false;
            tSave[i].setScaleOffset(xScale, yScale, xOffset, yOffset);
            tSave[i].start();
        }

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

        System.out.println("Creating Image...");
        int j2 = 0;
        for (int i = 0; i < Threads; i++) {
            for (int j = 0; j < XRes / Threads; j++, j2++) {
                for (int k = 0; k < YRes; k++) {
                    if (iterationMerge[i][j][k] < 256) {
                        img.setRGB(j2, k, colorToRGB(255, 0, iterationMerge[i][j][k], 0));
                    } else if (iterationMerge[i][j][k] < 512) {
                        img.setRGB(j2, k, colorToRGB(255, 0, 255 - (iterationMerge[i][j][k] - 256), iterationMerge[i][j][k] - 256));
                    } else if (iterationMerge[i][j][k] < 768) {
                        img.setRGB(j2, k, colorToRGB(255, iterationMerge[i][j][k] - 512, iterationMerge[i][j][k] - 512, 255));
                    }
                    //img.setRGB(j2, k, colorToRGB(255, iterationMerge[i][j][k] / 2, iterationMerge[i][j][k], iterationMerge[i][j][k] / 2));
                }
            }
        }
    }

    public void saveImage(String path) {
        try {
            System.out.println("Saving...");
            ImageIO.write(img, "png", new File(path));
            System.out.println("Saved!");
        } catch (IOException ex) {
            Logger.getLogger(Mandelbrot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JFrame initFrame() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setSize(XRes, YRes);
        f.setLocation((1920 - XRes) / 2, (1080 - YRes) / 2);
        f.add(new DrawPanel(img));
        f.setVisible(true);
        f.addMouseListener(mouse);
        return f;
    }

    public Mandelbrot() {
        System.out.println("Resolution: " + XRes + "x" + YRes);

        double x0 = 0;
        x0 /= Mandelbrot.XRes;
        x0 *= Mandelbrot.xScale;
        x0 += Mandelbrot.xOffset;
        double y0 = 0;
        y0 /= Mandelbrot.YRes;
        y0 *= Mandelbrot.yScale;
        y0 += Mandelbrot.yOffset;

        double x1 = XRes;
        x1 /= Mandelbrot.XRes;
        x1 *= Mandelbrot.xScale;
        x1 += Mandelbrot.xOffset;
        double y1 = YRes;
        y1 /= Mandelbrot.YRes;
        y1 *= Mandelbrot.yScale;
        y1 += Mandelbrot.yOffset;

        System.out.println(x0 + " " + y0 + "\n" + x1 + " " + y1);
    }

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
