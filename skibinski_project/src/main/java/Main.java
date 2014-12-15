import gogui.GeoList;
import gogui.GoGui;
import gogui.Point;
import gogui.Polygon;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Vector;

import static gogui.GoGui.snapshot;

/**
 * Created by testuser on 12/11/14.
 */
public class Main {

    private static final String RESOURCE_PATH = Paths.get("skibinski_project","src", "main", "resources").toString();
    private static final String FILE_NAME = "input.txt";

    private static Vector<Polygon> triangles = new Vector<>();

    public static void main(String[] args){
        InputParser parser = new InputParser(Paths.get(RESOURCE_PATH, FILE_NAME).toString());
        try {
            parser.parse();
            snapshot();
            for(Vector<Point> v: parser.getTriangles()){
               triangles.add(new Polygon(new GeoList<Point>(v)));
                snapshot();
            }

            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "mesh.json").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
