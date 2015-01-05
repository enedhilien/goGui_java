import gogui.GeoList;
import gogui.GoGui;
import gogui.Line;
import gogui.Polygon;
import gogui.wingedEdge.Edge;
import gogui.wingedEdge.Face;
import gogui.wingedEdge.Vertex;
import gogui.wingedEdge.WingedEdgeStructure;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Vector;

import static gogui.GoGui.clear;
import static gogui.GoGui.snapshot;

/**
 * Created by testuser on 12/11/14.
 */
public class Main {

    private static final String RESOURCE_PATH = Paths.get("skibinski_project","src", "main", "resources").toString();
    private static final String FILE_NAME = "input.txt";

    private static Vector<Polygon> triangles = new Vector<>();

    public static WingedEdgeStructure structure = new WingedEdgeStructure();
    public static void main(String[] args){
        InputParser parser = new InputParser(Paths.get(RESOURCE_PATH, FILE_NAME).toString());
        try {
            parser.parse();
            snapshot();GoGui.saveJSON(Paths.get(RESOURCE_PATH, "mesh.json").toString());
            clear();


//            for(Vector<Vertex> v: parser.getTriangles()){
//               triangles.add(new Polygon(new GeoList<Point>(v)));
//                snapshot();
//            }

            for(Vector<Vertex> triangle: parser.getTriangles()){
                structure.processFace(triangle);
            }
            System.out.println(structure.edges);
            structure.mergeFaces();
            clear();

            GeoList<Line> lines = new GeoList<>();
            for(Edge e: structure.edges){
                Line from = Edge.from(e);
                lines.add(from);
                snapshot();
            }
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "edges_iteration.json").toString());

            clear();
            lines = new GeoList<>();
            for(Face f:structure.faces){
                Edge start = f.getEdge();
                Edge e = start;
                do{
                    lines.add(Edge.from(e));
                    snapshot();
                    e = e.getNcw();
                }while(e!=start);
            }
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "face_traverse_cw.json").toString());

            clear();
            lines = new GeoList<>();
            for(Face f:structure.faces){
                Edge start = f.getEdge();
                Edge e = start;
                do{
                    lines.add(Edge.from(e));
                    snapshot();
                    e = e.getNccw();
                }while(e!=start);
            }
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "face_traverse_ccw.json").toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
