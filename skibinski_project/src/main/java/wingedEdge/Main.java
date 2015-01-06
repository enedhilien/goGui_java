package wingedEdge;

import gogui.GeoList;
import gogui.GoGui;
import gogui.Line;
import gogui.Polygon;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Vector;

import static gogui.GoGui.clear;
import static gogui.GoGui.registerContainer;
import static gogui.GoGui.snapshot;

/**
 * Created by testuser on 12/11/14.
 */
public class Main {

    private static final String RESOURCE_PATH = Paths.get("skibinski_project","src", "main", "resources").toString();
    private static final String FILE_NAME = "input2.txt";

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

            GeoList<Line> mesh = new GeoList<>();
            for(WingedEdge e: structure.edges){
                Line from = WingedEdge.from(e);
                mesh.add(from);
                snapshot();
            }
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "edges_iteration.json").toString());

            clear();
            GeoList<Line> lines = new GeoList<>();
            for(WingedFace f:structure.faces){
                WingedEdge start = f.getEdge();
                WingedEdge e = start;
                do{
                    lines.add(WingedEdge.from(e));
                    snapshot();
                    if(e.getnFace() == f){
                        e = e.getNccw();
                    }else{
                        e = e.getNcw();
                    }
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
                    if(e.getnFace() == f){
                        e = e.getNccw();
                    }else{
                        e = e.getPccw();
                    }
                }while(e!=start);
            }
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "face_traverse_ccw.json").toString());

            clear();
            registerContainer(mesh);
            snapshot();
            lines = new GeoList<>();

            WingedVertex v = structure.vertices.get(7);
            WingedEdge start = v.edge;
            WingedEdge e = start;
            do{
                Line line = WingedEdge.from(e);
                line.setColor("red");
                lines.add(line);
                    snapshot();
                    if(e.getnVertex() == v){
                        e = e.getNcw();
                    }else {
                        e = e.getPcw();
                    }
                }while(e!=start);
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "vertex_traverse.json").toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
