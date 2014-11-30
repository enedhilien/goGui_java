package project.structures.graph.cycle;

import gogui.Point;
import project.structures.HalfEdge;

import java.util.List;

public class EdgesCycle {


    List<Point> points;
    List<HalfEdge> edges;

    public EdgesCycle(List<HalfEdge> allEdges) {
        this.edges = allEdges;
    }


    public List<HalfEdge> getEdges() {
        return edges;
    }
}
