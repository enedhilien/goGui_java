package lab3.structures;

import gogui.GeoList;
import gogui.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Q {

    private GeoList<Point> points;
    private Point currentPoint;
    private GeoList<Point> intersectionPoints = new GeoList<>();

    public Q(GeoList<Point> points) {
        this.points = points;
        Collections.sort(points);
    }

    public Point currentPoint() {
        return currentPoint;
    }

    public boolean hasNext() {
        return points.indexOf(currentPoint()) < points.size() - 1;
    }

    public Point next() {
        if (hasNext()) {
            int currentIndex = points.indexOf(currentPoint);
            currentPoint = points.get(currentIndex + 1);
            return currentPoint;
        } else {
            return null;
        }
    }

    public void addPoint(Point p) {
        points.add(p);
        Collections.sort(points);
    }

    public void addIntersectionPoint(Point p) {
        intersectionPoints.add(p);
    }

    public boolean isIntersectionPoint(Point p) {
        return intersectionPoints.contains(p);
    }


}
