package project.structures;

import gogui.Point;

public class PointWithEdge {

    public Point point;
    public HalfEdge incidentEdge;

    public PointWithEdge(Point point, HalfEdge halfEdge) {
        this.point = point;
        this.incidentEdge = halfEdge;
    }

    @Override
    public String toString() {
        return "PointWithEdge{" + point + '}';
    }
}
