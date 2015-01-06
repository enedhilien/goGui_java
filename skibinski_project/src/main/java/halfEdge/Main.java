package halfEdge;

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


            for (Vector<HalfVertex> triangle : parser.getTriangles()) {
                structure.processFace(triangle);
            }
            structure.merge();
            
            GeoList<Line> mesh = new GeoList<>();
            for (HalfFace face : structure.faces.values()) {
                HalfEdge start = face.getEdge();
                HalfEdge e = start;
                do {
                    mesh.add(HalfEdge.from(e));
                    snapshot();
                    e = e.getNextEdge();
                } while (e != start);

            }
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "halfEdge_face_iteration.json").toString());
            clear();
            registerContainer(mesh);
            snapshot();

            GeoList<Line> lines = new GeoList<>();
            HalfEdge start = structure.halfEdges.get("68");
            HalfVertex vertex = structure.vertices.get(7);
            start = vertex.edge;
            HalfEdge e = start;
            do{
                Line l = HalfEdge.from(e);
                l.setColor("Red");
                lines.add(l);
                snapshot();
                e = e.getSymEdge().getNextEdge();
            }while(e != start);
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "halfEdge_vertex_iteration_inner.json").toString());

            clear();
            registerContainer(mesh);
            snapshot();
            lines = new GeoList<>();
            vertex = structure.vertices.get(4);
            start = vertex.edge;
            e = start;
            do{
                Line l = HalfEdge.from(e);
                l.setColor("Red");
                lines.add(l);
                snapshot();
                if(e.getSymEdge() != null){
                    e = e.getSymEdge().getNextEdge();
                }else{
                    break;
                }
            }while(e != start);
            GoGui.saveJSON(Paths.get(RESOURCE_PATH, "halfEdge_vertex_iteration_outer.json").toString());
            clear();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
