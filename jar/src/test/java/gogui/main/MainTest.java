package gogui.main;

import gogui.*;
import org.junit.Test;

public class MainTest {


    @Test
    public void aaa() throws Exception {

        GeoList<Point> cloud = new GeoList<>();
        Point p1 = new Point(2, 3);
        Point p2 = new Point(3, 4);
        Point p3 = new Point(1, 4);

        cloud.add(p1);
        GoGui.snapshot();

        cloud.push_back(p2);
        GoGui.snapshot();

        cloud.push_back(p3);
        GoGui.snapshot();


        GeoList<Line> lines = new GeoList<>();

        Line line1 = new Line(p1, p2);
        lines.push_back(line1);
        GoGui.snapshot();

        line1.setStatus(GeoObject.Status.ACTIVE);
        GoGui.snapshot();

        GoGui.getJSON();
        GoGui.saveJSON("C:\\home\\aaaaStudia\\Semestr_VII\\Geometria\\gogui\\visualization-grunt\\public\\data\\moje.data.json");
    }
}
