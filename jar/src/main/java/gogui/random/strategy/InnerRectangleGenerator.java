package gogui.random.strategy;

import gogui.Point;

import java.util.ArrayList;
import java.util.List;

import static gogui.random.PointsGenerator.realRandom;

public class InnerRectangleGenerator implements PointGeneratorStrategy {

    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;

    private InnerRectangleGenerator(double xmin, double xmax, double ymin, double ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    public static InnerRectangleGenerator withinRectangle(double xmin, double xmax, double ymin, double ymax) {
        return new InnerRectangleGenerator(xmin, xmax, ymin, ymax);
    }

    @Override
    public List<Point> generate(int pointsCount) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < pointsCount; i++) {
            points.add(new Point(realRandom(xmin, xmax), realRandom(ymin, ymax)));
        }
        return points;
    }
}
