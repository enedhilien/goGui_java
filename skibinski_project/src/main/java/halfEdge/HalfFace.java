package halfEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by testuser on 1/5/15.
 */
public class HalfFace {
    String id = "";
    HalfEdge edge;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HalfEdge getEdge() {
        return edge;
    }

    public void setEdge(HalfEdge edge) {
        this.edge = edge;
    }

    public HalfFace(Vector<HalfVertex> vertices){
        ArrayList<Integer> ids = new ArrayList<>();
        for(HalfVertex v: vertices){
            ids.add(v.id);
        }
        Collections.sort(ids);
        for(Integer i: ids)
            id += i;

    }
    public String toString(){
        return getId();
    }
}
