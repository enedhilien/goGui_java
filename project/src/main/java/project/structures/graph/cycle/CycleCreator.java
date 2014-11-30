package project.structures.graph.cycle;

import project.structures.HalfEdge;

import java.util.ArrayList;
import java.util.List;

public class CycleCreator {


    public static List<EdgesCycle> createCycles(List<HalfEdge> edges) {

        List<EdgesCycle> result = new ArrayList<>();

        List<HalfEdge> usedEdges = new ArrayList<>();
        for (HalfEdge edge : edges) {
            if (!edgeAlreadyProcessed(usedEdges, edge)) {
                EdgesCycle cycle = createCycle(edge);
                usedEdges.addAll(cycle.getEdges());
                result.add(cycle);
            }
        }
        return result;
    }

    private static EdgesCycle createCycle(HalfEdge startEdge) {
        List<HalfEdge> allEdges = new ArrayList<>();
        HalfEdge currentEdge = startEdge;
        do {
            allEdges.add(currentEdge);
            currentEdge = currentEdge.next;

        } while (startEdge != currentEdge);


        return new EdgesCycle(allEdges);
    }

    private static boolean edgeAlreadyProcessed(List<HalfEdge> usedEdges, HalfEdge edge) {
        for (HalfEdge usedEdge : usedEdges) {
            if (edge == usedEdge) {
                return true;
            }
        }
        return false;
    }

}
