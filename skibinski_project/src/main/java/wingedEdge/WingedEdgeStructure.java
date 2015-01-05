package wingedEdge;

import gogui.GeoList;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by testuser on 12/11/14.
 */
public class WingedEdgeStructure {

    public GeoList<WingedEdge> edges = new GeoList<>();
    public Vector<WingedFace> faces = new Vector<>();
    public Vector<WingedVertex> vertices = new Vector<>();


    public void processFace(Vector<WingedVertex> face) {
        WingedVertex[] vertexes = new WingedVertex[face.size()];
        vertexes = face.toArray(vertexes);
        WingedVertex v1,v2;
        WingedEdge e;
        Vector<WingedEdge> newEdges = new Vector<>();
        WingedFace newFace = new WingedFace(face);
        for(int i=0; i<face.size();i++){
            v1 = face.get(i); v2 = face.get((i+1)%face.size());
            e = new WingedEdge();
            e.setnVertex(v1);
            e.setpVertex(v2);
            e.setnFace(newFace);
            v1.setEdge(e);
            v2.setEdge(e);
            vertices.add(v1);
            vertices.add(v2);
            newEdges.add(e);
        }
        int n = newEdges.size();
        for(int i=0;i<n;i++){
            e = newEdges.get(i);
            e.setNcw(newEdges.get((i-1)%n < 0 ? ((i-1)%n)+n : (i-1)%n));
            e.setNccw(newEdges.get((i+1)%n));
            e.setPcw(e.getNcw());
            e.setPccw(e.getNccw());
        }
        newFace.setEdge(newEdges.get(0));
        faces.add(newFace);
        edges.addAll(newEdges);
    }

    public void mergeFaces(){
        edges.sort(WingedEdge.ID_COMPARATOR);
        Iterator<WingedEdge> it = edges.iterator();
        GeoList<WingedEdge> merged = new GeoList<>();
        WingedEdge e1, e2;
        int n = edges.size();
        for(int i=0;i<n-1;i++){;
            e1 = edges.get(i);
            e2 = edges.get(i+1);
            if(e1.getId().equals(e2.getId())){
                e1.setpFace(e2.getnFace());
                e1.setPccw(e2.getNcw());
                e1.setPcw(e2.getNccw());
                i++;
            }
            merged.add(e1);
        }
        e1 = edges.get(n-2);
        e2 = edges.get(n-1);
        if(e1.getId().equals(e2.getId())){
            e1.setpFace(e2.getnFace());
            e1.setPccw(e2.getNcw());
            e1.setPcw(e2.getNccw());
            merged.add(e1);
        }else {
            merged.add(e2);
        }
        System.out.println(merged.size());
        edges = merged;
    }



}
