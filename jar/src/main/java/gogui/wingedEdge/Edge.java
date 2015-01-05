package gogui.wingedEdge;

import gogui.GeoObject;
import gogui.Line;
import gogui.Point;

import java.util.Comparator;

/**
 * Created by testuser on 12/10/14.
 */
public class Edge extends GeoObject{

    public final static Comparator<Edge> ID_COMPARATOR = new IdComparator();

    public Edge(Vertex pVertex, Vertex nVertex) {
        this.pVertex = pVertex;
        this.nVertex = nVertex;
    }

    public Edge() {

    }

    public Vertex getpVertex() {
        return pVertex;
    }

    public void setpVertex(Vertex pVertex) {
        this.pVertex = pVertex;
    }

    public Vertex getnVertex() {
        return nVertex;
    }

    public void setnVertex(Vertex nVertex) {
        this.nVertex = nVertex;
    }

    public Face getpFace() {
        return pFace;
    }

    public void setpFace(Face pFace) {
        this.pFace = pFace;
    }

    public Face getnFace() {
        return nFace;
    }

    public void setnFace(Face nFace) {
        this.nFace = nFace;
    }

    public Edge getPcw() {
        return pcw;
    }

    public void setPcw(Edge pcw) {
        this.pcw = pcw;
    }

    public Edge getNcw() {
        return ncw;
    }

    public void setNcw(Edge ncw) {
        this.ncw = ncw;
    }

    public Edge getNccw() {
        return nccw;
    }

    public void setNccw(Edge nccw) {
        this.nccw = nccw;
    }

    public Edge getPccw() {
        return pccw;
    }

    public void setPccw(Edge pccw) {
        this.pccw = pccw;
    }

    private Vertex pVertex, nVertex;
    private Face pFace, nFace;
    private Edge pcw, ncw, nccw, pccw;

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


    private static class IdComparator implements Comparator<Edge>{

        @Override
        public int compare(Edge o1, Edge o2) {
            return o1.getId().compareTo(o2.getId());
        }

    }

    public static Line from(Edge edge) {
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
