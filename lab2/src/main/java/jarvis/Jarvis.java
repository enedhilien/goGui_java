package jarvis;

import gogui.GeoList;
import gogui.GoGui;
import gogui.Point;
import gogui.random.PointsGenerator;
import gogui.random.strategy.OuterRectangleGenerator;

import java.util.List;

public class Jarvis {

    public static void main(String[] args) {
        List<Point> points = PointsGenerator.generate(100, OuterRectangleGenerator.onRectangle(new Point(-10, 10), new Point(10, 10), new Point(10, -10), new Point(-10, -10)));
        GeoList<Point> pointGeoList = new GeoList<>(points);
        GoGui.snapshot();

        GoGui.saveJSON("C:\\home\\aaaaStudia\\Semestr_VII\\Geometria\\gogui\\visualization-grunt\\public\\data\\jarvis.data.json");

    }

}
