package gogui.history;

import gogui.GeoList;
import gogui.Line;
import gogui.Point;

public class State {

    private GeoList<Point> points;
    private GeoList<Line> lines;


    public State(GeoList<Point> points, GeoList<Line> lines) {

        this.points = points;
        this.lines = lines;

    }

    public GeoList<Point> getPoints() {
        return points;
    }

    public GeoList<Line> getLines() {
        return lines;
    }
}
