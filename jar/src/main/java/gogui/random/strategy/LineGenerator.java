package gogui.random.strategy;


import gogui.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineGenerator implements PointGeneratorStrategy {
    private Point b;
    private Point a;
    private Random rand = new Random();

    private LineGenerator(Point b, Point a) {
        this.b = b;
        this.a = a;
    }

    public static LineGenerator betweenPoints(Point a, Point b) {
        return new LineGenerator(a, b);
    }

    @Override
    public List<Point> generate(int pointsCount) {
        ArrayList<Point> points = new ArrayList<Point>();
        double x, y;
        double min_x = Math.min(a.x, b.x);
        double max_x = Math.max(a.x, b.x);
        double min_y = Math.min(a.y, b.y);
        double max_y = Math.max(a.y, b.y);
        for (int i = 0; i < pointsCount; i++) {
            x = rand.nextDouble();
            x = min_x + x * (max_x - min_x);
            if (a.x != b.x) {
                y = a.y + (b.y - a.y) * (x - a.x) / (b.x - a.x);
            } else {
                y = rand.nextDouble();
                y = min_y + y * max_y - min_y;
            }

            points.add(new Point(x, y));
        }

        return points;
    }
}


