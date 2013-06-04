/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot;

/**
 *
 * @author Max
 */
public class mathThread extends Thread {

    int xRes;
    int yRes;
    int Threads;
    int ThreadNumber;
    
    double xOffset;
    double yOffset;
    double xScale;
    double yScale;

    /**
     * Initializes the calculation threads
     * @param XRes          requested X-resolution for proper scaling
     * @param YRes          requested Y-resolution
     * @param Threads       number of total threads in use to determine what should be calculated
     * @param ThreadNumber  number of this thread (starts @ 0)
     */
    
    public mathThread(int XRes, int YRes, int Threads, int ThreadNumber) {
        this.xRes = XRes;
        this.yRes = YRes;
        this.Threads = Threads;
        this.ThreadNumber = ThreadNumber;
        this.xOffset = 1.5;     //standard offset if nothing else is specified displays the full mandelbrot @ 1:1 resolution
        this.yOffset = 1;    
        this.xScale = 2;        //standard scale to display the full mandelbrot @ 1:1 resolution
        this.yScale = this.xScale * YRes / XRes;
    }
    
    /**
     * function to manually set the X and Y offsets
     * @param xOffset
     * @param yOffset 
     */
    public void setOffset(double xOffset, double yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    
    /**
     * function to manually set X and Y scaling
     * @param xScale
     * @param yScale 
     */
    public void setScale(double xScale, double yScale) {
        this.xScale = xScale;
        this.yScale = yScale;
    }
    
    /**
     * function to manually set X and Y offset and scaling
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
    
    //Magic. Do not touch.
    @Override
    public void run() {
        for (int i = xRes / this.Threads * this.ThreadNumber; i < xRes / this.Threads * (this.ThreadNumber + 1); i++) {
            for (int j = 0; j < yRes; j++) {
                double x0 = i;
                x0 /= this.xRes;
                x0 *= this.xScale;        //2 - Scale
                x0 += this.xOffset;         //1.5 - Offset
                double y0 = j;
                y0 /= this.yRes;
                y0 *= this.yScale;        //2 - Scale
                y0 += this.yOffset;         //1 - Offset
                double x = 0;
                double y = 0;
                double xtemp;

                int it = 0;
                int max_it = 767;

                while ((x * x + y * y) < (2 * 2) && it < max_it) {  //(x * x + y * y) < (2 * 2)
                    xtemp = x * x - y * y + x0;                     //x * x - y * y + x0
                    y = 2 * x * y + y0;                             //2 * x * y + y0
                    x = xtemp;
                    it++;
                }
                Mandelbrot.iterationMerge[this.ThreadNumber][i - (xRes / this.Threads * this.ThreadNumber)][j] = it;
            }
        }
        Mandelbrot.ready[this.ThreadNumber] = true;
    }
}
