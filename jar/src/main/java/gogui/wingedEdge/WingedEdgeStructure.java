package gogui.wingedEdge;

import gogui.GeoList;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by testuser on 12/11/14.
 */
public class WingedEdgeStructure {

    public GeoList<Edge> edges = new GeoList<>();
    public Vector<Face> faces = new Vector<>();
    public Vector<Vertex> vertices = new Vector<>();


    public void processFace(Vector<Vertex> face) {
        Vertex[] vertexes = new Vertex[face.size()];
        vertexes = face.toArray(vertexes);
        Vertex v1,v2;
        Edge e;
        Vector<Edge> newEdges = new Vector<>();
        Face newFace = new Face(face);
        for(int i=0; i<face.size();i++){
            v1 = face.get(i); v2 = face.get((i+1)%face.size());
            e = new Edge();
            e.setnVertex(v1);
            e.setpVertex(v2);
            e.setnFace(newFace);
            newEdges.add(e);
        }
        int n = newEdges.size();
        for(int i=0;i<n;i++){
            e = newEdges.get(i);
            e.setNcw(newEdges.get((i-1)%n < 0 ? ((i-1)%n)+n : (i-1)%n));
            e.setNccw(newEdges.get((i+1)%n));
        }
        newFace.setEdge(newEdges.get(0));
        faces.add(newFace);
        edges.addAll(newEdges);
    }

    public void mergeFaces(){
        edges.sort(Edge.ID_COMPARATOR);
        Iterator<Edge> it = edges.iterator();
        GeoList<Edge> merged = new GeoList<>();
        Edge e1, e2;
        int n = edges.size();
        for(int i=0;i<n-1;i++){;
            e1 = edges.get(i);
            e2 = edges.get(i+1);
            if(e1.getId().equals(e2.getId())){
                e1.setpFace(e2.getnFace());
                e1.setPcw(e2.getNcw());
                e1.setPccw(e2.getNccw());
                i++;
            }
            merged.add(e1);
        }
        e1 = edges.get(n-2);
        e2 = edges.get(n-1);
        if(e1.getId().equals(e2.getId())){
            e1.setpFace(e2.getnFace());
            e1.setPcw(e2.getNcw());
            e1.setPccw(e2.getNccw());
            merged.add(e1);
        }else {
            merged.add(e2);
        }
        edges = merged;
    }



}
