package gogui.history;

import gogui.GeoList;
import gogui.Line;
import gogui.Point;

import java.util.ArrayList;
import java.util.List;

import static gogui.GoGui.getActiveLists;
import static java.util.stream.Collectors.toList;

public class History {

    private static List<State> states = new ArrayList<>();

    public void clear() {
        states.clear();
    }

    public void putState() {
        List<Point> pointsList = (List<Point>) getActiveLists().stream()
                .filter(x -> !x.isEmpty())
                .filter(x -> x.get(0).getClass().isAssignableFrom(Point.class))
                        .flatMap(geoList -> geoList.stream())
                        .collect(toList());

        List<Line> linesList = (List<Line>) getActiveLists().stream()
                .filter(x -> !x.isEmpty())
                .filter(x -> x.get(0).getClass().isAssignableFrom(Line.class))
                        .flatMap(geoList -> geoList.stream())
                        .collect(toList());



        List<Point> newPointsList = pointsList.stream().map(Point::from).collect(toList());
        List<Line> newLinesList = linesList.stream().map(Line::from).collect(toList());

        states.add(states.size(), new State(new GeoList<>(newPointsList, false), new GeoList<>(newLinesList, false)));
    }

    public static List<State> getStates() {
        return states;
    }

    public void snapshot() {
        putState();
    }

}
