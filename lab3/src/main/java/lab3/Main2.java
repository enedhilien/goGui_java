package lab3;

import gogui.GeoList;
import gogui.Line;
import gogui.Point;
import lab3.structures.LinePair;
import lab3.structures.Q;
import lab3.structures.T;

import java.util.*;

import static gogui.GoGui.*;

public class Main2 {

    public static void main(String[] args) {
        fireAlgorithm("input2");
    }

    private static void fireAlgorithm(String fileName) {
        GeoList<Line> lines = loadLinesFromJson(Main.LAB3_SRC_MAIN_RESOURCES + fileName + Main.INPUT_FILE_EXTENSION);
        GeoList<Point> points = new GeoList<>();
        GeoList<Line> helper = new GeoList<>();

        Map<Point, Line> pointToLine = new HashMap<>();

        for (Line line : lines) {
            points.push_back(line.getPoint1());
            points.push_back(line.getPoint2());
            pointToLine.put(line.getPoint1(), line);
            pointToLine.put(line.getPoint2(), line);
        }

        Q q = new Q(points);
        T t = new T();
        snapshot();

        Line broomstick = null;
        while (q.hasNext()) {
            Point p = q.next();
            broomstick = getNextBroomstick(helper, broomstick, p.x);

            if (q.isIntersectionPoint(p)) {

                t.swapIntersectionLines(p);
                LinePair intersectionLines = t.getIntersectionLines(p);

                Line l1 = intersectionLines.l1;
                Line l2 = intersectionLines.l2;

                findIntersectionsWithNeighbouringLines(l1, q, t);
                findIntersectionsWithNeighbouringLines(l2, q, t);

            } else {
                pointToLine.get(p).activate();
                Line currentLine = pointToLine.get(p);

                Point lineSecondPoint = getAnotherEnd(p, currentLine);

                if (p.x < lineSecondPoint.x) {
                    t.add(currentLine, p.x);

                    findIntersectionsWithNeighbouringLines(currentLine, q, t);

                } else { // right end of segment
                    Optional<Line> leftNeighbor = t.getRightNeighbor(currentLine);
                    Optional<Line> rightNeighbor = t.getLeftNeighbor(currentLine);

                    if (leftNeighbor.isPresent() && rightNeighbor.isPresent()) {
                        Line left = leftNeighbor.get();
                        Line right = rightNeighbor.get();

                        processNeighboringLine(q, left, right, t);
                    }
                    t.remove(currentLine);

                }
                currentLine.processed();
//            snapshot();
                helper.clear();
            }
        }

        saveJSON("C:\\home\\aaaaStudia\\Semestr_VII\\Geometria\\gogui\\visualization-grunt\\public\\data\\sweep." + fileName + ".data.json");
    }

    private static void findIntersectionsWithNeighbouringLines(Line currentLine, Q q, T t) {
        Optional<Line> rightNeighbor = t.getRightNeighbor(currentLine);

        if (rightNeighbor.isPresent()) {
            Line line = rightNeighbor.get();

            processNeighboringLine(q, currentLine, line, t);
        }

        Optional<Line> leftNeighbor = t.getLeftNeighbor(currentLine);

        if (leftNeighbor.isPresent()) {
            Line line = leftNeighbor.get();

            processNeighboringLine(q, currentLine, line, t);
        }
    }

    private static void processNeighboringLine(Q q, Line currentLine, Line line, T t) {
        line.activate();
        snapshot();
        findIntersection(currentLine, line, q, t);
    }

    private static Point getAnotherEnd(Point knownPoint, Line line) {
        Point other;
        if (knownPoint == line.getPoint1()) {
            other = line.getPoint2();
        } else {
            other = line.getPoint1();
        }
        return other;
    }

    private static Line getNextBroomstick(GeoList<Line> helper, Line broomstick, double x) {
        if (broomstick == null) {
            broomstick = new Line(new Point(x, 0.0), new Point(x, 1000.0));
        } else {
            helper.remove(broomstick);
            broomstick = new Line(new Point(x, 0.0), new Point(x, 1000.0));
        }

        helper.push_back(broomstick);
        return broomstick;
    }

    private static void findIntersection(Line l1, Line l2, Q q, T t) {
        Point intersection = l1.intersectionPoint(l2);

        if (l1.containsPoint(intersection) && l2.containsPoint(intersection)) {
            System.out.println("Lines : " + l1 + " and " + l2 + " intersects at: " + intersection);
//            intersectionPoints.push_back(intersection);
            if (!q.isIntersectionPoint(intersection)) {
                q.addPoint(intersection);
                q.addIntersectionPoint(intersection);
                t.addIntersectionLines(intersection, l1, l2);
            }
            snapshot();
        } else {
            System.out.println("Intersection is null : ");
        }
    }
}
