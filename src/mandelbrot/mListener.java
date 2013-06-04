/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Max
 */
public class mListener implements MouseListener {

    int clicks = 0;
    double[] x = new double[2];
    double[] y = new double[2];

    /**
     * function / listener to make zooming into a certain area easier
     * first click at the top left of the area you want than at the bottom right
     * now enter the console output into the variables in Mandelbrot.java
     * @param e 
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        this.x[clicks] = e.getX();
        this.x[clicks] /= Mandelbrot.XRes;
        this.x[clicks] *= Mandelbrot.xScale;
        this.x[clicks] += Mandelbrot.xOffset;
        this.y[clicks] = e.getY();
        this.y[clicks] /= Mandelbrot.YRes;
        this.y[clicks] *= Mandelbrot.yScale;
        this.y[clicks] += Mandelbrot.yOffset;
        //write mouse click position and relative position in the mandelbrot to the console
        System.out.println(e.getX() + " " + e.getY() + " | " + this.y[clicks] + " " + this.y[clicks]);
        clicks++;   //Count the clicks to display the scaling and offset information
        if (clicks >= 2) {  //every second click display the scaling and offset information
            clicks = 0;
            System.out.println("x offset: " + this.x[0]);   //relative position of the first click
            System.out.println("y offset: " + this.y[0]);   //also...
            System.out.println("scale: " + Math.max(diff(this.x[0], this.x[1]), diff(this.y[0], this.y[1]) * Mandelbrot.XRes / Mandelbrot.YRes));
        }
    }

    /**
     * short function to keep the above cleaner
     * @param x
     * @param y
     * @return difference between x and y
     */
    private double diff(double x, double y) {
        return Math.max(x, y) - Math.min(x, y);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
