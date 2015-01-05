package halfEdge;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by testuser on 1/5/15.
 */
public class HalfEdgeStructure {

    private Map<VertexPair<HalfVertex, HalfVertex>, HalfEdge> edges = new HashMap<>();

    public void processFace(Vector<HalfVertex> face) {
        HalfVertex[] vertexes = new HalfVertex[face.size()];
        vertexes = face.toArray(vertexes);
        HalfVertex v1,v2;
        for(int i=0;i<face.size();i++){
            HalfEdge edge = new HalfEdge();
            v1 = vertexes[i];
            v2 = vertexes[(i + 1) % face.size()];
            edge.setPrevVertex(v1);
            edge.setNextVertex(v2);
            edges.put(new VertexPair<>(v1, v2), edge);
            System.out.println("Putting " + v1.id +" -> " + v2.id);
        }
        System.out.println(edges.size());
        for(int i=0;i<face.size();i++){


        }

    }
}
