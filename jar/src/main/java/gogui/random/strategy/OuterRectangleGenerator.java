package gogui.random.strategy;

import gogui.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static gogui.random.PointsGenerator.realRandom;

public class OuterRectangleGenerator implements PointGeneratorStrategy {


    private final Point p1;
    private final Point p2;
    private final Point p3;
    private final Point p4;

    private OuterRectangleGenerator(Point p1, Point p2, Point p3, Point p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public static OuterRectangleGenerator onRectangle(Point p1, Point p2, Point p3, Point p4) {
        return new OuterRectangleGenerator(p1, p2, p3, p4);
    }


    @Override
    public List<Point> generate(int pointsCount) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < pointsCount; i++) {
            points.add(generateNewPoint());
        }
        return points;
    }

    private Point generateNewPoint() {
        int side = Math.abs(new Random().nextInt()) % 4;

        switch (side) {
            // top
            case 0:
                return new Point(realRandom(p1.x, p2.x), p1.y);

            // left
            case 1:
                return new Point(p2.x, realRandom(p3.y, p2.y));

            // down
            case 2:
                return new Point(realRandom(p4.x, p3.x), p3.y);

            // right
            case 3:
                return new Point(p4.x, realRandom(p4.y, p1.y));

        }
        return null;
    }
}
