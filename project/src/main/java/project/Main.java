package project;

import gogui.*;
import javafx.util.Pair;
import project.structures.HalfEdge;
import project.structures.HalfEdgeDataStructure;
import project.structures.PointWithEdge;
import project.structures.Wall;
import project.structures.graph.cycle.CycleCreator;
import project.structures.graph.cycle.CycleGoGuiDrawer;
import project.structures.graph.cycle.EdgesCycle;
import project.structures.sweep.LinePair;
import project.structures.sweep.Q;
import project.structures.sweep.T;

import java.util.*;

import static gogui.GoGui.saveJSON;
import static gogui.GoGui.snapshot;
import static java.util.stream.Collectors.toList;

public class Main {


    private static final String INPUT_FILE_EXTENSION = ".txt";
    private static final String LAB4_SRC_MAIN_RESOURCES = "project\\src\\main\\resources\\";

    public static void main(String[] args) {

        String fileName = "map1";

//        go(fileName, "map2");
//        go(fileName, "map3");
//        go(fileName, "map4");
//        go(fileName, "map5");
//        go(fileName, "map6");
//        go(fileName, "map7");
        go(fileName, "map8");
    }

    private static void go(String fileName, String fileName2) {
        GeoList<Point> polygonPoints = GoGui.loadPoints_ZMUDA(LAB4_SRC_MAIN_RESOURCES + fileName + INPUT_FILE_EXTENSION);
        Polygon polygon = new Polygon(polygonPoints);
        HalfEdgeDataStructure halfEdgeDataStructure = HalfEdgeDataStructure.from(polygon, "A");

        GeoList<Point> polygonPoints2 = GoGui.loadPoints_ZMUDA(LAB4_SRC_MAIN_RESOURCES + fileName2 + INPUT_FILE_EXTENSION);
        Polygon polygon2 = new Polygon(polygonPoints2);
        HalfEdgeDataStructure halfEdgeDataStructure2 = HalfEdgeDataStructure.from(polygon2, "B");


//        withIntersections(halfEdgeDataStructure, halfEdgeDataStructure2);
        fireAlgorithm(halfEdgeDataStructure, halfEdgeDataStructure2);

        snapshot();
        saveJSON("project\\src\\main\\resources\\project." + fileName2 + ".data.json");
        saveJSON("results\\project." + fileName2 + ".data.json");

        GoGui.clear();
    }

    private static Set<Point> fireAlgorithm(HalfEdgeDataStructure structure1, HalfEdgeDataStructure structure2) {
        HalfEdgeDataStructure joinedStructure = HalfEdgeDataStructure.join(structure1, structure2);

        List<Line> lines1 = structure1.edges.stream().map(x -> new Line(x.start.point, x.sibling.start.point)).collect(toList());
        List<Line> lines2 = structure2.edges.stream().map(x -> new Line(x.start.point, x.sibling.start.point)).collect(toList());

        Set<Point> intersectionPoints = new HashSet<>();

        T t = new T();

        for (Line l1 : lines1) {
            for (Line l2 : lines2) {
                if (!l1.isParallel(l2)) {
                    Point intersectionPoint = l1.intersectionPoint(l2);
                    if (l1.containsPoint(intersectionPoint) && l2.containsPoint(intersectionPoint)) {
                        intersectionPoints.add(intersectionPoint);
                        t.addIntersectionLines(intersectionPoint, l1, l2);
                    }
                }
            }
        }

        GeoList<Point> intersectionPointsGeoList = new GeoList<>();
        intersectionPointsGeoList.addAll(intersectionPoints);
        snapshot();

        for (Point intersectionPoint : intersectionPoints) {
            LinePair intersectionLines = t.getIntersectionLines(intersectionPoint);
            HalfEdge e2 = joinedStructure.findEdge(intersectionLines.l1, intersectionPoint);
            HalfEdge e1 = e2.sibling;
            HalfEdge f2 = joinedStructure.findEdge(intersectionLines.l2, intersectionPoint);
            HalfEdge f1 = f2.sibling;

            // halfEdge1

            ArrayList<HalfEdge> newHalfEdges = new ArrayList<>();

            HalfEdge e11 = new HalfEdge(new PointWithEdge(intersectionPoint, null));
            HalfEdge e12 = new HalfEdge(new PointWithEdge(intersectionPoint, null));
            HalfEdge f11 = new HalfEdge(new PointWithEdge(intersectionPoint, null));
            HalfEdge f12 = new HalfEdge(new PointWithEdge(intersectionPoint, null));
            newHalfEdges.add(e11);
            newHalfEdges.add(e12);
            newHalfEdges.add(f11);
            newHalfEdges.add(f12);
            //2

            joinedStructure.addAll(newHalfEdges);

            List<Pair<HalfEdge, Wall>> a = new ArrayList<>();
            a.add(new Pair<>(e1, e1.sibling.incidentWall));
            a.add(new Pair<>(e2, e2.sibling.incidentWall));
            a.add(new Pair<>(f1, f1.sibling.incidentWall));
            a.add(new Pair<>(f2, f2.sibling.incidentWall));

            e11.makeSibling(e1, a.stream().filter(x -> x.getKey().equals(e1)).findAny().get().getValue());
            e12.makeSibling(e2, a.stream().filter(x -> x.getKey().equals(e2)).findAny().get().getValue());

            structure1.addAll(Arrays.asList(e11, e12));

            f11.makeSibling(f1, a.stream().filter(x -> x.getKey().equals(f1)).findAny().get().getValue());
            f12.makeSibling(f2, a.stream().filter(x -> x.getKey().equals(f2)).findAny().get().getValue());

            structure2.addAll(Arrays.asList(f11, f12));
            //3
            fixFarFromIntersectionPoints(e2, e11);
            fixFarFromIntersectionPoints(e1, e12);
            fixFarFromIntersectionPoints(f2, f11);
            fixFarFromIntersectionPoints(f1, f12);

            ArrayList<Point> CWCoordinatesSortedList = new ArrayList<>();
            CWCoordinatesSortedList.add(e1.start.point);
            CWCoordinatesSortedList.add(e2.start.point);
            CWCoordinatesSortedList.add(f1.start.point);
            CWCoordinatesSortedList.add(f2.start.point);

            sortPointByPolarCoordinates(intersectionPoint, CWCoordinatesSortedList);

            List<Point> CCWCoordinatesSortedList = getReversed(CWCoordinatesSortedList);

            // 4
            List<HalfEdge> edgesToFix = new ArrayList<>(Arrays.asList(e1, e2, f1, f2));
//            edgesToFix.addAll(newHalfEdges);

            for (HalfEdge edgeToFix : edgesToFix) {
                Point edgeToFixStartPoint = edgeToFix.start.point;
                Point edgeToFixEndPoint = edgeToFix.sibling.start.point;
                // ma v jako koniec - (1)
                if (edgeToFixStartPoint.equals(intersectionPoint)) {

                    joinedStructure.addIncidentalEdge(intersectionPoint, edgeToFix);

                    Point prevEdgeStartPoint = findNext(edgeToFix.next.start.point, CWCoordinatesSortedList);
                    HalfEdge prevEdge = joinedStructure.findStartingFromIntersectionPoint(prevEdgeStartPoint, edgeToFixStartPoint);

                    prevEdge.next = edgeToFix;
                    edgeToFix.prev = prevEdge;

                    System.out.println();
                } else if (edgeToFixEndPoint.equals(intersectionPoint)) { // ma v jako początek - (2)
                    Point prevEdgeStartPoint = findNext(edgeToFix.next.start.point, CCWCoordinatesSortedList);
                    HalfEdge nextEdge = joinedStructure.findStartingFromIntersectionPoint(edgeToFix.sibling.start.point, prevEdgeStartPoint);
                    if (!nextEdge.start.point.equals(intersectionPoint)) {
                        nextEdge = nextEdge.sibling;
                    }

                    nextEdge.prev = edgeToFix;
                    edgeToFix.next = nextEdge;
                    System.out.println();
                } else {
                    throw new IllegalStateException();
                }

            }
            System.out.println();
        }

        // Create cycles:
        List<EdgesCycle> cycles = CycleCreator.createCycles(joinedStructure.edges);

        System.out.println("Number of intersections: " + intersectionPoints.size());
        intersectionPoints.forEach(System.out::println);

        GeoList<Line> linesOfItersection = new GeoList<>();

        for (EdgesCycle cycle : cycles) {
            CycleGoGuiDrawer.draw(cycle);
            if (cycle.isIntersection()) {
                linesOfItersection.addAll(cycle.getLines());
            }
        }
        linesOfItersection.setColor("red");
        snapshot();

        return new HashSet<>(intersectionPoints);
    }

    private static void withIntersections(HalfEdgeDataStructure structure1, HalfEdgeDataStructure structure2) {

        List<Line> lines1 = structure1.edges.stream().map(x -> new Line(x.start.point, x.sibling.start.point)).collect(toList());
        List<Line> lines2 = structure2.edges.stream().map(x -> new Line(x.start.point, x.sibling.start.point)).collect(toList());

        Set<Point> intersectionPoints = new HashSet<>();

        T t = new T();

        for (Line l1 : lines1) {
            for (Line l2 : lines2) {
                if (!l1.isParallel(l2)) {
                    Point intersectionPoint = l1.intersectionPoint(l2);
                    if (l1.containsPoint(intersectionPoint) && l2.containsPoint(intersectionPoint)) {
                        intersectionPoints.add(intersectionPoint);
                        t.addIntersectionLines(intersectionPoint, l1, l2);
                    }
                }
            }
        }

        GeoList<Point> points = new GeoList<>();
        points.addAll(intersectionPoints);
        snapshot();
    }

    private static Point findNext(Point edgeStartPoint, List<Point> list) {
        int indexOf = list.indexOf(edgeStartPoint);
        int nextIndex = (indexOf + 1) % list.size();

        return list.get(nextIndex);
    }

    private static List<Point> getReversed(ArrayList<Point> polarCoordinatesSortedList) {
        ArrayList<Point> copy = new ArrayList<>(polarCoordinatesSortedList);
        Collections.reverse(copy);
        return copy;
    }


    private static void fixFarFromIntersectionPoints(HalfEdge original, HalfEdge startingAtIntersection) {
        startingAtIntersection.next = original.next;
        startingAtIntersection.next.prev = startingAtIntersection;
    }

    private static void sortPointByPolarCoordinates(Point intersectionPoint, List<Point> polarCoordinatesSortedList) {
        Line baseLine = new Line(new Point(intersectionPoint.x - 1000, intersectionPoint.y), new Point(intersectionPoint.x + 1000, intersectionPoint.y));

        Collections.sort(polarCoordinatesSortedList, (p1, p2) -> {
            Line newLine = new Line(intersectionPoint, p1);
            Line newLine2 = new Line(intersectionPoint, p2);
            if (baseLine.angleBetweenLines2(newLine) < baseLine.angleBetweenLines2(newLine2)) {
                return -1;
            } else if (baseLine.angleBetweenLines2(newLine) > baseLine.angleBetweenLines2(newLine2)) {
                return 1;
            }
            return 0;
        });
    }

    private static void processNeighboringLine(Q q, Line currentLine, Line line, T t) {
        line.activate();
        findIntersection(currentLine, line, q, t);
    }

    private static void findIntersection(Line l1, Line l2, Q q, T t) {
        Point intersection = l1.intersectionPoint(l2);

        if (l1.containsPoint(intersection) && l2.containsPoint(intersection)) {
//            System.out.println("Lines : " + l1 + " and " + l2 + " intersects at: " + intersection);
            if (!q.isIntersectionPoint(intersection)) {
                q.addIntersectionPoint(intersection);
                t.addIntersectionLines(intersection, l1, l2);
            }
        }
    }
}
