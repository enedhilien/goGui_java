package project;

import gogui.GeoList;
import gogui.GoGui;
import gogui.Point;
import gogui.Polygon;
import project.structures.HalfEdgeDataStructure;

import static gogui.GoGui.saveJSON;
import static gogui.GoGui.snapshot;

public class Main {


    private static final String INPUT_FILE_EXTENSION = ".txt";
    private static final String LAB4_SRC_MAIN_RESOURCES = "project\\src\\main\\resources\\";

    public static void main(String[] args) {

        String fileName = "map1";
        GeoList<Point> polygonPoints = GoGui.loadPoints_ZMUDA(LAB4_SRC_MAIN_RESOURCES + fileName + INPUT_FILE_EXTENSION);
        Polygon polygon = new Polygon(polygonPoints);

        HalfEdgeDataStructure halfEdgeDataStructure = HalfEdgeDataStructure.from(polygon);

        snapshot();
        saveJSON("project\\src\\main\\resources\\project." + fileName + ".data.json");
        saveJSON("results\\project." + fileName + ".data.json");
    }
}
