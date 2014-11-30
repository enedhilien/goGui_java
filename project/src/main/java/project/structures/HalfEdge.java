package project.structures;

public class HalfEdge {

    public HalfEdge prev, next, sibling;
    public PointWithEdge start;
    public Wall incidentWall;

    public HalfEdge(PointWithEdge startPoint) {
        this.start = startPoint;

    }


    public void makeSibling(HalfEdge e1) {
        this.sibling = e1;
        e1.sibling = this;
    }
}
