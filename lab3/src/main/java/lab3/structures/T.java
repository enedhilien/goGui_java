package lab3.structures;

import gogui.Line;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class T {

    private List<Line> activeLines = new ArrayList<>();

    public void add(Line lineToInsert, double broomX) {
        if (activeLines.isEmpty()) {
            activeLines.add(lineToInsert);
        }

        Comparator<Line> lineComparator = (l1, l2) -> {
            if (l1.getY(broomX) > l2.getY(broomX)) {
                return 1;
            } else if (l1.getY(broomX) < l2.getY(broomX)) {
                return -1;
            }
            return 0;
        };
        int indexToInsert = 0;
        for (int i = 0; i < activeLines.size(); i++) {

            Line activeLine = activeLines.get(i);

            if ( activeLine.getY(broomX) < lineToInsert.getY(broomX) ) {
                indexToInsert = i;
                break;
            }
            indexToInsert++;
        }
        activeLines.add(indexToInsert, lineToInsert);
    }

    public Optional<Line> getRightNeighbor(Line currentLine) {
        Line neighborLine = null;

        int sIndex = activeLines.indexOf(currentLine);
        if (sIndex < activeLines.size() - 1) {
            neighborLine = activeLines.get(sIndex + 1);
        }
        return Optional.ofNullable(neighborLine);
    }

    public Optional<Line> getLeftNeighbor(Line currentLine) {
        Line neighborLine = null;

        int sIndex = activeLines.indexOf(currentLine);
        if (sIndex > 0) {
            neighborLine = activeLines.get(sIndex - 1);
        }
        return Optional.ofNullable(neighborLine);
    }

    public void remove(Line finishedLine) {
        activeLines.remove(finishedLine);
    }
}
