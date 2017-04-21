package rpg.common.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Polygon {
    
    private List<Vector> polygon;
    
    public Polygon() {
        polygon = new CopyOnWriteArrayList<>();
    }
    
    public Vector get(int index) {
        return polygon.get(index);
    }
    
    public void add(Vector vector) {
        polygon.add(vector);
    }
    
    public Vector getLast() {
        return polygon.get(polygon.size() - 1);
    }
    
    public int size() {
        return polygon.size();
    }
    
}
