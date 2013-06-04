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

    public mathThread(int XRes, int YRes, int Threads, int ThreadNumber) {
        this.xRes = XRes;
        this.yRes = YRes;
        this.Threads = Threads;
        this.ThreadNumber = ThreadNumber;
        this.xOffset = 2.5;
        this.yOffset = 1.25;
        this.xScale = 4;
        this.yScale = this.xScale * YRes / XRes;
    }
    
    public void setOffset(double xOffset, double yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    
    public void setScale(double xScale, double yScale) {
        this.xScale = xScale;
        this.yScale = yScale;
    }
    
    public void setScaleOffset(double xScale, double yScale, double xOffset, double yOffset) {
        this.xScale = xScale;
        this.yScale = yScale;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

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
            //System.out.println(i);
        }
        Mandelbrot.ready[this.ThreadNumber] = true;
    }
}
