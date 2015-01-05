package wingedEdge;

import gogui.GeoObject;
import gogui.Line;
import gogui.Point;

import java.util.Comparator;

/**
 * Created by testuser on 12/10/14.
 */
public class WingedEdge extends GeoObject{

    public final static Comparator<WingedEdge> ID_COMPARATOR = new IdComparator();

    public WingedEdge(WingedVertex pVertex, WingedVertex nVertex) {
        this.pVertex = pVertex;
        this.nVertex = nVertex;
    }

    public WingedEdge() {

    }

    public WingedVertex getpVertex() {
        return pVertex;
    }

    public void setpVertex(WingedVertex pVertex) {
        this.pVertex = pVertex;
    }

    public WingedVertex getnVertex() {
        return nVertex;
    }

    public void setnVertex(WingedVertex nVertex) {
        this.nVertex = nVertex;
    }

    public WingedFace getpFace() {
        return pFace;
    }

    public void setpFace(WingedFace pFace) {
        this.pFace = pFace;
    }

    public WingedFace getnFace() {
        return nFace;
    }

    public void setnFace(WingedFace nFace) {
        this.nFace = nFace;
    }

    public WingedEdge getPcw() {
        return pcw;
    }

    public void setPcw(WingedEdge pcw) {
        this.pcw = pcw;
    }

    public WingedEdge getNcw() {
        return ncw;
    }

    public void setNcw(WingedEdge ncw) {
        this.ncw = ncw;
    }

    public WingedEdge getNccw() {
        return nccw;
    }

    public void setNccw(WingedEdge nccw) {
        this.nccw = nccw;
    }

    public WingedEdge getPccw() {
        return pccw;
    }

    public void setPccw(WingedEdge pccw) {
        this.pccw = pccw;
    }

    private WingedVertex pVertex, nVertex;
    private WingedFace pFace, nFace;
    private WingedEdge pcw, ncw, nccw, pccw;

    @Override
    public String toString() {
        return "Line{" +
                "p1=" + pVertex +
                ", p2=" + nVertex +
                '}';
    }

    public String getId(){
        if(pVertex == null || nVertex == null)
            return "NULL_ID";
        return pVertex.id < nVertex.id ? new StringBuilder().append(pVertex.id).append(nVertex.id).toString() : new StringBuilder().append(nVertex.id).append(pVertex.id).toString();
    }


    private static class IdComparator implements Comparator<WingedEdge>{

        @Override
        public int compare(WingedEdge o1, WingedEdge o2) {
            return o1.getId().compareTo(o2.getId());
        }

    }

    public static Line from(WingedEdge edge) {
        Line newLine = new Line(new Point(edge.pVertex.x, edge.pVertex.y), new Point(edge.nVertex.x, edge.nVertex.y));
        newLine.setStatus(edge.getStatus());
        if (edge.hasCustomColor()) {
            newLine.setColor(edge.getColor());
        }
        return newLine;
    }
//    public Edge(EdgeBuilder edgeBuilder) {
//        this.vertex = edgeBuilder.vertex;
//        this.face = edgeBuilder.face;
//        this.prev = edgeBuilder.prev;
//        this.next = edgeBuilder.next;
//        this.sym = edgeBuilder.sym;
//    }
//    public static class EdgeBuilder {
//        private Vertex vertex;
//        private Face face;
//        private Edge prev, next, sym;
//
//
//        public EdgeBuilder vertex(Vertex v) {
//            this.vertex = v;
//            return this;
//        }
//
//        public EdgeBuilder face(Face f) {
//            this.face = f;
//            return this;
//        }
//
//        public EdgeBuilder prev(Edge prev) {
//            this.prev = prev;
//            return this;
//        }
//
//        public EdgeBuilder next(Edge next) {
//            this.next = next;
//            return this;
//        }
//
//        public EdgeBuilder sym(Edge sym) {
//            this.sym = sym;
//            return this;
//        }
//
//        public Edge build() {
//            return new Edge(this);
//        }
//    }
}
