package project.structures;

public class HalfEdge {

    HalfEdge prev, next, sibling;
    PointWithEdge start;
    Wall incidentWall;

    public HalfEdge(PointWithEdge startPoint) {
        this.start = startPoint;

    }
}
