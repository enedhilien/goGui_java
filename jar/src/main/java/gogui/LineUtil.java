package gogui;

/**
 * Created by testuser on 12/15/14.
 */
public class LineUtil {

    private static Line broomstick= null;

    public static Line getNextBroomstick(GeoList<Line> helper, double x, double heigth) {
        if (broomstick == null) {
            broomstick = new Line(new Point(x, 0.0), new Point(x, heigth));
        } else {
            helper.remove(broomstick);
            broomstick = new Line(new Point(x, 0.0), new Point(x, heigth));
        }

        helper.push_back(broomstick);
        return broomstick;
    }
}
