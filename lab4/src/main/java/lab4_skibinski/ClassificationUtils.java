package lab4_skibinski;

import gogui.*;

import java.nio.file.Paths;

import static gogui.GoGui.saveJSON;
import static gogui.GoGui.snapshot;

/**
 * Created by testuser on 1/4/15.
 */
public class ClassificationUtils {

    public static final String LAB4_SRC_MAIN_RESOURCES = Paths.get("lab4", "src", "main", "resources").toString();

    public static void main(String[] args){
        fireAlgo("input1.zmuda");
        GoGui.clear();
        fireAlgo("input2.zmuda");
        GoGui.clear();
        fireAlgo("input3.zmuda");
        GoGui.clear();
        fireAlgo("input4.zmuda");
        GoGui.clear();
    }

    public static Polygon fireAlgo(String fileName){
        System.out.println(fileName);

        GeoList<Point> points = GoGui.loadPoints_ZMUDA(Paths.get(LAB4_SRC_MAIN_RESOURCES, fileName).toString());
        Polygon polygon = new Polygon(points);
        snapshot();

        System.out.println("Is Y monotonic: " + isPolygonYMonotonic(polygon) );
        classifyPoints(polygon);
        saveJSON(Paths.get(LAB4_SRC_MAIN_RESOURCES, fileName + ".classification_result").toString());
        return polygon;
    }

    private static boolean isPolygonYMonotonic(Polygon polygon) {
        Point[] points = new Point[polygon.getPoints().size()];
        points = polygon.getPoints().toArray(points);
        int local_mins = 0;
        int n = points.length;
        for(int i = 0; i<n; i++){
            int prev_index = (i-1)%n < 0 ? ((i-1)%n)+n : (i-1)%n;
            if (points[i].y < points[(i+1)%n].y && points[i].y < points[prev_index].y){
                local_mins++;
            }
        }
        return local_mins == 1;
    }

    private static Polygon classifyPoints(Polygon polygon){
        Point next, prev;
        for(Point p: polygon.getPoints()){
            next = polygon.getNextNeighbor(p);
            prev = polygon.getPrevNeighbor(p);

            GeoList<Line> activeLines = new GeoList<>();
            Line l1 = new Line(p, prev);
            Line l2 = new Line(p,next);
            activeLines.add(l1);
            activeLines.add(l2);
            activeLines.setStatus(GeoObject.Status.ACTIVE);
            snapshot();

            if (Point.isBelow(next, p) && Point.isBelow(prev, p)){
                if(p.getAngleBetweenPoints(prev,next) > 0){
                    p.setClassification(Point.Classification.SPLIT);
                }else{
                    p.setClassification(Point.Classification.START);
                }
            }else if(Point.isAbove(next,p) && Point.isAbove(prev, p)){
                if(p.getAngleBetweenPoints(prev, next) >0){
                    p.setClassification(Point.Classification.MERGE);
                }else{
                    p.setClassification(Point.Classification.END);
                }
            }else{
                p.setClassification(Point.Classification.REGULAR);
            }
            activeLines.setStatus(GeoObject.Status.NORMAL);
            snapshot();
        }
        return polygon;
    }

    private static boolean isLess(Point[] X, int i, int j){
        return ((X[i].y < X[j].y) || ((X[i].y == X[j].y) && (i < j)));
    }
}
