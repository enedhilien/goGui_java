package jarvis;

import gogui.*;
import gogui.random.strategy.OuterRectangleGenerator;
import gogui.random.strategy.PointGeneratorStrategy;

import java.util.List;

import static gogui.GoGui.saveJSON;
import static gogui.GoGui.snapshot;

public class Jarvis {

    public static void main(String[] args) {
        PointGeneratorStrategy strategy = OuterRectangleGenerator.onRectangle(new Point(-10, 10), new Point(10, 10), new Point(10, -10), new Point(-10, -10));
//        PointGeneratorStrategy strategy = SquareWithDiagonal.byPoints(new Point(0, 0), new Point(10, 10));
        List<Point> points = strategy.generate(100);

//        List<Point> points  = Arrays.asList(new Point(-15,-10), new Point(10,5),new Point(16,-20),new Point(0,10));
//        List<Point> points = PointsGenerator.generate(10, InnerRectangleGenerator.withinRectangle(-10, 10, -10, 10));
        GeoList<Point> pointGeoList = new GeoList<>(points);
        snapshot();

        GeoList<Point> convexHull = findConvexHullWithJarvis(pointGeoList);
        convexHull.setStatus(GeoObject.Status.ACTIVE);

        GoGui.saveJSON("C:\\home\\aaaaStudia\\Semestr_VII\\Geometria\\gogui\\visualization-grunt\\public\\data\\jarvis.data.json");
        GoGui.saveJSON("lab2\\src\\main\\resources\\jarvis.data.json");
        saveJSON("results\\jarvis.data.json");
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

    static Point getNextHullPoint(GeoList<Point> points, Point lastHullPoint) {
        Point currentCandidate = points.get(0);

        for (int i = 1; i < points.size(); ++i) {
            Point nextCandidate = points.get(i);
            if (isBetterThanCurrentCandidate(nextCandidate, currentCandidate, lastHullPoint)) {
                currentCandidate = nextCandidate;
            }
        }
        currentCandidate.activate();

        return currentCandidate;
    }

    static boolean isBetterThanCurrentCandidate(Point asked, Point currentCandidate, Point lastConvexPoint) {
        double det_value = det(lastConvexPoint, currentCandidate, asked);
        return det_value < 0 || (det_value == 0.0 && lastConvexPoint.distance(asked)> lastConvexPoint.distance(currentCandidate));
    }

    static double det(Point x, Point y, Point z) {
        return (x.x - z.x) * (y.y - z.y) - (x.y - z.y) * (y.x - z.x);
    }

}
