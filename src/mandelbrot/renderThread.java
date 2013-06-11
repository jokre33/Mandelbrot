/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot;

/**
 *
 * @author Max
 */
public class renderThread extends Thread {

    int xRes;
    int yRes;
    int renderXRes;
    int renderYRes;
    int renderXOffset;
    int renderYOffset;
    int ThreadNumber;
    double xOffset;
    double yOffset;
    double xScale;
    double yScale;

    public renderThread(int xOffset, int y‎Offset, int originalXRes, int originalYRes, int ThreadNumber) {
        this.xRes = originalXRes;
        this.yRes = originalYRes;
        this.renderXRes = 256;
        this.renderYRes = 256;
        this.renderXOffset = xOffset;
        this.renderYOffset = y‎Offset;
        this.ThreadNumber = ThreadNumber;
        this.xOffset = 1.5;     //standard offset if nothing else is specified displays the full mandelbrot @ 1:1 resolution
        this.yOffset = 1;
        this.xScale = 2;        //standard scale to display the full mandelbrot @ 1:1 resolution
        this.yScale = this.xScale * originalYRes / originalXRes;
    }

    /**
     * function to manually set the X and Y offsets
     *
     * @param xOffset
     * @param yOffset
     */
    public void setOffset(double xOffset, double yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * function to manually set X and Y scaling
     *
     * @param xScale
     * @param yScale
     */
    public void setScale(double xScale, double yScale) {
        this.xScale = xScale;
        this.yScale = yScale;
    }

    /**
     * function to manually set X and Y offset and scaling
     *
     * @param xScale
     * @param yScale
     * @param xOffset
     * @param yOffset
     */
    public void setScaleOffset(double xScale, double yScale, double xOffset, double yOffset) {
        this.xScale = xScale;
        this.yScale = yScale;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    
    public void setRenderOffset(int xOffset, int yOffset) {
        this.renderXOffset = xOffset;
        this.renderYOffset = yOffset;
    }

    //Magic. Do not touch.
    @Override
    public void run() {
        for (int i = this.renderXOffset; i < this.renderXOffset + this.renderXRes; i++) {
            for (int j = this.renderYOffset; j < this.renderYOffset + this.renderYRes; j++) {
                double x0 = i;
                x0 /= this.xRes;
                x0 *= this.xScale;
                x0 += this.xOffset;
                double y0 = j;
                y0 /= this.yRes;
                y0 *= this.yScale;
                y0 += this.yOffset;
                double x = 0;
                double y = 0;
                double xtemp;

                int it = 0;
                int max_it = 767;

                while ((x * x + y * y) < (2 * 2) && it < max_it) {
                    xtemp = x * x - y * y + x0;
                    y = 2 * x * y + y0;
                    x = xtemp;
                    it++;
                }
                Mandelbrot.iterationMerge[this.ThreadNumber][i - this.renderXOffset][j - this.renderYOffset] = it;
            }
        }
        Mandelbrot.ready[this.ThreadNumber]++;
    }
}
