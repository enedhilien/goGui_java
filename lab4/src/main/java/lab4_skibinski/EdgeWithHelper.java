package lab4_skibinski;

import gogui.Line;
import gogui.Point;

/**
 * Created by testuser on 1/4/15.
 */
public class EdgeWithHelper extends Line{
    public Point helper;

    public EdgeWithHelper(Point p1, Point p2){
        super(p1,p2);
    }

    public EdgeWithHelper(Point p1, Point p2, Point helper){
        super(p1,p2);
        this.helper = helper;
    }
}
