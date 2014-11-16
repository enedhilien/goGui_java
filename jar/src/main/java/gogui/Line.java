package gogui;

public class Line extends GeoObject {

    final Point point1;
    final Point point2;

    private Parameters parameters;

    public Line(Point point1, Point point2) {

        this.point1 = point1;
        this.point2 = point2;

        parameters = new Parameters(getA(), getB(), getC());
    }

    public static Line from(Line line) {

        Line newLine = new Line(Point.from(line.point1), Point.from(line.point2));
        newLine.setStatus(line.getStatus());
        return newLine;
    }


    private boolean isEqualToWithOrder(Line that) {
        return (point1 == that.point1) && (point2 == that.point2);
    }

    private boolean isEqualToReversed(Line that) {
        return (point1 == that.point2) && (point2 == that.point1);
    }


    public double getA() {
        return point1.y - point2.y;
    }

    public double getB() {
        return point2.x - point1.x;
    }

    public double getC() {
        return (point1.x * point2.y) - (point2.x * point1.y);
    }

    // y = ax + b;
    public double get_a() {
        return -(getA() / getB());
    }

    public double get_b() {
        return -(getC() / getB());
    }

    public boolean isParallel(Line line) {
        return Commons.compareDouble(parameters.A, line.parameters.A, 1.0e-3) &&
                Commons.compareDouble(parameters.B, line.parameters.B, 1.0e-3);
    }

    public boolean isPerpendicular(Line line) {
        return Commons.compareDouble(parameters.A * line.parameters.A, -parameters.B * line.parameters.B, 1.0e-3);
    }

    public double distance(Line line) {
        if (!isParallel(line))
            return 0;
        return (Math.abs(parameters.C - line.parameters.C)) /
                (Math.sqrt(parameters.A * parameters.A + parameters.B * parameters.B));
    }

    public double distance(Point p) {
        return Math.abs(parameters.A * p.x + parameters.B * p.y + parameters.C) /
                Math.sqrt(parameters.A * parameters.A + parameters.B * parameters.B);
    }

    public double angleBetweenLines(Line line) {
        if (isPerpendicular(line)) return Commons.PI / 2;
        return Math.atan((parameters.A * line.parameters.B - line.parameters.A * parameters.B) /
                (parameters.A * line.parameters.A + parameters.B * line.parameters.B));
    }

    public double angleBetweenLines2(Line line) {
        double angle = Math.atan2(point2.y - point1.y, point2.x - point1.x) - Math.atan2(line.point2.y - line.point1.y, line.point2.x - line.point1.x);
        if (angle < 0) {
            angle += 2 * Commons.M_PI;
        }
        return angle;
    }

    public Point getLeftPoint() {
        if (point1.x < point2.x) {
            return point1;
        } else {
            return point2;
        }
    }


    public Point getRightPoint() {
        if (point1.x < point2.x) {
            return point2;
        } else {
            return point1;
        }
    }


    public Point getBottomPoint() {
        if (point1.y < point2.y) {
            return point1;
        } else {
            return point2;
        }
    }

    public Point getTopPoint() {
        if (point1.y < point2.y) {
            return point2;
        } else {
            return point1;
        }
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public boolean containsPoint(Point point) {
        Point left = getLeftPoint();
        Point right = getRightPoint();
        Point top = getTopPoint();
        Point bottom = getBottomPoint();

        if (left.x < point.x && point.x < right.x && bottom.y < point.y && point.y < top.y) {
            double a = get_a();
            double b = get_b();
            if (Commons.compareDouble(point.y, a * point.x + b, 1.0e-3)) {
                return true;
            }
        }
        return false;
    }

    public Point intersectionPoint(Line line) {
        //Ax+By+C=0
        // y = -A/Bx -C/B

        // y = ax + c
        // y = bx + d

        double a = get_a();
        double c = get_b();

        double b = line.get_a();
        double d = line.get_b();

        Point p = new Point();
        p.x = ((d - c) / (a - b));
        p.y = ((a * d - b * c) / (a - b));

        return p;
    }

    // y = -(Ax + C) / B
    double getY(double x) {
        return -((getA() * x + getC()) / getB());
    }

    // Converts degrees to radians.
    double degreesToRadians(double angleDegrees) {
        return angleDegrees * Commons.M_PI / 180.0;
    }

    // Converts radians to degrees.
    double radiansToDegrees(double angleRadians) {
        return angleRadians * 180.0 / Commons.M_PI;
    }

    private class Parameters {
        double A, B, C;

        public Parameters(double a, double b, double c) {
            A = a;
            B = b;
            C = c;
        }
    }
}
