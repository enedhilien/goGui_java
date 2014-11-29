package project.structures;

import gogui.Point;
import gogui.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class HalfEdgeDataStructure {

    List<Wall> walls = new ArrayList<>();
    List<PointWithEdge> points = new ArrayList<>();
    List<HalfEdge> edges = new ArrayList<>();

    public HalfEdgeDataStructure(Wall wall, List<PointWithEdge> pointsWithEdges, List<HalfEdge> edges) {
        walls.add(wall);
        this.points = pointsWithEdges;
        this.edges = edges;
    }

    public static HalfEdgeDataStructure from(Polygon polygon) {
        Wall wall = new Wall();

        List<PointWithEdge> pointsWithEdges = polygon.getPoints().stream().map(x -> new PointWithEdge(x, null)).collect(toList());

        HalfEdge lastCW = null;
        HalfEdge lastCCW = null;
        HalfEdge firstToSecond = null;
        HalfEdge secondToFirst = null;

        List<HalfEdge> edges = new ArrayList<>();

        for (int i = 0; i < pointsWithEdges.size(); i++) {
            PointWithEdge thisPoint = pointsWithEdges.get(i);
            PointWithEdge nextPoint = getNextNeighbor(thisPoint, pointsWithEdges, polygon);

            HalfEdge edgeToNext = new HalfEdge(thisPoint);
            thisPoint.incidentEdge = edgeToNext;
            HalfEdge edgeFromNext = new HalfEdge(nextPoint);

            if (firstToSecond == null) {
                firstToSecond = edgeToNext;
            }
            if (secondToFirst == null) {
                secondToFirst = edgeFromNext;
            }

            addSiblingsToCollection(edges, edgeToNext, edgeFromNext);

            joinSiblingsIntoPair(edgeToNext, edgeFromNext);

            if (lastCCW != null) {
                edgeToNext.prev = lastCCW;
                lastCCW.next = edgeToNext;
            }

            if (lastCW != null) {
                edgeFromNext.next = lastCW;
                lastCW.prev = edgeFromNext;
            }

            lastCCW = edgeToNext;
            lastCW = edgeFromNext;

        }

        int totalEdges = edges.size();
        edges.get(0).prev = edges.get(totalEdges - 2);
        edges.get(1).next = edges.get(totalEdges - 1);
        edges.get(totalEdges - 2).next = edges.get(0);
        edges.get(totalEdges - 1).prev = edges.get(1);

        wall.outerEdge = firstToSecond;
        for (PointWithEdge pointsWithEdge : pointsWithEdges) {
            pointsWithEdge.incidentEdge.incidentWall = wall;
        }

        return new HalfEdgeDataStructure(wall, pointsWithEdges, edges);
    }

    private static void addSiblingsToCollection(List<HalfEdge> edges, HalfEdge edgeToNext, HalfEdge edgeFromNext) {
        edges.add(edgeToNext);
        edges.add(edgeFromNext);
    }

    private static void joinSiblingsIntoPair(HalfEdge edgeToNext, HalfEdge edgeFromNext) {
        edgeToNext.sibling = edgeFromNext;
        edgeFromNext.sibling = edgeToNext;
    }

    private static PointWithEdge getNextNeighbor(PointWithEdge thisPoint, List<PointWithEdge> pointsWithEdges, Polygon polygon) {
        pointsWithEdges.indexOf(thisPoint);

        Point prevNeighbor = polygon.getNextNeighbor(thisPoint.point);
        Optional<PointWithEdge> first = pointsWithEdges.stream().filter(x -> x.point.equals(prevNeighbor)).findFirst();

        return first.get();
    }


}
