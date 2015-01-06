package halfEdge;

import gogui.GoGui;
import gogui.Polygon;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Vector;

import static gogui.GoGui.clear;
import static gogui.GoGui.snapshot;

/**
 * Created by testuser on 12/11/14.
 */
public class Main {

    private static final String RESOURCE_PATH = Paths.get("skibinski_project", "src", "main", "resources").toString();
    private static final String FILE_NAME = "input2.txt";
    public static HalfEdgeStructure structure = new HalfEdgeStructure();
    private static Vector<Polygon> triangles = new Vector<>();

    public static void main(String[] args) {
        InputParser parser = new InputParser(Paths.get(RESOURCE_PATH, FILE_NAME).toString());
        try {
            parser.parse();
            snapshot();
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "mesh.json").toString());
            clear();


//            for(Vector<Vertex> v: parser.getTriangles()){
//               triangles.add(new Polygon(new GeoList<Point>(v)));
//                snapshot();
//            }

            for (Vector<HalfVertex> triangle : parser.getTriangles()) {
                structure.processFace(triangle);
            }
            structure.merge();

//            HalfEdge start = structure.halfEdges.get("68");
//            HalfEdge e = start;
//            do{
//                System.out.println(e.face);
//                e = e.getSymEdge().getNextEdge();
//            }while(e != start);

            HalfFace face = structure.faces.get("067");
            HalfEdge start = face.getEdge();
            HalfEdge e = start;
            do{
                System.out.println(e);
                e = e.getNextEdge();
            }while(e != start);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
