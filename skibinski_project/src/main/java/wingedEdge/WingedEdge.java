package wingedEdge;

/**
 * Created by testuser on 12/10/14.
 */
public class WingedEdge {
    WingedEdgeVertex vertex;
    WingedEdgeFace face;
    WingedEdge prev, next, sym;

    public WingedEdge getNext() {
        return next;
    }

    public WingedEdge getPrev() {
        return prev;
    }

    public WingedEdge getSym() {
        return sym;
    }

    public WingedEdgeFace getFace() {
        return face;
    }

    public WingedEdgeVertex getVertex() {
        return vertex;
    }
}
