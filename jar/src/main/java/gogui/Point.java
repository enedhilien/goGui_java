package gogui;

import java.util.Comparator;

public class Point extends GeoObject implements Comparable {

    public final Comparator<Point> POLAR_ORDER_COMPARATOR = new PolarOrderComparator();

    public final Comparator<Point> REVERSED_POLAR_ORDER = new ReversedPolarOrderComparator();

    public static final Comparator<Point> Y_ORDER_COMPARATOR = new YOrderComparator();

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point from(Point p) {
        Point newPoint = new Point(p.x, p.y);
        newPoint.setStatus(p.getStatus());
        if (p.hasCustomColor()) {
            newPoint.setColor(p.getColor());
        }
        return newPoint;
    }

    public Point() {
        x = 0;
        y = 0;
    }

    boolean equals(Point that) {
        return Commons.compareDouble(x, that.x) && Commons.compareDouble(y, that.y);
    }

    boolean isSmaller(Point that) {
        if (x != that.x)
            return x < that.x;
        return y < that.y;
    }

    public double distance(Point p) {
        return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
    }

    double angleBetweenXAxis(Point p) {
        double oposite = p.y - y;
        double adjacent = p.x - x;

        return Math.atan2(oposite, adjacent);
    }

    static double orient2d(Point a, Point b, Point c) {
        return ((b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x));
    }

    /**
     * Is a->b->c a counterclockwise turn? Repaste from http://algs4.cs.princeton.edu/12oop/Point2D.java
     *
     * @param a first point
     * @param b second point
     * @param c third point
     * @return { -1, 0, +1 } if a->b->c is a { clockwise, collinear; counterclocwise } turn.
     */
    public static int ccw(Point a, Point b, Point c) {
        double area2 = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
        if (area2 < 0) return -1;
        else if (area2 > 0) return +1;
        else return 0;
    }

    @Override
    public int compareTo(Object o) {
        Point that = (Point) o;
        if (x != that.x) {
            if (x < that.x) {
                return -1;
            } else return 1;
        } else if (y < that.y) {
            return -1;
        } else if (y > that.y) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        if (Double.compare(point.y, y) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }

    public Point minus(Point relative) {
        return new Point(this.x - relative.x, this.y - relative.y);
    }

    private class PolarOrderComparator implements Comparator<Point> {
        @Override
        public int compare(Point q1, Point q2) {
            double dx1 = q1.x - x;
            double dy1 = q1.y - y;
            double dx2 = q2.x - x;
            double dy2 = q2.y - y;

            if (dy1 >= 0 && dy2 < 0) return -1;    // q1 above; q2 below
            else if (dy2 >= 0 && dy1 < 0) return +1;    // q1 below; q2 above
            else if (dy1 == 0 && dy2 == 0) {            // 3-collinear and horizontal
                if (dx1 >= 0 && dx2 < 0) return -1;
                else if (dx2 >= 0 && dx1 < 0) return +1;
                else return 0;
            } else return -ccw(Point.this, q1, q2);     // both above or below

            // Note: ccw() recomputes dx1, dy1, dx2, and dy2
        }
    }

    private class ReversedPolarOrderComparator implements Comparator<Point> {

        @Override
        public int compare(Point o1, Point o2) {
            return -compare2(o1, o2);
        }


        public int compare2(Point q1, Point q2) {
            double dx1 = q1.x - x;
            double dy1 = q1.y - y;
            double dx2 = q2.x - x;
            double dy2 = q2.y - y;

            if (dy1 >= 0 && dy2 < 0) return -1;    // q1 above; q2 below
            else if (dy2 >= 0 && dy1 < 0) return +1;    // q1 below; q2 above
            else if (dy1 == 0 && dy2 == 0) {            // 3-collinear and horizontal
                if (dx1 >= 0 && dx2 < 0) return -1;
                else if (dx2 >= 0 && dx1 < 0) return +1;
                else return 0;
            } else return -ccw(Point.this, q1, q2);     // both above or below

            // Note: ccw() recomputes dx1, dy1, dx2, and dy2
        }
    }

    private static class YOrderComparator implements Comparator<Point> {

        @Override
        public int compare(Point o1, Point o2) {
            if (o1.equals(o2))
                return 0;
            else {
                if (o1.y < o2.y) {
                    return -1;
                } else if (o1.y > o2.y) {
                    return 1;
                } else if (o1.y == o2.y) {
                    return o1.compareTo(o2);
                } else
                    return 0;

            }

        }
    }
}