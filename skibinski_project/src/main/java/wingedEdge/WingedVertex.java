package wingedEdge;

import gogui.Point;

/**
 * Created by testuser on 12/10/14.
 */
public class WingedVertex extends Point{
    WingedEdge edge;
    int id;

    public WingedVertex(WingedEdge e) {
        this.edge = e;
    }

    public WingedVertex(double x, double y, int id){
        super(x,y);
        this.id = id;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static WingedVertex from(WingedVertex p) {
        WingedVertex newPoint = new WingedVertex(p.x, p.y, p.id);
        newPoint.setStatus(p.getStatus());
        if (p.hasCustomColor()) {
            newPoint.setColor(p.getColor());
        }
        return newPoint;
    }
}
