/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.astar;

import java.util.ArrayList;
import java.util.List;
import rpg.common.util.Vector;

/**
 *
 * @author Antonio
 */
public class Path {

    private List<Vector> steps = new ArrayList<>();

    public Path() {

    }

    public int getLength() {
        return steps.size();
    }

    public Vector getStep(int index) {
        return steps.get(index);
    }

    public int getX(int index) {
        return Math.round(getStep(index).getX());
    }

    public int getY(int index) {
        return Math.round(getStep(index).getY());
    }

    public void addStep(int x, int y) {
        steps.add(new Vector(x, y));
    }

    public void prependStep(int x, int y) {
        steps.add(0, new Vector(x, y));
    }

    public boolean contains(int x, int y) {
        return steps.contains(new Vector(x, y));
    }
}
