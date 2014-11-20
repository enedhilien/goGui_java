package main.helpers;

import gogui.Point;

import java.util.Comparator;

public class PointWithSide {

    public static Comparator<PointWithSide> comparator = (point1, point2) -> {
        if (point1.y() < point2.y()) {
            return 1;
        } else if (point1.y() > point2.y()) {
            return -1;
        }
        return 0;
    };

    Point point;
    Side side;

    public PointWithSide(Point point, Side side) {
        this.point = point;
        this.side = side;
    }

    public Point getPoint() {
        return point;
    }

    public Side getSide() {
        return side;
    }

    public double y() {
        return point.y;
    }

    @Override
    public String toString() {
        return "PointWithSide{" + point + ", side=" + side + '}';
    }
}
