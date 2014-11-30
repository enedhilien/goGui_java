package project.structures.sweep;

import gogui.Line;

public class LinePair {
    public Line l1;
    public Line l2;

    public LinePair(Line l1, Line l2) {
        this.l1 = l1;
        this.l2 = l2;
    }

    @Override
    public String toString() {
        return "LinePair{" + "l1=" + l1 + ", l2=" + l2 + '}';
    }
}

