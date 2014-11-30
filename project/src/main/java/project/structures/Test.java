package project.structures;

import gogui.GeoList;
import gogui.Line;
import gogui.Point;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Test {

    public static void main(String[] args) {

        GeoList<Point> points = new GeoList<>();
        points.add(new Point(-1, -1));
        points.add(new Point(-1, 1));
        points.add(new Point(1, 1));
        points.add(new Point(1, -1));

        points.add(new Point(-2, 0.5));
        points.add(new Point(1, 5));
        points.add(new Point(0.5, -3));
        Collections.shuffle(points);


        Point v = new Point(0, 0);
        Line baseLine = new Line(new Point(-1, 0), new Point(1, 0));

        Collections.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {

                Line newLine = new Line(v, p1);
                Line newLine2 = new Line(v, p2);

                if (baseLine.angleBetweenLines2(newLine) < baseLine.angleBetweenLines2(newLine2)) {
                    return -1;
                } else if (baseLine.angleBetweenLines2(newLine) > baseLine.angleBetweenLines2(newLine2)) {
                    return 1;
                }
                return 0;
            }
        });

        System.out.println(Arrays.toString(points.toArray()));

    }

}
