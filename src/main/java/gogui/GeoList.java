package gogui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GeoList<T extends GeoObject> extends ArrayList<T> {

    private VisualizationMethod visualizationMethod = VisualizationMethod.CLOUD;

    public GeoList() {
        this(true);
    }

    public GeoList(List<T> vector) {
        this();
        this.addAll(vector);
    }

    public GeoList(List<T> vector, boolean b) {

        this(b);
        this.addAll(vector);
    }

    public GeoList(boolean register) {
        if (register) {
            GoGui.registerContainer(this);
        }
    }

    public void push_back(T object) {
        this.add(size(), object);
    }
}
