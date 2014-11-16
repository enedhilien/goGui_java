package lab3;

import gogui.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.*;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {

        GeoList<Line> lines = loadJsonFromFile("lab3\\src\\main\\resources\\input.json");


    }

    private static GeoList<Line> loadJsonFromFile(String s) {

        GeoList<Line> lines = new GeoList<>();

        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(s));

            JSONArray allObjects = (JSONArray) obj;
            Iterator<JSONObject> iterator = allObjects.iterator();
            while (iterator.hasNext()) {
                JSONObject next = iterator.next();
                int lineId = Integer.valueOf(next.get("lineID").toString());
                JSONArray points = (JSONArray) next.get("points");
                Iterator<JSONObject> pointIterator = points.iterator();
                JSONObject point1 = pointIterator.next();
                JSONObject point2 = pointIterator.next();
                int point1X = Integer.parseInt(point1.get("x").toString());
                int point1Y = Integer.parseInt(point1.get("y").toString());
                int point2X = Integer.parseInt(point2.get("x").toString());
                int point2Y = Integer.parseInt(point2.get("y").toString());

                Line line = new Line(new Point(point1X, point1Y), new Point(point2X, point2Y));
                lines.push_back(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
