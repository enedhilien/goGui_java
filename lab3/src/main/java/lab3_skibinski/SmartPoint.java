package lab3_skibinski;

import gogui.Point;

/**
 * Created by testuser on 12/15/14.
 */
public class SmartPoint extends Point {

    public STATE state;

    public SmartPoint(Point p, STATE state){
        super(p.x, p.y);
        this.state = state;
    }

    public static SmartPoint from(Point p, STATE s){
        return new SmartPoint(p, s);
    }


    public enum STATE{
        LEFT, RIGHT, INTERSECTION;
    }
}
