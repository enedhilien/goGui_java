package gogui;

import gogui.history.History;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GoGui {

    //    private static List<GeoList<Point>> pointsVector = new ArrayList<>();
//    private static List<GeoList<Line>> lineVector = new ArrayList<>();
    private static History history = new History();
    private static List<GeoList> activeLists = new ArrayList<>();

    public static void clean() {
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
