/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.astar;
import rpg.common.entities.Entity;

public interface PathFinder {
    
    public Path findPath(Entity entity, int startX, int startY, int targetX, int targetY);
}
