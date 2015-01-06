package halfEdge;

import gogui.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by testuser on 1/5/15.
 */
public class HalfEdgeStructure {

    private Map<String, ArrayList<HalfEdge>> edges = new HashMap<>();
    Map<String, HalfEdge> halfEdges;
    Map<String, HalfFace> faces = new HashMap<>();
    Map<Integer, HalfVertex> vertices = new HashMap<>();

    public void processFace(Vector<HalfVertex> face) {
        HalfVertex[] vertexes = new HalfVertex[face.size()];
        ArrayList<HalfEdge> newEdges = new ArrayList<>();
        vertexes = face.toArray(vertexes);
        HalfVertex v0,v1,v2;
        HalfFace newFace = new HalfFace(face);
        faces.put(newFace.getId(), newFace);
        for(int i=0;i<face.size();i++){
            HalfEdge edge = new HalfEdge();
            v1 = vertexes[i];
            v2 = vertexes[(i + 1) % face.size()];
            vertices.put(v1.id, v1);
            edge.setPrevVertex(v1);
            edge.setNextVertex(v2);
            edge.setFace(newFace);
            newFace.setEdge(edge); //TODO: make it happen only once
            if(edges.get(edge.getId()) == null){
                edges.put(edge.getId(), new ArrayList<>());
            }
            edges.get(edge.getId()).add(edge);
            newEdges.add(edge);
            System.out.println("Putting " + edge.getId());

//            edge = new HalfEdge();
//            edge.setPrevVertex(v2);
//            edge.setNextVertex(v1);
//            edge.setFace(newFace);
//            if(edges.get(edge.getId()) == null){
//                edges.put(edge.getId(), new ArrayList<>());
//            }
//            edges.get(edge.getId()).add(edge);
//            newEdges.add(edge);
//            System.out.println("Putting " + edge.getId());
        }

        HalfEdge[] newEdgesArray = new HalfEdge[newEdges.size()];
        newEdgesArray = newEdges.toArray(newEdgesArray);
        int n = newEdgesArray.length;
        for(int i=0;i<n;i++){
            HalfEdge prev = newEdgesArray[(i-1)%n < 0 ? ((i-1)%n)+n : (i-1)%n];
            HalfEdge next = newEdgesArray[(i+1)%n];
            HalfEdge current = newEdgesArray[i];
            current.setNextEdge(next);
            current.setPrevEdge(prev);
            current.setSymEdge(current);

        }

    }

    public static String getId(HalfVertex v1, HalfVertex v2){
        return v1.id + "" + v2.id;
    }

    public void merge(){
        Map<String, HalfEdge> merged = new HashMap<>();
        for(String key:edges.keySet()){
            System.out.println(key);
            System.out.println("\t" + edges.get(key));

            ArrayList<HalfEdge> halfEdges = edges.get(key);
            HalfEdge pairedEdge;
            if(halfEdges.size()>1){
                HalfEdge e1 = halfEdges.get(0);
                HalfEdge e2 = halfEdges.get(1);
                int ccw = Point.ccw(e1.getPrevVertex(), e1.nextEdge.getNextVertex(), e1.getNextVertex());
                pairedEdge = new HalfEdge();
                if(ccw==-1){
                    //e1 left, e2 right
                    pairedEdge.setSymEdge(e1);
                    e1.setSymEdge(pairedEdge);

                    pairedEdge.setNextVertex(e1.getPrevVertex());
                    pairedEdge.setPrevVertex(e1.getNextVertex());
                    pairedEdge.setFace(e2.getFace());
                    halfEdges.remove(e2);
                    merged.put(e1.getId(), e1);
                }else{
                    pairedEdge.setSymEdge(e2);
                    e2.setSymEdge(pairedEdge);

                    pairedEdge.setNextVertex(e2.getPrevVertex());
                    pairedEdge.setPrevVertex(e2.getNextVertex());
                    pairedEdge.setFace(e1.getFace());
                    halfEdges.remove(e1);
                    merged.put(e2.getId(), e2);
                }
                merged.put(pairedEdge.getId(), pairedEdge);
            }
            else {
                HalfEdge edge = halfEdges.get(0);
                merged.put(edge.getId(), halfEdges.get(0));
            }
        }
        System.out.println("###");
        for(String key: merged.keySet()){
            System.out.println(key);
            System.out.println("\t" + merged.get(key));
            HalfEdge edge = merged.get(key);
            HalfEdge paired;
            if((paired = merged.get(getId(edge.getNextVertex(), edge.getPrevVertex()))) != null){
                paired.setSymEdge(edge);
                edge.setSymEdge(paired);
            }
        }

        halfEdges = merged;
    }
}
