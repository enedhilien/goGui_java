package project.structures;

public class HalfEdge {

    public HalfEdge prev, next, sibling;
    public PointWithEdge start;
    public Wall incidentWall;

    public HalfEdge(PointWithEdge startPoint) {
        this.start = startPoint;
    }


    public void makeSibling(HalfEdge e1, Wall value) {
        this.sibling = e1;
        e1.sibling = this;
        if (this.incidentWall == null) {
            this.incidentWall = value;
        }
    }
}
