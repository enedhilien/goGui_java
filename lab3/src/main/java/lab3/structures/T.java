package lab3.structures;

import gogui.Line;
import gogui.Point;

import java.util.*;

public class T {

    private List<Line> activeLines = new ArrayList<>();
    private Map<Point, LinePair> intersectionsLines = new HashMap<>();


    public void add(Line lineToInsert, double broomX) {
        if (activeLines.isEmpty()) {
            activeLines.add(lineToInsert);
            return;
        }

        int indexToInsert = 0;
        for (int i = 0; i < activeLines.size(); i++) {

            Line activeLine = activeLines.get(i);

            if (activeLine.getY(broomX) < lineToInsert.getY(broomX)) {
                indexToInsert = i;
                break;
            }
            indexToInsert++;
        }
        activeLines.add(indexToInsert, lineToInsert);
    }

    public Optional<Line> getRightNeighbor(Line currentLine) {
        Line neighborLine = null;

        int sIndex = activeLines.indexOf(currentLine);
        if (sIndex < activeLines.size() - 1) {
            neighborLine = activeLines.get(sIndex + 1);
        }
        return Optional.ofNullable(neighborLine);
    }

    public Optional<Line> getLeftNeighbor(Line currentLine) {
        Line neighborLine = null;

        int sIndex = activeLines.indexOf(currentLine);
        if (sIndex > 0) {
            neighborLine = activeLines.get(sIndex - 1);
        }
        return Optional.ofNullable(neighborLine);
    }

    public void remove(Line finishedLine) {
        activeLines.remove(finishedLine);
    }

    public void addIntersectionLines(Point intersectionLine, Line l1, Line l2) {
        intersectionsLines.put(intersectionLine, new LinePair(l1, l2));
    }

    public void swapIntersectionLines(Point p) {
        if (intersectionsLines.containsKey(p)) {
            LinePair intersectionLines = intersectionsLines.get(p);

            int index1 = activeLines.indexOf(intersectionLines.l1);
            int index2 = activeLines.indexOf(intersectionLines.l2);

            Collections.swap(activeLines, index1, index2);
        } else {
            throw new IllegalArgumentException("No lines intersection at Point: " + p);
        }
    }

    public LinePair getIntersectionLines(Point p) {
        if (intersectionsLines.containsKey(p)) {
            return intersectionsLines.get(p);
        } else {
            throw new IllegalArgumentException("No lines intersection at Point: " + p);
        }
    }
}
