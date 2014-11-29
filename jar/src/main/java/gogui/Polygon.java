package gogui;

public class Polygon extends GeoObject {

    GeoList<Point> points;
    GeoList<Line> lines;

    public Polygon(GeoList<Point> points) {
        this.points = points;
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

    public Point getNextNeighbor(Point point) {
        int pointIndex = points.indexOf(point);
        Point result;

        if (pointIndex == points.size() -1) {
            result = points.front();
        } else {
            result = points.get(pointIndex + 1);
        }
        return result;
    }

    public Point getPrevNeighbor(Point point) {
        int pointIndex = points.indexOf(point);
        Point result;

        if (pointIndex == 0) {
            result = points.back();
        } else {
            result = points.get(pointIndex - 1);
        }
        return result;

    }

    @Override
    public void setColor(String color) {
        points.forEach(x -> x.setColor(color));
        lines.forEach(x -> x.setColor(color));
    }

    public GeoList<Line> getLines() {
        return lines;
    }
}
