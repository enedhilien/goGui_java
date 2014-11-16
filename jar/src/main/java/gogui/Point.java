package gogui;

public class Point extends GeoObject implements Comparable {

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point from(Point p) {
        Point newPoint = new Point(p.x,p.y);
        newPoint.setStatus(p.getStatus());

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

    double distance(Point p) {
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
}