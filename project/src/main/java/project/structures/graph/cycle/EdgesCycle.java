package project.structures.graph.cycle;

import gogui.Line;
import gogui.Point;
import project.structures.HalfEdge;
import project.structures.Wall;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class EdgesCycle {

    List<Point> points;
    List<HalfEdge> edges;

    public EdgesCycle(List<HalfEdge> allEdges) {
        this.edges = allEdges;
    }


    public List<HalfEdge> getEdges() {
        return edges;
    }

    public List<Line> getLines() {

        List<Line> result = new ArrayList<>();
        for (HalfEdge edge : edges) {
            Line l = new Line(edge.start.point, edge.next.start.point);
            result.add(l);
        }

        return result;
    }

    public boolean isIntersection() {
        Set<Wall> walls = edges.stream().map(x -> x.incidentWall).collect(toSet());
        Set<Wall> collect = walls.stream().filter(x -> x == null || !"OUTER".equals(x.name)).collect(toSet());
        return collect.size() > 1;
    }
}
