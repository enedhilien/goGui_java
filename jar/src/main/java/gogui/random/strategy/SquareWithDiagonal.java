package gogui.random.strategy;

import gogui.GeoList;
import gogui.Line;
import gogui.Point;

import java.util.List;

/**
 * Generates points on square's bottom and left side and on its diagonals
 */
public class SquareWithDiagonal implements PointGeneratorStrategy {

    private final Point bottomLeft;
    private final Point topRight;
    private final Point bottomRight;
    private final Point topLeft;

    private SquareWithDiagonal(Point bottomLeft, Point topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;

        this.topLeft = new Point(bottomLeft.x, topRight.y);
        this.bottomRight = new Point(topRight.x, bottomLeft.y);

    }

    public static SquareWithDiagonal byPoints(Point bottomLeft, Point topRight) {
        return new SquareWithDiagonal(bottomLeft, topRight);
    }


    /**
     * @param pointsCount - number of points generated on 2 square edges and diagonals
     * @return - generated points
     */
    @Override
    public List<Point> generate(int pointsCount) {

        Line diagonal1 = new Line(bottomLeft, topRight);
        Line diagonal2 = new Line(topLeft, bottomRight);

        Line edgeX = new Line(bottomLeft, bottomRight);
        Line edgeY = new Line(bottomLeft, topLeft);

        List<Point> result = new GeoList<>();
        result.addAll(OnLineGenerator.onLine(edgeX).generate(pointsCount));
        result.addAll(OnLineGenerator.onLine(edgeY).generate(pointsCount));
        result.addAll(OnLineGenerator.onLine(diagonal1).generate(pointsCount));
        result.addAll(OnLineGenerator.onLine(diagonal2).generate(pointsCount));
        return result;
    }
}
