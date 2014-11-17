package gogui.random.strategy;

import gogui.Point;

import java.util.List;

public interface PointGeneratorStrategy {
    List<Point> generate(int pointsCount);
}
