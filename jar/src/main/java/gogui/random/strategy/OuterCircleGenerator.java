package gogui.random.strategy;

import gogui.Point;
import gogui.random.PointsGenerator;

import java.util.ArrayList;
import java.util.List;

public class OuterCircleGenerator implements PointGeneratorStrategy {

    private double xcenter;
    private double ycenter;
    private double radius;

    private OuterCircleGenerator(double xcenter, double ycenter, double radius) {
        this.xcenter = xcenter;
        this.ycenter = ycenter;
        this.radius = radius;
    }

    public static OuterCircleGenerator withinCircle(double xcenter, double ycenter, double radius) {
        return new OuterCircleGenerator(xcenter, ycenter, radius);
    }


    @Override
    public List<Point> generate(int pointsCount) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < pointsCount; i++) {
            points.add(generatePointOnCircle(PointsGenerator.radianRandom()));
        }
        return points;
    }

    private Point generatePointOnCircle(double angle) {
        return new Point(xcenter + radius * Math.cos(angle), ycenter + radius * Math.sin(angle));
    }


}
