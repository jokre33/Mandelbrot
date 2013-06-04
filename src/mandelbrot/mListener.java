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

    @Override
    public void mouseClicked(MouseEvent e) {
        this.x[clicks] = e.getX();
        this.x[clicks] /= Mandelbrot.XRes;
        this.x[clicks] *= Mandelbrot.xScale;        //2 - Scale
        this.x[clicks] += Mandelbrot.xOffset;         //1.5 - Offset
        this.y[clicks] = e.getY();
        this.y[clicks] /= Mandelbrot.YRes;
        this.y[clicks] *= Mandelbrot.yScale;        //2 - Scale
        this.y[clicks] += Mandelbrot.yOffset;         //1 - Offset
        System.out.println(e.getX() + " " + e.getY() + " | " + this.y[clicks] + " " + this.y[clicks]);
        clicks++;
        if (clicks >= 2) {
            clicks = 0;
            System.out.println("x offset: " + this.x[0]);
            System.out.println("y offset: " + this.y[0]);
            System.out.println("scale(x): " + diff(this.x[0], this.x[1]) + "x" + diff(this.x[0], this.x[1]) * Mandelbrot.YRes / Mandelbrot.XRes);
            System.out.println("scale(y): " + diff(this.y[0], this.y[1]) * Mandelbrot.XRes / Mandelbrot.YRes + "x" + diff(this.y[0], this.y[1]));
        }
    }

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
