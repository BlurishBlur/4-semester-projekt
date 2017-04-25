/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.astarcnc;

import rpg.common.world.Room;

/**
 *
 * @author Antonio
 */
public interface AStarHeuristic {
    public float getCost(Room room, Mover mover, int startX, int startY, int targetX, int targetY);
}
