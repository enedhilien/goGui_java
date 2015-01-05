package lab4_skibinski;

import com.google.common.collect.Lists;
import gogui.*;
import main.helpers.Side;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

import static gogui.GoGui.*;
import static java.util.stream.Collectors.toList;

/**
 * Created by testuser on 1/4/15.
 */
public class TriangulationUtils {

    public static void main(String[] args){
        fireSimplerAlgo("input1.zmuda");
        GoGui.clear();
        fireSimplerAlgo("input4.zmuda");
        GoGui.clear();
    }


    public static void fireSimplerAlgo(String fileName){

        GeoList<Point> points = GoGui.loadPoints_ZMUDA(Paths.get(ClassificationUtils.LAB4_SRC_MAIN_RESOURCES, fileName).toString());
        Polygon polygon = new Polygon(points);

        snapshot();

        Stack<Point> stack = new Stack();
        TreeMap<Point, SIDE> sideMap = getPointsWithSidesMap(polygon);
        List<Point> zdarzenia = new ArrayList<Point>(sideMap.keySet());
        registerContainer(new GeoList<Point>(zdarzenia));
        zdarzenia.sort(Point.Y_ORDER_COMPARATOR);
        zdarzenia = Lists.reverse(zdarzenia);

        stack.push(zdarzenia.get(0));
        stack.push(zdarzenia.get(1));
        zdarzenia.get(0).setStatus(GeoObject.Status.ACTIVE);
        zdarzenia.get(1).setStatus(GeoObject.Status.ACTIVE);
        snapshot();

        GeoList<Line> diags = new GeoList<>();
        for(int i=2; i<zdarzenia.size();i++){
            System.out.println(i);
            Point nextPoint = zdarzenia.get(i);

            nextPoint.setColor("Aqua");
            snapshot();

            Point first = stack.peek();
            if(!sideMap.get(nextPoint).equals(sideMap.get(first))){
                while(!stack.isEmpty()){
                    List<Line> collect = stack.stream().map(x -> new Line(nextPoint, x)).collect(toList());
                    for(Line l: collect){
                        l.setColor("Orange");
                    }
                    diags.addAll(collect);
                    stack.clear();
                    snapshot();
                }
            }else{
                Point pop = stack.pop();
                pop.setStatus(GeoObject.Status.PROCESSED);
                Point second = stack.peek();
                while(isInPolygon(nextPoint, first, second)){
                    Line e = new Line(nextPoint, second);
                    e.setColor("Violet");
                    diags.add(e);
                    first = second;
                    snapshot();
                    Point pop1 = stack.pop();
                    pop1.setStatus(GeoObject.Status.PROCESSED);
                    if(stack.isEmpty()){
                       first = second;
                       break;
                   }else{
                       second = stack.peek();
                   }
                }

            }
            first.setStatus(GeoObject.Status.ACTIVE);
            nextPoint.setStatus(GeoObject.Status.ACTIVE);
            stack.push(first);
            stack.push(nextPoint);
            Point peek = stack.peek(); peek.setColor("Blue");
            snapshot();
        }


        snapshot();
        saveJSON(Paths.get(ClassificationUtils.LAB4_SRC_MAIN_RESOURCES, fileName + ".triang_result").toString());
    }

    private  static TreeMap<Point, SIDE> map;
    private static TreeMap<Point, SIDE> getPointsWithSidesMap(Polygon polygon) {
        map = new TreeMap<>();
        GeoList<Point> points = (GeoList<Point>) polygon.getPoints().clone();
        points.sort(Point.Y_ORDER_COMPARATOR);
        Point topY = points.get(points.size() - 1);
        Point bottomY = points.get(0);
        Point walker = bottomY;
       do{
            Point nextNeighbor = polygon.getNextNeighbor(walker);
            map.put(nextNeighbor, SIDE.RIGHT);
            walker = nextNeighbor;
        } while(!walker.equals(topY));
        walker = bottomY;
        do{
            Point prevNeighbor = polygon.getPrevNeighbor(walker);
            map.put(prevNeighbor, SIDE.LEFT);
            walker = prevNeighbor;
        }while(!walker.equals(topY));
        return map;



    }

    private static boolean isInPolygon(Point p, Point q, Point r) {
        double det = (p.x - r.x) * (q.y - r.y) - (p.y - r.y) * (q.x - r.x);

        if (det > 0 && Side.LEFT.equals(map.get(p))) {
            return false;
        }
        if (det < 0 && Side.RIGHT.equals(map.get(p))) {
            return false;
        }
        return true;
    }


    private static double det(Point x, Point y, Point z) {
        return (x.x - z.x) * (y.y - z.y) - (x.y - z.y) * (y.x - z.x);
    }

    public static SIDE getSide(Point top, Point p){
        if(p.x > top.x){
            return SIDE.RIGHT;
        }else{
            return SIDE.LEFT;
        }
    }
    public static enum SIDE{
        RIGHT, LEFT;
    }
}

