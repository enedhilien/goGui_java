package halfEdge;

import gogui.GeoObject;
import gogui.Line;
import gogui.Point;

/**
 * Created by testuser on 1/5/15.
 */
public class HalfEdge extends GeoObject{
    HalfEdge nextEdge;
    HalfEdge prevEdge;

    public String getId(){
        return prevVertex.id + "" + nextVertex.id;
    }
    public HalfEdge getNextEdge() {
        return nextEdge;
    }

    public void setNextEdge(HalfEdge nextEdge) {
        this.nextEdge = nextEdge;
    }

    public HalfEdge getPrevEdge() {
        return prevEdge;
    }

    public void setPrevEdge(HalfEdge prevEdge) {
        this.prevEdge = prevEdge;
    }

    public HalfEdge getSymEdge() {
        return symEdge;
    }

    public void setSymEdge(HalfEdge symEdge) {
        this.symEdge = symEdge;
    }

    public HalfVertex getPrevVertex() {
        return prevVertex;
    }

    public void setPrevVertex(HalfVertex prevVertex) {
        this.prevVertex = prevVertex;
    }

    public HalfVertex getNextVertex() {
        return nextVertex;
    }

    public void setNextVertex(HalfVertex nextVertex) {
        this.nextVertex = nextVertex;
    }

    public HalfFace getFace() {
        return face;
    }

    public void setFace(HalfFace face) {
        this.face = face;
    }

    public String toString(){
        return HalfEdgeStructure.getId(prevVertex, nextVertex) + " -> " + face.getId();
    }

    HalfEdge symEdge;
    HalfVertex prevVertex, nextVertex;
    HalfFace face;

    public static Line from(HalfEdge line) {

        Line newLine = new Line(new Point(line.getPrevVertex().x, line.getPrevVertex().y),
                new Point(line.getNextVertex().x, line.getNextVertex().y));
        newLine.setStatus(line.getStatus());
        if (line.hasCustomColor()) {
            newLine.setColor(line.getColor());
        }
        return newLine;
    }



}
