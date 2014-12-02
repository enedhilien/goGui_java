package project.structures.cycle;

import gogui.GeoList;
import gogui.GoGui;
import gogui.Line;

import java.util.List;

import static gogui.GeoObject.Status;

public class CycleGoGuiDrawer {

    public static void draw(List<EdgesCycle> cycles) {
        GeoList<Line> activeLines = new GeoList<>();
        for (EdgesCycle cycle : cycles) {
            activeLines.setStatus(Status.NORMAL);
            activeLines.clear();
            activeLines.addAll(cycle.getLines());
            activeLines.setColor("red");
            GoGui.snapshot();
        }
        activeLines.clear();
    }

    public static void draw(EdgesCycle cycle) {
        GeoList<Line> activeLines = new GeoList<>();
        activeLines.addAll(cycle.getLines());
        activeLines.setColor("red");
        GoGui.snapshot();
        activeLines.clear();
    }
}
