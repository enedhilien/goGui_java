package gogui;

import gogui.history.History;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class GoGui {

    private static History history = new History();
    private static List<GeoList> activeLists = new ArrayList<>();

    public static void clear() {
        history.clear();
        activeLists.clear();
    }

    public static String getJSON() {
        return new JsonPrinter(History.getStates()).getJSON();
    }

    public static void saveJSON(String fileName) {

        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.append(new JsonPrinter(History.getStates()).getJSON());
            printWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static GeoList<Line> loadLinesFromJson(String s) {

        GeoList<Line> lines = new GeoList<>();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(s));

            JSONArray allObjects = (JSONArray) obj;
            Iterator<JSONObject> iterator = allObjects.iterator();
            while (iterator.hasNext()) {
                JSONObject next = iterator.next();
                JSONArray points = (JSONArray) next.get("points");
                Iterator<JSONObject> pointIterator = points.iterator();
                JSONObject point1 = pointIterator.next();
                JSONObject point2 = pointIterator.next();
                int point1X = Integer.parseInt(point1.get("x").toString());
                int point1Y = Integer.parseInt(point1.get("y").toString());
                int point2X = Integer.parseInt(point2.get("x").toString());
                int point2Y = Integer.parseInt(point2.get("y").toString());

                Line line = new Line(new Point(point1X, point1Y), new Point(point2X, point2Y), true);
                lines.push_back(line);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static GeoList<Point> loadPoints(String s) {
        GeoList<Point> points = new GeoList<>();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(s));

            JSONArray allObjects = (JSONArray) obj;
            Iterator<JSONObject> iterator = allObjects.iterator();
            while (iterator.hasNext()) {
                JSONObject next = iterator.next();
                JSONArray pointsArray = (JSONArray) next.get("points");
                Iterator<JSONObject> pointIterator = pointsArray.iterator();
                JSONObject point1 = pointIterator.next();
                JSONObject point2 = pointIterator.next();
                int point1X = Integer.parseInt(point1.get("x").toString());
                int point1Y = Integer.parseInt(point1.get("y").toString());
                int point2X = Integer.parseInt(point2.get("x").toString());
                int point2Y = Integer.parseInt(point2.get("y").toString());

                if (!points.contains(new Point(point1X, point1Y))) {
                    points.push_back(new Point(point1X, point1Y));
                }

                if (!points.contains(new Point(point2X, point2Y))) {
                    points.push_back(new Point(point2X, point2Y));
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return points;
    }

    public static GeoList<Point> loadPoints_ZMUDA(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            List<Point> collectedPoints = bufferedReader.lines().map(line -> {
                String[] split = line.split(" ");
                return new Point(Double.valueOf(split[0]).intValue(), Double.valueOf(split[1]).intValue());
            }).collect(toList());
            return new GeoList<>(collectedPoints);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new GeoList<>();
    }


    public static void snapshot() {
        history.snapshot();
    }

    public static List<GeoList> getActiveLists() {
        return activeLists;
    }

    public static <T extends GeoObject> void registerContainer(GeoList<T> ts) {
        activeLists.add(ts);
    }

    public static History getHistory() {
        return history;
    }
}
