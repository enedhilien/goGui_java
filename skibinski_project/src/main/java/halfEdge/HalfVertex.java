package halfEdge;

import gogui.Point;

/**
 * Created by testuser on 1/5/15.
 */
public class HalfVertex extends Point{
    HalfEdge edge;
    int id;

    public HalfVertex(HalfEdge e) {
        this.edge = e;
    }

    public HalfVertex(double x, double y, int id){
        super(x,y);
        this.id = id;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static HalfVertex from(HalfVertex p) {
        HalfVertex newPoint = new HalfVertex(p.x, p.y, p.id);
        newPoint.setStatus(p.getStatus());
        if (p.hasCustomColor()) {
            newPoint.setColor(p.getColor());
        }
        return newPoint;
    }

    public void setEdge(HalfEdge edge) {
        this.edge = edge;
    }
    
}
