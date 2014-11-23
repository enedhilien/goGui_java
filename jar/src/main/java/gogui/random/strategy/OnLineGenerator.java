package gogui.random.strategy;

import gogui.GeoList;
import gogui.Line;
import gogui.Point;
import gogui.random.PointsGenerator;

import java.util.List;

/**
 * Generates points on line
 */
public class OnLineGenerator implements PointGeneratorStrategy {

    private static Line line;

    public OnLineGenerator(Line line) {
        this.line = line;
    }


    public static OnLineGenerator onLine(Line line) {
        OnLineGenerator.line = line;
        return new OnLineGenerator(line);
    }

    @Override
    public List<Point> generate(int pointsCount) {
        List<Point> result = new GeoList<>();

        double x_min = Math.min(line.getLeftPoint().x, line.getRightPoint().x);
        double x_max = Math.max(line.getLeftPoint().x, line.getRightPoint().x);
        double y_min = Math.min(line.getLeftPoint().y, line.getRightPoint().y);
        double y_max = Math.max(line.getLeftPoint().y, line.getRightPoint().y);

        for (int i = 0; i < pointsCount; i++) {
            double x = PointsGenerator.realRandom(x_min, x_max);
            double y;
            if (x_min == 0 && x_max == 0) {
                y = PointsGenerator.realRandom(y_min, y_max);
            } else {
                y = line.getY(x);
            }
            result.add(new Point(x, y));
        }
        return result;
    }
}
