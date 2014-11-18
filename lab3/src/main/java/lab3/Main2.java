package lab3;

import gogui.GeoList;
import gogui.Line;
import gogui.Point;
import lab3.structures.Q;
import lab3.structures.T;

import java.util.*;

import static gogui.GoGui.*;

public class Main2 {

    public static void main(String[] args) {
        fireAlgorithm("input");
    }

    private static void fireAlgorithm(String fileName) {
        GeoList<Line> lines = loadLinesFromJson(Main.LAB3_SRC_MAIN_RESOURCES + fileName + Main.INPUT_FILE_EXTENSION);
        GeoList<Point> points = new GeoList<>();
        GeoList<Line> helper = new GeoList<>();

//        GeoList<Point> intersectionPoints = new GeoList<>();
//        Map<Point, LinePair> intersectionLines = new HashMap<>();

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

//        GeoList<Line> T = new GeoList<>(false);

        Line broomstick = null;
        while (q.hasNext()) {
            Point p = q.next();
            broomstick = getBroomstick(helper, broomstick, p.x);

            pointToLine.get(p).activate();
            snapshot();

            Line currentLine = pointToLine.get(p);

            Point lineSecondPoint = getAnotherEnd(p, currentLine);

            //            if (q.isIntersectionPoint(p)) {
//                LinePair linePair = intersectionLines.get(p);
//                changedLines.add(linePair);
//                swapRequiredLines(T, changedLines);
//
//                Line leftNeighbor = getRightNeighbor(linePair.l1, T);
//                Line rightNeighbor = getLeftNeighbor(linePair.l1, T);
//
//                findIntersection(linePair.l1, leftNeighbor, intersectionPoints, intersectionLines, q);
//                findIntersection(linePair.l1, rightNeighbor, intersectionPoints, intersectionLines, q);
//
//                leftNeighbor = getRightNeighbor(linePair.l2, T);
//                rightNeighbor = getLeftNeighbor(linePair.l2, T);
//                findIntersection(linePair.l2, leftNeighbor, intersectionPoints, intersectionLines, q);
//                findIntersection(linePair.l2, rightNeighbor, intersectionPoints, intersectionLines, q);
//            }

            if (p.x < lineSecondPoint.x) {
                t.add(currentLine, p.x);
//                T.push_back(currentLine);
//                Collections.sort(T, lineComparator);
//                swapRequiredLines(T, changedLines);

                Optional<Line> leftNeighbor = t.getRightNeighbor(currentLine);

                if (leftNeighbor.isPresent()) {
                    Line line = leftNeighbor.get();

                    processNeighboringLine(q, currentLine, line);
                }

                Optional<Line> rightNeighbor = t.getLeftNeighbor(currentLine);

                if (rightNeighbor.isPresent()) {
                    Line line = rightNeighbor.get();

                    processNeighboringLine(q, currentLine, line);
                }
            } else { // right end of segment

                Optional<Line> leftNeighbor = t.getRightNeighbor(currentLine);
                Optional<Line> rightNeighbor = t.getLeftNeighbor(currentLine);

                if (leftNeighbor.isPresent() && rightNeighbor.isPresent()) {
                    Line left = leftNeighbor.get();
                    Line right = rightNeighbor.get();

                    processNeighboringLine(q, left, right);

//                    left.activate();
//                    snapshot();
//                    findIntersection(left, right, q);
                }
                t.remove(currentLine);
            }
            currentLine.processed();
            snapshot();
            helper.clear();
        }

        saveJSON("C:\\home\\aaaaStudia\\Semestr_VII\\Geometria\\gogui\\visualization-grunt\\public\\data\\sweep." + fileName + ".data.json");
    }

    private static void processNeighboringLine(Q q, Line currentLine, Line line) {
        line.activate();
        snapshot();
        findIntersection(currentLine, line, q);
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

    private static Line getBroomstick(GeoList<Line> helper, Line broomstick, double x) {
        if (broomstick == null) {
            broomstick = new Line(new Point(x, 0.0), new Point(x, 1000.0));
        } else {
            helper.remove(broomstick);
            broomstick = new Line(new Point(x, 0.0), new Point(x, 1000.0));
        }

        helper.push_back(broomstick);
        return broomstick;
    }

    private static void findIntersection(Line l1, Line l2, Q q) {
        Point intersection = l1.intersectionPoint(l2);

        if (l1.containsPoint(intersection) && l2.containsPoint(intersection)) {
            System.out.println("Lines : " + l1 + " and " + l2 + " intersects at: " + intersection);
//            intersectionPoints.push_back(intersection);
//            q.addPoint(intersection);
            q.addIntersectionPoint(intersection);
            snapshot();
        } else {
            System.out.println("Intersection is null : ");
        }
    }
}
