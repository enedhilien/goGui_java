package project.structures.sweep;

import gogui.Line;
import gogui.Point;

import java.util.HashMap;
import java.util.Map;

public class T {

    private Map<Point, LinePair> intersectionsLines = new HashMap<>();

    public void addIntersectionLines(Point intersectionLine, Line l1, Line l2) {
        intersectionsLines.put(intersectionLine, new LinePair(l1, l2));
    }

    public LinePair getIntersectionLines(Point p) {
        if (intersectionsLines.containsKey(p)) {
            return intersectionsLines.get(p);
        } else {
            throw new IllegalArgumentException("No lines intersection at Point: " + p);
        }
    }
}
