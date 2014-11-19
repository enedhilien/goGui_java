package main;

import gogui.GeoList;
import gogui.GoGui;
import gogui.Point;
import gogui.Polygon;

import static gogui.GoGui.saveJSON;

public class Main {

    public static final String LAB4_SRC_MAIN_RESOURCES = "lab4\\src\\main\\resources\\";
    public static final String INPUT_FILE_EXTENSION = ".json";

    public static void main(String[] args) {

        String fileName = "input";

        GeoList<Point> polygonPoints = GoGui.loadPoints(LAB4_SRC_MAIN_RESOURCES + fileName + INPUT_FILE_EXTENSION);
        Polygon polygon = new Polygon(polygonPoints);
        GoGui.snapshot();


        saveJSON("C:\\home\\aaaaStudia\\Semestr_VII\\Geometria\\gogui\\visualization-grunt\\public\\data\\triangulation." + fileName + ".data.json");
        GoGui.saveJSON("lab4\\src\\main\\resources\\triangulation." + fileName + ".data.json");

        System.out.println(polygon.getPointsCount());
        System.out.println(isYMonotic(polygon));
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
