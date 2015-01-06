package halfEdge;

/**
 * Created by testuser on 1/5/15.
 */
public class HalfEdge {
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



}
