package gogui;

import gogui.history.History;
import gogui.history.State;

import javax.json.*;
import java.util.*;
import java.util.stream.Collectors;

import static gogui.GeoObject.*;

public class JsonPrinter {

    private TreeSet<Point> points = new TreeSet<>();
    private TreeSet<InternalLine> lines = new TreeSet<>();
    private List<State> states;

    public JsonPrinter(List<State> states) {
        this.states = states;
    }

    public String getJSON() {
        getPoints();
        getLines();

        JsonObjectBuilder obj = Json.createObjectBuilder();
        obj.add("history", getJSONAllStates());
        obj.add("lines", getJSONLinesDefinition());
        obj.add("points", getJSONPointsDefinition());

        return obj.build().toString();
    }


    private void getPoints() {
        points.clear();

        for (State state : states) {
            points.addAll(state.getPoints().stream().collect(Collectors.toList()));

            for (Line line : state.getLines()) {
                Point p1 = line.point1;
                Point p2 = line.point2;
                points.add(p1);
                points.add(p2);
            }
        }
    }

    void getLines() {
        lines.clear();

        for (State state : states) {
            for (Line line : state.getLines()) {
                Point p1 = line.point1;
                Point p2 = line.point2;

                InternalLine iline = new InternalLine();
                iline.point1id = getPointID(p1);
                iline.point2id = getPointID(p2);
                iline.normalize();
                lines.add(iline);
            }
        }
    }

    int getPointID(Point p) {
        int i = 0;
        for (Point point : points) {
            if (p.equals(point)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    JsonStructure getJSONPointsDefinition() {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for (Point point : points) {
            JsonObject jsonPoint = Json.createObjectBuilder().add("x", point.x).add("y", point.y).build();
            jsonArray.add(jsonPoint);
        }

        return jsonArray.build();
    }

    JsonStructure getJSONLinesDefinition() {

        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for (InternalLine internalLine : lines) {
            JsonObject jsonPoint = Json.createObjectBuilder().add("p1", internalLine.point1id).add("p2", internalLine.point2id).build();
            jsonArray.add(jsonPoint);
        }

        return jsonArray.build();
    }

    JsonStructure getJSONAllStates() {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for (State state : states) {
            jsonArray.add(getJsonState(state));
        }
        return jsonArray.build();
    }

    private JsonObject getJsonState(State state) {

        Map<Point, GeoObject.Status> displayPoints = new HashMap<>();
        for (Point point : state.getPoints()) {
            if (displayPoints.containsKey(point))
                displayPoints.put(point, mergeStatus(displayPoints.get(point), point.getStatus()));
            else
                displayPoints.put(point, point.getStatus());
        }

        JsonArrayBuilder pointsArray = Json.createArrayBuilder();

        for (Map.Entry<Point, Status> pointStatusEntry : displayPoints.entrySet()) {
            Point point = pointStatusEntry.getKey();
            int pointId = getPointID(point);
            JsonObject jsonPoint = Json.createObjectBuilder().add("pointID", pointId).add("style", pointStatusEntry.getValue().toString()).build();
            pointsArray.add(jsonPoint);
        }

        JsonArrayBuilder linesArray = Json.createArrayBuilder();

        for (Line line : state.getLines()) {
            int lineID = getLineID(line);
            JsonObject jsonLine = Json.createObjectBuilder().add("lineID", lineID).add("style", line.getStatus().toString()).build();
            linesArray.add(jsonLine);
        }

        return Json.createObjectBuilder().add("lines", linesArray).add("points", pointsArray).build();
    }

    private Status mergeStatus(Status s1, Status s2) {
        if (s1 == Status.Active || s2 == Status.Active) {
            return Status.Active;
        } else if (s1 == Status.Normal || s2 == Status.Normal) {
            return Status.Normal;
        }
        return Status.Processed;
    }

    int getLineID( InternalLine iline) {
        int i = 0;
        for (InternalLine line : lines) {
            if (line.equals(iline)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    int getLineID(Line line) {
        InternalLine iline = new InternalLine();
        iline.point1id = getPointID(line.point1);
        iline.point2id = getPointID(line.point2);
        iline.normalize();
        return getLineID(iline);
    }


    private class InternalLine implements Comparable {
        int point1id, point2id;

        boolean equals(InternalLine that) {
            return point1id == that.point1id && point2id == that.point2id;
        }

        boolean isSmaller(InternalLine that) {
            return point1id == that.point1id ? point2id < that.point2id : point1id < that.point1id;
        }

        void normalize() {
            if (point1id > point2id) {
                int temp = point1id;
                point1id = point2id;
                point2id = temp;
            }
        }

        @Override
        public int compareTo(Object o) {

            InternalLine that = (InternalLine) o;
            if (point1id == that.point1id) {
                if (point2id < that.point2id) {
                    return -1;
                } else if (point2id == that.point2id) {
                    return 1;
                } else return 0;
            } else if (point1id < that.point1id) {
                return -1;
            } else if (point1id > that.point1id) {
                return 1;
            }
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InternalLine that = (InternalLine) o;

            if (point1id != that.point1id) return false;
            if (point2id != that.point2id) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = point1id;
            result = 31 * result + point2id;
            return result;
        }
    }

}
