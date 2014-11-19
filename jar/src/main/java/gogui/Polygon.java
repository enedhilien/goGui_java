package gogui;

public class Polygon extends GeoObject {

    GeoList<Point> points;
    GeoList<Line> lines;


    public Polygon(GeoList<Point> points) {
        this.points = new GeoList<>(points);

        lines = joinedPolygonPoints();
    }

    private GeoList<Line> joinedPolygonPoints() {
        GeoList<Line> newLines = new GeoList<>();

        for (int i = 0; i < points.size() - 1; i++) {
            Line line = new Line(points.get(i), points.get(i + 1));
            newLines.add(line);
        }

        newLines.add(new Line(points.back(), points.get(0)));
        return newLines;
    }

    public int getPointsCount() {
        return points.size();
    }

    public GeoList<Point> getPoints() {
        return points;
    }
}
