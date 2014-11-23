package gogui.random;

import java.util.Random;

public class PointsGenerator {

    public static double realRandom(double rMin, double rMax) {

        if (rMin > rMin) {
            double tmp = rMin;
            rMin = rMax;
            rMax = tmp;
        }

        double f = new Random().nextDouble();
        return rMin + f * (rMax - rMin);
    }

//    static Point withinRectangle(double xmin, double xmax, double ymin, double ymax);
//    static Point onRing(double r, double xOffset, double yOffset);
//    static Point randomOnRectangle(Point p1, Point p2, Point p3, Point p4);
//    static Point onXaxis(double xmin, double xmax);
//    static Point onYaxis(double ymin, double ymax);
//    static Point onDiagonalFromOrigin(Point p);
//    static Point onDiagonalNotFromOrigin(Point p);
}
