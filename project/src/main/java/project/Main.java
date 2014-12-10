package project;

import gogui.*;
import javafx.util.Pair;
import project.structures.HalfEdge;
import project.structures.HalfEdgeDataStructure;
import project.structures.PointWithEdge;
import project.structures.Wall;
import project.structures.cycle.CycleCreator;
import project.structures.cycle.CycleGoGuiDrawer;
import project.structures.cycle.EdgesCycle;
import project.structures.sweep.LinePair;
import project.structures.sweep.T;

import java.nio.file.Paths;
import java.util.*;

import static gogui.GoGui.saveJSON;
import static gogui.GoGui.snapshot;
import static java.util.stream.Collectors.toList;

public class Main {


    private static final String INPUT_FILE_EXTENSION = ".txt";
    private static final String LAB4_SRC_MAIN_RESOURCES = Paths.get("project", "src", "main", "resources").toString();

    public static void main(String[] args) {
        String fileName = "map1";

        fireAlgorithmWithFiles(fileName, "map2");
//        fireAlgorithmWithFiles(fileName, "map3");
//        fireAlgorithmWithFiles(fileName, "map4");
//        fireAlgorithmWithFiles(fileName, "map5");
//        fireAlgorithmWithFiles(fileName, "map6");
//        fireAlgorithmWithFiles(fileName, "map7");
//        fireAlgorithmWithFiles(fileName, "map8");
    }

    private static void fireAlgorithmWithFiles(String fileName, String fileName2) {
        GeoList<Point> polygonPoints = GoGui.loadPoints_ZMUDA(Paths.get(LAB4_SRC_MAIN_RESOURCES,fileName + INPUT_FILE_EXTENSION).toString());
        Polygon polygon = new Polygon(polygonPoints);
        HalfEdgeDataStructure halfEdgeDataStructure = HalfEdgeDataStructure.from(polygon, "A");

        GeoList<Point> polygonPoints2 = GoGui.loadPoints_ZMUDA(Paths.get(LAB4_SRC_MAIN_RESOURCES, fileName2 + INPUT_FILE_EXTENSION).toString());
        Polygon polygon2 = new Polygon(polygonPoints2);
        HalfEdgeDataStructure halfEdgeDataStructure2 = HalfEdgeDataStructure.from(polygon2, "B");

//        withIntersections(halfEdgeDataStructure, halfEdgeDataStructure2);
        fireAlgorithm(halfEdgeDataStructure, halfEdgeDataStructure2);

        saveJSON(Paths.get("project","src", "main", "resources","project." + fileName2 + ".data.json").toString());
//        saveJSON(Paths.get("results","project." + fileName2 + ".data.json").toString());
        GoGui.clear();
    }

    private static Set<Point> fireAlgorithm(HalfEdgeDataStructure structure1, HalfEdgeDataStructure structure2) {
        HalfEdgeDataStructure joinedStructure = HalfEdgeDataStructure.join(structure1, structure2);

        List<Line> lines1 = structure1.edges.stream().map(x -> new Line(x.start.point, x.sibling.start.point)).collect(toList());
        List<Line> lines2 = structure2.edges.stream().map(x -> new Line(x.start.point, x.sibling.start.point)).collect(toList());

        Set<Point> intersectionPoints = new HashSet<>();
        T t = new T();

        findIntersections(lines1, lines2, intersectionPoints, t);

        for (Point intersectionPoint : intersectionPoints) {
            LinePair intersectionLines = t.getIntersectionLines(intersectionPoint);
            HalfEdge e2 = joinedStructure.findEdge(intersectionLines.l1, intersectionPoint);
            HalfEdge e1 = e2.sibling;
            HalfEdge f2 = joinedStructure.findEdge(intersectionLines.l2, intersectionPoint);
            HalfEdge f1 = f2.sibling;

            // Create new halfEdges with starting intersectionPoint as their starting point
            ArrayList<HalfEdge> newHalfEdges = new ArrayList<>();

            HalfEdge e11 = new HalfEdge(new PointWithEdge(intersectionPoint, null));
            HalfEdge e12 = new HalfEdge(new PointWithEdge(intersectionPoint, null));
            HalfEdge f11 = new HalfEdge(new PointWithEdge(intersectionPoint, null));
            HalfEdge f12 = new HalfEdge(new PointWithEdge(intersectionPoint, null));
            newHalfEdges.add(e11);
            newHalfEdges.add(e12);
            newHalfEdges.add(f11);
            newHalfEdges.add(f12);

            //Save new edges and join them with their applicable siblings existing previously
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

            //fix halfEdges connections on end of lines that intersects
            fixFarFromIntersectionPoints(e2, e11);
            fixFarFromIntersectionPoints(e1, e12);
            fixFarFromIntersectionPoints(f2, f11);
            fixFarFromIntersectionPoints(f1, f12);

            ArrayList<Point> CWCoordinatesSortedList = getClockWisePointsAroundIntersectionPoint(intersectionPoint, e2, e1, f2, f1);

            List<Point> CCWCoordinatesSortedList = getCCWPointsList(CWCoordinatesSortedList);

            // fix intersection lines halfEdges
            List<HalfEdge> edgesToFix = new ArrayList<>(Arrays.asList(e1, e2, f1, f2));

            for (HalfEdge edgeToFix : edgesToFix) {
                Point edgeToFixEndPoint = edgeToFix.sibling.start.point;
                if (edgeToFixEndPoint.equals(intersectionPoint)) { // should have end point in intersection point
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
        }

        List<EdgesCycle> cycles = CycleCreator.createCycles(joinedStructure.edges);

        GeoList<Line> foundCyclesLines = new GeoList<>();

        for (EdgesCycle cycle : cycles) {
            CycleGoGuiDrawer.draw(cycle);
            if (cycle.isIntersection()) {
                foundCyclesLines.addAll(cycle.getLines());
            }
        }
        foundCyclesLines.setColor("red");
        snapshot();

        List<EdgesCycle> notOuterCycles = cycles.stream().filter(x -> !x.getEdges().get(0).incidentWall.name.equals(Wall.OUTER_WALL_NAME)).collect(toList());
        if (notOuterCycles.size() == 2 && intersectionPoints.size() == 0) {
            EdgesCycle firstCycle = notOuterCycles.get(0);
            EdgesCycle secondCycle = notOuterCycles.get(1);
            if (firstCycle.contains(secondCycle)) {
                CycleGoGuiDrawer.draw(secondCycle);
            } else if (secondCycle.contains(firstCycle)) {
                CycleGoGuiDrawer.draw(firstCycle);
            }
        }

        return new HashSet<>(intersectionPoints);
    }

    private static ArrayList<Point> getClockWisePointsAroundIntersectionPoint(Point intersectionPoint, HalfEdge e2, HalfEdge e1, HalfEdge f2, HalfEdge f1) {
        ArrayList<Point> CWCoordinatesSortedList = new ArrayList<>();
        CWCoordinatesSortedList.add(e1.start.point);
        CWCoordinatesSortedList.add(e2.start.point);
        CWCoordinatesSortedList.add(f1.start.point);
        CWCoordinatesSortedList.add(f2.start.point);

        sortPointByPolarCoordinates(intersectionPoint, CWCoordinatesSortedList);
        return CWCoordinatesSortedList;
    }

    private static void findIntersections(List<Line> lines1, List<Line> lines2, Set<Point> intersectionPoints, T t) {
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

        System.out.println("Number of intersections: " + intersectionPoints.size());
        intersectionPoints.forEach(System.out::println);

        GeoList<Point> intersectionPointsGeoList = new GeoList<>();
        intersectionPointsGeoList.addAll(intersectionPoints);
        snapshot();
    }

    private static void withIntersections(HalfEdgeDataStructure structure1, HalfEdgeDataStructure structure2) {

        List<Line> lines1 = structure1.edges.stream().map(x -> new Line(x.start.point, x.sibling.start.point)).collect(toList());
        List<Line> lines2 = structure2.edges.stream().map(x -> new Line(x.start.point, x.sibling.start.point)).collect(toList());

        Set<Point> intersectionPoints = new HashSet<>();

        T t = new T();

        findIntersections(lines1, lines2, intersectionPoints, t);

        GeoList<Point> points = new GeoList<>();
        points.addAll(intersectionPoints);
        snapshot();
    }

    private static Point findNext(Point edgeStartPoint, List<Point> list) {
        int indexOf = list.indexOf(edgeStartPoint);
        int nextIndex = (indexOf + 1) % list.size();

        return list.get(nextIndex);
    }

    private static List<Point> getCCWPointsList(ArrayList<Point> polarCoordinatesSortedList) {
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

}
