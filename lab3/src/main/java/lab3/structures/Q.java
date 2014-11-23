package lab3.structures;

import gogui.GeoList;
import gogui.Line;
import gogui.Point;

import java.util.*;

public class Q {

    private GeoList<Point> guiPoints;
    private int currentIndex = -1;
    private ArrayList<Point> points = new ArrayList<>();
    private GeoList<Point> intersectionPoints = new GeoList<>();


    public Q(GeoList<Point> points) {
        this.guiPoints = new GeoList<>(points);
        this.points.addAll(points);
        Collections.sort(this.points);
    }

    public boolean hasNext() {
        return (currentIndex == -1 && points.size() > 0 ) || (currentIndex < points.size() - 1);
    }

    public Point next() {
        if (hasNext()) {
            return points.get(++currentIndex);
        } else {
            return null;
        }
    }

    public void addPoint(Point p) {
        int insertIndex = 0;

        for ( int i = 0 ; i < points.size() ; i++) {
            if ( points.get(i).x > p.x) {
                insertIndex = i;
                break;
            }
            insertIndex++;
        }
        points.add(insertIndex, p);
    }

    public void addIntersectionPoint(Point intersectionPoint) {
        intersectionPoint.activate();
        addPoint(intersectionPoint);
        intersectionPoints.add(intersectionPoint);
    }

    public boolean isIntersectionPoint(Point p) {
        return intersectionPoints.contains(p);
    }


    public Set<Point> getIntersectionPoints() {
        return new HashSet<>(intersectionPoints);
    }
}
