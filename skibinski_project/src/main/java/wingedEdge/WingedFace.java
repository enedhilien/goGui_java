package wingedEdge;

import gogui.GeoObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by testuser on 12/10/14.
 */
public class WingedFace extends GeoObject {
    WingedEdge edge;
    String id;

    public WingedFace(Vector<WingedVertex> vertices) {
        setId(vertices);
    }

    public WingedEdge getEdge() {
        return edge;
    }

    public void setEdge(WingedEdge edge) {
        this.edge = edge;
    }

    public String getId() {
        return id;
    }

    public void setId(Vector<WingedVertex> vertices) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (WingedVertex v : vertices) {
            ids.add(v.id);
        }
        Collections.sort(ids);
        StringBuilder stringBuilder = new StringBuilder();
        ids.forEach(stringBuilder::append);
        id = stringBuilder.toString();
    }
}
