import gogui.GeoList;
import gogui.GoGui;
import gogui.Line;
import gogui.Polygon;
import wingedEdge.WingedEdge;
import wingedEdge.WingedEdgeStructure;
import wingedEdge.WingedFace;
import wingedEdge.WingedVertex;

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

            for(Vector<WingedVertex> triangle: parser.getTriangles()){
                structure.processFace(triangle);
            }
            System.out.println(structure.edges);
            structure.mergeFaces();
            clear();

            GeoList<Line> lines = new GeoList<>();
            for(WingedEdge e: structure.edges){
                Line from = WingedEdge.from(e);
                lines.add(from);
                snapshot();
            }
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "edges_iteration.json").toString());

            clear();
            lines = new GeoList<>();
            for(WingedFace f:structure.faces){
                WingedEdge start = f.getEdge();
                WingedEdge e = start;
                do{
                    lines.add(WingedEdge.from(e));
                    snapshot();
                    e = e.getNcw();
                }while(e!=start);
            }
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "face_traverse_cw.json").toString());

            clear();
            lines = new GeoList<>();
            for(WingedFace f:structure.faces){
                WingedEdge start = f.getEdge();
                WingedEdge e = start;
                do{
                    lines.add(WingedEdge.from(e));
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
