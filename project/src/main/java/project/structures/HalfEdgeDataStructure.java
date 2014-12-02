package project.structures;

import gogui.Line;
import gogui.Point;
import gogui.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class HalfEdgeDataStructure {

    public List<Wall> walls = new ArrayList<>();
    public List<PointWithEdge> points = new ArrayList<>();
    public List<HalfEdge> edges = new ArrayList<>();

    public HalfEdgeDataStructure(Wall wall, List<PointWithEdge> pointsWithEdges, List<HalfEdge> edges) {
        walls.add(wall);
        this.points = pointsWithEdges;
        this.edges = edges;
    }

    public HalfEdgeDataStructure(List<Wall> walls, List<PointWithEdge> pointsWithEdges, List<HalfEdge> edges) {
        this.walls = walls;
        this.points = pointsWithEdges;
        this.edges = edges;
    }

    public static HalfEdgeDataStructure from(Polygon polygon, String name) {
        Wall innerWall = new Wall(null, name);
        Wall outerWall = new Wall(null, Wall.OUTER_WALL_NAME);

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
            edgeToNext.incidentWall = innerWall;

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

        innerWall.outerEdge = firstToSecond;

        List<HalfEdge> collect = pointsWithEdges.stream().map(x -> x.incidentEdge.sibling).filter(x -> x.incidentWall == null).collect(toList());
        collect.stream().forEach(x -> x.incidentWall = outerWall);

        for (HalfEdge edge : edges) {
            if (!edge.next.start.point.equals(edge.sibling.start.point)) {
                throw new IllegalStateException();
            }
            if (!edge.prev.start.point.equals(edge.sibling.next.next.start.point)) {
                throw new IllegalStateException();
            }
            if (edge.incidentWall == null) {
                throw new IllegalStateException();
            }
        }

        HalfEdge start = edges.get(0);
        checkIfHaveSameWall(start);
        checkIfHaveSameWall(start.sibling);

        return new HalfEdgeDataStructure(innerWall, pointsWithEdges, edges);
    }

    private static void checkIfHaveSameWall(HalfEdge start) {
        HalfEdge current = start;
        do {
            if (current.incidentWall != start.incidentWall) {
                throw new IllegalStateException();
            }
            current = current.next;
        } while (start != current);
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

    public HalfEdge findEdge(Line l1, Point intersectionPoint) {

        for (HalfEdge edge : edges) {
            if (matchesLine(edge, l1) || matchesLine(edge, l1, intersectionPoint)) {
                return edge;
            }
        }

        List<HalfEdge> collected = edges.stream().filter(x -> x.start.point.equals(l1.getPoint1()) || x.start.point.equals(l1.getPoint2()) || x.next.start.point.equals(l1.getPoint1()) || x.next.start.point.equals(l1.getPoint2())).collect(toList());
        List<HalfEdge> collected2 = collected.stream().filter(x -> new Line(x.start.point, x.next.start.point).containsPoint(intersectionPoint)).collect(toList());
        return collected2.stream().findAny().orElse(null);
    }

    private boolean matchesLine(HalfEdge edge, Line line, Point intersectionPoint) {
        if (edge.start.point.equals(line.getPoint1()) && edge.next.start.point.equals(intersectionPoint)) {
            return true;
        }
        if (edge.start.point.equals(line.getPoint2()) && edge.next.start.point.equals(intersectionPoint)) {
            return true;
        }
        return false;
    }

    private boolean matchesLine(HalfEdge edge, Line line) {

        if (edge.start.point.equals(line.getPoint1()) && edge.next.start.point.equals(line.getPoint2())) {
            return true;
        }
        if (edge.start.point.equals(line.getPoint2()) && edge.next.start.point.equals(line.getPoint1())) {
            return true;
        }
        return false;
    }

    public static HalfEdgeDataStructure join(HalfEdgeDataStructure structure1, HalfEdgeDataStructure structure2) {

        List<Wall> walls = new ArrayList<>(structure1.walls);
        List<HalfEdge> halfEdges = new ArrayList<>(structure1.edges);
        List<PointWithEdge> points = new ArrayList<>(structure1.points);
        walls.addAll(structure2.walls);
        halfEdges.addAll(structure2.edges);
        points.addAll(structure2.points);
        return new HalfEdgeDataStructure(walls, points, halfEdges);
    }

    public HalfEdge findStartingFromIntersectionPoint(Point prevEdgeStartPoint, Point prevEdgeEndPoint) {

        List<HalfEdge> startingAt = edges.stream().filter(edge -> edge.start.point.equals(prevEdgeStartPoint)).collect(toList());
        for (HalfEdge edge : startingAt) {
            if (edge.next.start.point.equals(prevEdgeEndPoint)) {
                return edge;
            }

        }
        return null;
    }

    public void addAll(List<HalfEdge> newHalfEdges) {
        edges.addAll(newHalfEdges);
    }
}
