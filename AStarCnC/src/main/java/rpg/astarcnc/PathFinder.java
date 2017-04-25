/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.astarcnc;

/**
 *
 * @author Antonio
 */
public interface PathFinder {
    
    public Path findPath(Mover mover, int startX, int startY, int targetX, int targetY);
}
