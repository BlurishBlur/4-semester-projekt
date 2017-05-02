/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.astarcnc;

import rpg.common.world.Room;
import rpg.common.entities.Entity;

/**
 *
 * @author Antonio
 */
public class ClosestHeuristic implements AStarHeuristic {

    @Override
    public float getCost(Room room, Entity entity, int startX, int startY, int targetX, int targetY) {
        float dx = targetX - startX;
        float dy = targetY - startY;

        float result = (float) (Math.sqrt((dx * dx) + (dy * dy)));
        return result;
    }

}
