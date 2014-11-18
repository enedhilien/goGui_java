package jarvis;

import gogui.*;
import gogui.random.PointsGenerator;
import gogui.random.strategy.OuterRectangleGenerator;

import java.util.List;

import static gogui.GoGui.snapshot;

public class Jarvis {

    public static void main(String[] args) {
        List<Point> points = PointsGenerator.generate(100, OuterRectangleGenerator.onRectangle(new Point(-10, 10), new Point(10, 10), new Point(10, -10), new Point(-10, -10)));
//        List<Point> points = PointsGenerator.generate(10, InnerRectangleGenerator.withinRectangle(-10, 10, -10, 10));
        GeoList<Point> pointGeoList = new GeoList<>(points);
        snapshot();

        GeoList<Point> convexHull = findConvexHullWithJarvis(pointGeoList);
        convexHull.setStatus(GeoObject.Status.Active);

        GoGui.saveJSON("C:\\home\\aaaaStudia\\Semestr_VII\\Geometria\\gogui\\visualization-grunt\\public\\data\\jarvis.data.json");
        GoGui.saveJSON("lab2\\src\\main\\resources\\jarvis.data.json");
    }

    static GeoList<Point> findConvexHullWithJarvis(GeoList<Point> points) {
        Point first = getStartingPoint(points);
        first.activate();
        snapshot();


        GeoList<Point> convexHull = new GeoList<>();
        GeoList<Line> lines = new GeoList<>();

        convexHull.push_back(first);

        do {
            Point point = getNextHullPoint(points, convexHull.back());
            lines.add(new Line(convexHull.back(), point));
            snapshot();
            convexHull.push_back(point);
        } while (convexHull.back() != first);

        return convexHull;
    }

    static boolean isStartingPoint(Point a, Point b) {
        return a.y < b.y || (a.y == b.y && a.x < b.x);
    }

    static Point getStartingPoint(GeoList<Point> points) {
        int currentStartingIndex = 0;
        for (int i = 1; i < points.size(); ++i) {
            if (isStartingPoint(points.get(i), points.get(currentStartingIndex))) {
                currentStartingIndex = i;
            }
        }
        return points.get(currentStartingIndex);
    }

    static double det(Point x, Point y, Point z) {
        return (x.x - z.x) * (y.y - z.y) - (x.y - z.y) * (y.x - z.x);
    }

    static double dist2(Point a, Point b) {
        return (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y);
    }

    static boolean isBetterThanCurrentCandidate(Point asked, Point currentCandidate, Point lastConvexPoint) {
        double det_value = det(lastConvexPoint, currentCandidate, asked);
        return det_value > 0 || (det_value == 0.0 && dist2(lastConvexPoint, asked) > dist2(lastConvexPoint, currentCandidate));
    }

    static Point getNextHullPoint(GeoList<Point> points, Point lastHullPoint) {
        Point currentCandidate = points.get(0);

        for (int i = 1; i < points.size(); ++i) {
            Point point = points.get(i);
            if (isBetterThanCurrentCandidate(point, currentCandidate, lastHullPoint)) {
                currentCandidate = point;
            }
        }
        currentCandidate.activate();

        return currentCandidate;
    }





}
