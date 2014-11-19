package main;

import gogui.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static gogui.GeoObject.Status;
import static gogui.GoGui.saveJSON;
import static gogui.GoGui.snapshot;

public class Main {

    public static final String LAB4_SRC_MAIN_RESOURCES = "lab4\\src\\main\\resources\\";
    public static final String INPUT_FILE_EXTENSION = ".json";


    public static void main(String[] args) {

        String fileName = "input3";

        GeoList<Point> polygonPoints = GoGui.loadPoints(LAB4_SRC_MAIN_RESOURCES + fileName + INPUT_FILE_EXTENSION);
        Polygon polygon = new Polygon(polygonPoints);
        GoGui.snapshot();

        System.out.println(polygon.getPointsCount());
        System.out.println(isYMonotic(polygon));

        Map<Point, PointClassification> pointPointClassificationMap = classifyPoints(polygon);
        colorPoints(pointPointClassificationMap);
        snapshot();

        saveJSON("C:\\home\\aaaaStudia\\Semestr_VII\\Geometria\\gogui\\visualization-grunt\\public\\data\\triangulation." + fileName + ".data.json");
        GoGui.saveJSON("lab4\\src\\main\\resources\\triangulation." + fileName + ".data.json");
    }

    private static GeoList<Point> colorPoints(Map<Point, PointClassification> pointPointClassificationMap) {
        for (Map.Entry<Point, PointClassification> pointPointClassificationEntry : pointPointClassificationMap.entrySet()) {
            pointPointClassificationEntry.getKey().setColor(pointPointClassificationEntry.getValue().getColor());
        }
        return new GeoList<>(pointPointClassificationMap.keySet().stream().collect(Collectors.toList()));
    }

    private static Map<Point, PointClassification> classifyPoints(Polygon polygon) {
        Map<Point, PointClassification> classificationMap = new HashMap<>();

        for (Point point : polygon.getPoints()) {

            Point leftNeighbor = polygon.getLeftNeighbor(point);
            Point rightNeighbor = polygon.getRightNeighbor(point);

            PointClassification classificationResult = classifyPoint(point, leftNeighbor, rightNeighbor);
            classificationMap.put(point, classificationResult);
        }

        return classificationMap;
    }

    private static PointClassification classifyPoint(Point point, Point leftNeighbor, Point rightNeighbor) {

        PointClassification classificationResult;

        GeoList<Line> currentLines = new GeoList<>();

        Line l1 = new Line(leftNeighbor, point);
        Line l2 = new Line(rightNeighbor, point);

        currentLines.addAll(Arrays.asList(l1, l2));
        currentLines.setStatus(Status.ACTIVE);
        snapshot();

        if (isBelow(point, leftNeighbor) && isBelow(point, rightNeighbor)) {
            if (getOrientation(new Line(leftNeighbor, point), rightNeighbor) < 0) {
                classificationResult = PointClassification.END;
            } else {
                classificationResult = PointClassification.MERGE;
            }
        } else if (isOver(point, leftNeighbor) && isOver(point, rightNeighbor)) {
            if (getOrientation(new Line(leftNeighbor, point), rightNeighbor) < 0) {
                classificationResult = PointClassification.START;
            } else {
                classificationResult = PointClassification.SPLIT;
            }
        } else {
            classificationResult = PointClassification.REGULAR;
        }
        currentLines.setStatus(Status.NORMAL);
        snapshot();
        return classificationResult;
    }

    private static boolean isOver(Point point, Point comparingTo) {
        return point.y > comparingTo.y;
    }

    private static boolean isBelow(Point point, Point comparingTo) {
        return point.y < comparingTo.y;
    }

    static double getOrientation(Line vec, Point point) {
        Point a1 = vec.getPoint1().minus(point);
        Point b1 = vec.getPoint2().minus(point);
        return a1.x * b1.y - a1.y * b1.x;
    }

    private static boolean isYMonotic(Polygon polygon) {

        GeoList<Point> points = polygon.getPoints();
        int localMinimumCount = 0;

        for (int i = 1; i < points.size() - 1; i++) {
            if ((points.get(i).y < points.get(i - 1).y) && (points.get(i).y < points.get(i + 1).y)) {
                localMinimumCount++;
            }
        }
        if ((points.get(0).y < points.back().y) && (points.get(0).y < points.get(1).y)) {
            localMinimumCount++;
        }
        return localMinimumCount == 1;
    }
}
