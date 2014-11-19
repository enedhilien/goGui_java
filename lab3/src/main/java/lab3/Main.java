package lab3;

import gogui.*;

import java.util.*;

public class Main {



//    public static void main(String[] args) {
//        fireAlgorithm("input2");
//
//    }
//
//    private static void fireAlgorithm(String fileName) {
//        GeoList<Line> lines = GoGui.loadLinesFromJson(LAB3_SRC_MAIN_RESOURCES + fileName+ INPUT_FILE_EXTENSION);
//        GeoList<Point> points = new GeoList<>();
//        GeoList<Line> helper = new GeoList<>();
//        GeoList<Point> intersectionPoints = new GeoList<>();
//
//
//        Map<Point, Line> pointToLine = new HashMap<>();
//
//        for (Line line : lines) {
//            points.push_back(line.getPoint1());
//            points.push_back(line.getPoint2());
//            pointToLine.put(line.getPoint1(), line);
//            pointToLine.put(line.getPoint2(), line);
//        }
//
//        Collections.sort(points);
//
//        GeoList<Line> T = new GeoList<>(false);
//        GoGui.snapshot();
//
//        Line broomstick = null;
//        for (Point p : points) {
//
//            double x = p.x;
//            if (broomstick == null) {
//                broomstick = new Line(new Point(x, 0.0), new Point(x, 1000.0));
//            } else {
//                helper.remove(broomstick);
//                broomstick = new Line(new Point(x, 0.0), new Point(x, 1000.0));
//            }
//
//            helper.push_back(broomstick);
//
//            pointToLine.get(p).activate();
//            GoGui.snapshot();
//            Line s = pointToLine.get(p);
//
//            Point other;
//            if (p == s.getPoint1()) {
//                other = s.getPoint2();
//            } else {
//                other = s.getPoint1();
//            }
//
//            Comparator<Line> lineComparator = (l1, l2) -> {
//                if (l1.getY(x) > l2.getY(x)) {
//                    return 1;
//                } else if (l1.getY(x) < l2.getY(x)) {
//                    return -1;
//                }
//                return 0;
//            };
//            Collections.sort(T, lineComparator);
//            int sIndex = T.indexOf(s);
//
//            // left end of segment
//            if (p.x < other.x) {
//                T.push_back(s);
////                assert (it != T.end()); // this must be inside our container
//                if (sIndex < T.size() - 1) {
//                    Line line = T.get(sIndex + 1);
//                    line.activate();
//
//                    GoGui.snapshot();
//
//                    // if segments cross(s, sn)
//                    Point intersection = s.intersectionPoint2(line);
//
//                    if (intersection != null) {
//                        System.out.println("Lines : " + s + " and " + line + " intersects at: " + intersection);
//                        intersectionPoints.push_back(intersection);
//                    } else {
//                        System.out.println("Intersection is null : ");
//                    }
//                }
//
//                if (sIndex > 0) {
//                    Line line = T.get(sIndex - 1);
//                    line.activate();
//
//                    GoGui.snapshot();
//
//                    // if segments cross(s, sn)
//                    Point intersection = s.intersectionPoint(line);
//
//                    if (line.containsPoint(intersection) && s.containsPoint(intersection)) {
//                        System.out.println("Lines : " + s + " and " + line + " intersects at: " + intersection);
//                        intersectionPoints.push_back(intersection);
//                    } else {
//                        System.out.println("Intersection is null : ");
//                    }
//                }
//            } else { // right end of segment
//                if (sIndex > 0 && sIndex < T.size()-1) {
//                    Line line = T.get(sIndex - 1);
//                    Line line2 = T.get(sIndex + 1);
//
//                    line.activate();
//
//                    GoGui.snapshot();
//
//                    // if segments cross(s, sn)
//                    Point intersection = line.intersectionPoint( line2);
//
//                    if (line.containsPoint(intersection) && line2.containsPoint(intersection)){
//                        System.out.println("Lines : " + s + " and " + line + " intersects at: " + intersection);
//                        intersectionPoints.push_back(intersection);
//                    }
//
//                    T.remove(sIndex);
//                    Collections.sort(T, lineComparator);
//                }
//            }
//
//            s.processed();
//            GoGui.snapshot();
//            helper.clear();
//        }
//
//        GoGui.saveJSON("C:\\home\\aaaaStudia\\Semestr_VII\\Geometria\\gogui\\visualization-grunt\\public\\data\\sweep."+fileName+".data.json");
//    }


}
