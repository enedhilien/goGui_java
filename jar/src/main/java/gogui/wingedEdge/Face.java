package gogui.wingedEdge;

import gogui.GeoObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by testuser on 12/10/14.
 */
public class Face extends GeoObject {
    Edge edge;
    String id;

    public Face(Vector<Vertex> vertices) {
        setId(vertices);
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public String getId() {
        return id;
    }

    public void setId(Vector<Vertex> vertices) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Vertex v : vertices) {
            ids.add(v.id);
        }
        Collections.sort(ids);
        StringBuilder stringBuilder = new StringBuilder();
        ids.forEach(stringBuilder::append);
        id = stringBuilder.toString();
    }
}
