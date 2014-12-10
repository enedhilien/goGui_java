package gogui.random.strategy;


import gogui.Point;

import java.util.ArrayList;
import java.util.List;

public class WielokontGeneratorStrategy implements PointGeneratorStrategy {
    private List<Point> vertices;

    private WielokontGeneratorStrategy(List<Point> vertices) {
        this.vertices = vertices;
    }

    public static WielokontGeneratorStrategy onWielokont(List<Point> vertices) {
        return new WielokontGeneratorStrategy(vertices);
    }

    @Override
    public List<Point> generate(int pointsCount) {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < vertices.size() - 1; i++) {
            points.addAll(LineGenerator.betweenPoints(vertices.get(i),
                    vertices.get(i + 1)).generate(pointsCount / vertices.size()));
        }
        points.addAll(LineGenerator.betweenPoints(vertices.get(0), vertices.get(vertices.size() - 1)).generate(pointsCount / vertices.size()));
        return points;

    }
}
