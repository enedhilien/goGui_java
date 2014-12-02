package project.structures;

public class Wall {

    HalfEdge outerEdge;
    public String name;

    public Wall(HalfEdge outerEdge, String name) {
        this.outerEdge = outerEdge;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wall wall = (Wall) o;

        if (name != null ? !name.equals(wall.name) : wall.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
