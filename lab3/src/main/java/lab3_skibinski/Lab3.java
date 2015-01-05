package lab3_skibinski;

import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeMultiset;
import gogui.*;

import java.nio.file.Paths;
import java.util.NavigableSet;
import java.util.stream.Collectors;

import static gogui.GoGui.*;

/**
 * Created by testuser on 12/15/14.
 */
public class Lab3 {

    public static final String LAB3_SRC_MAIN_RESOURCES = Paths.get("lab3", "src", "main", "resources").toString();

    public static void main(String[] args){
        fireAlgo("input.json");
        GoGui.clear();
        fireAlgo("input2.json");
        GoGui.clear();
        fireAlgo("input3.json");
        GoGui.clear();
        fireAlgo("input4.json");
        GoGui.clear();
    }

    public static void fireAlgo(String fileName){
        GeoList<Line> lines = loadLinesFromJson(Paths.get(LAB3_SRC_MAIN_RESOURCES,fileName).toString());
        GeoList<Point> points = new GeoList<>();
        GeoList<Line> helper = new GeoList<>(); //broomsticks

        TreeMultimap<SmartPoint, Line> Q = TreeMultimap.create(Point.X_ORDER_COMPARATOR, Line.LEFT_END_X_COMPARATOR);
        TreeMultiset<Line> T = TreeMultiset.create(Line.LEFT_END_Y_COMPARATOR);
        for(Line l : lines){
            points.push_back(l.getPoint1());
            points.push_back(l.getPoint2());
            Q.put(SmartPoint.from(l.getLeftPoint(), SmartPoint.STATE.LEFT), l);
            Q.put(SmartPoint.from(l.getRightPoint(), SmartPoint.STATE.RIGHT), l);
        }
        snapshot();

        GeoList<Point> intersections = new GeoList<>();

        while(!Q.isEmpty()){
            SmartPoint first = Q.keySet().first();
            helper.add(LineUtil.getNextBroomstick(helper, first.x, 500));
            snapshot();

            switch(first.state){
                case LEFT:
                    for(Line l : Q.get(first)){
                        l.setStatus(GeoObject.Status.ACTIVE);

                    }
                    NavigableSet<Line> set = Q.get(first);
                    T.addAll(set.stream().collect(Collectors.toList()));
                    break;
                case RIGHT:
                    for(Line l : Q.get(first)){
                        l.setStatus(GeoObject.Status.PROCESSED);

                    }
                    T.removeAll(Q.get(first).stream().collect(Collectors.toList()));
                    break;
                case INTERSECTION:
                    break;
            }
            for(Line l1: T){
                for(Line l2: T){
                    if(!l1.equals(l2)){
                        Point point = l1.intersectionPoint(l2);
                        if(l1.containsPoint(point) && l2.containsPoint(point) && !intersections.contains(point)){
                            Q.put(SmartPoint.from(point, SmartPoint.STATE.INTERSECTION), l1);
                            point.activate();
                            intersections.add(point);
                            snapshot();
                        }
                    }
                }
            }
            Q.removeAll(first);
            helper.clear();
        }
        snapshot();
        System.out.println(intersections.size());
        saveJSON(Paths.get(LAB3_SRC_MAIN_RESOURCES, fileName+"_result.json").toString());

    }

}
