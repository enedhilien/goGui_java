package gogui.wingedEdge;

import gogui.Point;

/**
 * Created by testuser on 12/10/14.
 */
public class Vertex extends Point{
    Edge edge;
    int id;

    public Vertex(Edge e) {
        this.edge = e;
    }

    public Vertex(double x, double y, int id){
        super(x,y);
        this.id = id;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static Vertex from(Vertex p) {
        Vertex newPoint = new Vertex(p.x, p.y, p.id);
        newPoint.setStatus(p.getStatus());
        if (p.hasCustomColor()) {
            newPoint.setColor(p.getColor());
        }
        return newPoint;
    }
}
