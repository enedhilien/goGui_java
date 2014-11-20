package main;

import gogui.*;
import main.helpers.PointWithSide;
import main.helpers.Side;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static gogui.GoGui.saveJSON;
import static gogui.GoGui.snapshot;
import static java.util.stream.Collectors.toList;

public class Triangulation {

    public static final String LAB4_SRC_MAIN_RESOURCES = "lab4\\src\\main\\resources\\";
    public static final String INPUT_FILE_EXTENSION = ".json";
    private static final String STACK_POINTS_COLOR = "blue";
    private static final String LAST_FOUND_POINTS_COLOR = "blue";

    public static void main(String[] args) {

        String fileName = "input";

        GeoList<Point> polygonPoints = GoGui.loadPoints(LAB4_SRC_MAIN_RESOURCES + fileName + INPUT_FILE_EXTENSION);
        Polygon polygon = new Polygon(polygonPoints);
        GoGui.snapshot();

        triangulatePolygon(polygon);

        saveJSON("C:\\home\\aaaaStudia\\Semestr_VII\\Geometria\\gogui\\visualization-grunt\\public\\data\\triangulation." + fileName + ".data.json");
        saveJSON("lab4\\src\\main\\resources\\triangulation." + fileName + ".data.json");
        saveJSON("results\\triangulation." + fileName + ".data.json");
    }

    private static List<Line> triangulatePolygon(Polygon polygon) {

        List<PointWithSide> pointWithSides = allPointsWithSides(polygon);

        Stack<PointWithSide> stack = new Stack<>();

        stack.push(pointWithSides.get(0));
        stack.push(pointWithSides.get(1));

        GeoList<Line> foundLines = new GeoList<>();

        GeoList<Point> stackPoints = new GeoList<>();
        stackPoints.push_back(pointWithSides.get(0).getPoint());
        stackPoints.push_back(pointWithSides.get(1).getPoint());
        stackPoints.setColor(STACK_POINTS_COLOR);
        snapshot();

        for (int i = 2; i < pointWithSides.size(); i++) {
            PointWithSide nextPoint = pointWithSides.get(i);

            PointWithSide stackTopPoint = stack.peek();

            if (areOnDifferentSide(nextPoint, stackTopPoint)) {
                List<Line> collect = stack.stream().map(x -> new Line(nextPoint.getPoint(), x.getPoint())).collect(toList());

                drawFoundLines(foundLines, collect);

                stack.clear();
                stack.add(stackTopPoint);
                stack.add(nextPoint);
            } else {
                List<PointPair> pointsStackPairs = toPairs(stack);

                PointWithSide lastFromStack = stack.pop();
                for (PointPair pointPair : pointsStackPairs) {

                    if (!isInsidePolygon(nextPoint, pointPair.getP1(), pointPair.getP2(), polygon)) {
                        break;
                    }

                    drawFoundLines(foundLines, nextPoint, pointPair);
                    lastFromStack = stack.pop();
                }
                stack.push(lastFromStack);
                stack.push(nextPoint);
            }
        }
        return foundLines;
    }

    private static boolean isInsidePolygon(PointWithSide stackTopPoint, Point p1, Point p2, Polygon polygon) {

        Line line1 = new Line(stackTopPoint.getPoint(), p1);
        Line line2 = new Line(stackTopPoint.getPoint(), p2);

        for (Line line : polygon.getLines()) {
            Point intersection1 = line1.intersectionPoint(line);
            Point intersection2 = line2.intersectionPoint(line);

            if (line1.containsPoint(intersection1) && line.containsPoint(intersection1)) {
                return false;
            }

            if (line1.containsPoint(intersection2) && line.containsPoint(intersection2)) {
                return false;
            }

        }

        return true;
    }

    private static List<PointPair> toPairs(Stack<PointWithSide> stack) {
        List<PointPair> pairs = new ArrayList<>();
        for (int i = 0; i < stack.size() - 1; i++) {
            pairs.add(new PointPair(stack.get(i).getPoint(), stack.get(i + 1).getPoint()));
        }

        return pairs;
    }

    private static void drawFoundLines(GeoList<Line> foundLines, List<Line> collect) {
        collect.forEach(x -> x.setColor(LAST_FOUND_POINTS_COLOR));
        foundLines.addAll(collect);
        snapshot();
    }

    private static void drawFoundLines(GeoList<Line> foundLines, PointWithSide nextPoint, PointPair pointPair) {
        foundLines.add(new Line(nextPoint.getPoint(), pointPair.getP1()));
        foundLines.add(new Line(nextPoint.getPoint(), pointPair.getP2()));
        foundLines.forEach(x -> x.setColor(LAST_FOUND_POINTS_COLOR));
        snapshot();
    }

    private static List<PointWithSide> allPointsWithSides(Polygon polygon) {

        List<Point> allPoints = getOrderedPoints(polygon);

        Point topYPoint = allPoints.get(0);
        Point bottomYPoint = allPoints.get(allPoints.size() - 1);

        List<Point> getLeftPath = getLeftPath(polygon, topYPoint, bottomYPoint);
        List<Point> getRightPath = getRightPath(polygon, topYPoint, bottomYPoint);
        getRightPath.remove(getRightPath.size() - 1);
        getRightPath.add(topYPoint);

        return toPointsWithSides(getLeftPath, getRightPath);
    }

    private static List<PointWithSide> toPointsWithSides(List<Point> getLeftPath, List<Point> getRightPath) {
        List<PointWithSide> pointWithSides = new ArrayList<>();
        for (Point point : getLeftPath) {
            pointWithSides.add(new PointWithSide(point, Side.LEFT));
        }
        for (Point point : getRightPath) {
            pointWithSides.add(new PointWithSide(point, Side.RIGHT));
        }
        pointWithSides.sort(PointWithSide.comparator);
        return pointWithSides;
    }

    private static boolean areOnDifferentSide(PointWithSide nextPoint, PointWithSide stackTopPoint) {
        return !nextPoint.getSide().equals(stackTopPoint.getSide());
    }

    private static List<Point> getRightPath(Polygon polygon, Point topYPoint, Point bottomYPoint) {
        List<Point> result = new ArrayList<>();
        Point lastNeighbor = topYPoint;
        do {
            lastNeighbor = polygon.getRightNeighbor(lastNeighbor);
            result.add(lastNeighbor);
        } while (!lastNeighbor.equals(bottomYPoint));

        return result;
    }

    private static List<Point> getLeftPath(Polygon polygon, Point topYPoint, Point bottomYPoint) {

        List<Point> result = new ArrayList<>();
        Point lastNeighbor = topYPoint;
        do {
            lastNeighbor = polygon.getLeftNeighbor(lastNeighbor);
            result.add(lastNeighbor);

        } while (!lastNeighbor.equals(bottomYPoint));

        return result;
    }

    private static List<Point> getOrderedPoints(Polygon polygon) {
        List<Point> points = new ArrayList<>(polygon.getPoints());
        Collections.sort(points, (point1, point2) -> {
            if (point1.y < point2.y) {
                return 1;
            } else if (point1.y > point2.y) {
                return -1;
            }
            return 0;
        });

        return points;
    }

    private static class PointPair {

        Point p1;
        Point p2;

        private PointPair(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public Point getP1() {
            return p1;
        }

        public Point getP2() {
            return p2;
        }
    }
}
