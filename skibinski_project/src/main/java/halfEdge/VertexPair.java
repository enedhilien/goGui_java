package halfEdge;

/**
 * Created by testuser on 1/5/15.
 */
public class VertexPair<T extends HalfVertex, T1 extends  HalfVertex>{

    HalfVertex v1;
    HalfVertex v2;

    public VertexPair(T v1, T1 v2){
        if (v1.id < v2.id){
            this.v1 = v1;
            this.v2 = v2;
        }else{
            this.v1 = v2;
            this.v2 = v1;
        }
    }

    @Override
    public int hashCode() {
        return v1.id * 13 + v2.id;
    }
}
