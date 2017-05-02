/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.astarcnc;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.services.IEntityProcessingService;
import rpg.common.world.World;
import rpg.common.entities.Entity;
import rpg.common.world.Room;
import rpg.commonenemy.Enemy;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class AStarControlSystem implements IEntityProcessingService {

    private Room currentRoom;
    private Node[][] nodes;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getCurrentRoom().getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) entity;
            AStarPathFinder pathFinder;
            if (currentRoom != null && nodes != null && world.getCurrentRoom().equals(currentRoom)) {
                pathFinder = new AStarPathFinder(currentRoom, nodes, (int) enemy.getWidth(), true);
                System.out.println("opretter IKKE nye");
            }
            else {
                System.out.println("opretter nye");
                currentRoom = world.getCurrentRoom();
                nodes = new Node[currentRoom.getWidth()][currentRoom.getHeight()];
                int minX = (int) Math.min(enemy.getRoomPosition().getX(), world.getPlayer().getRoomPosition().getX());
                int maxX = (int) Math.max(enemy.getRoomPosition().getX(), world.getPlayer().getRoomPosition().getX()) + (int) enemy.getWidth();
                int minY = (int) Math.min(enemy.getRoomPosition().getY(), world.getPlayer().getRoomPosition().getY());
                int maxY = (int) Math.max(enemy.getRoomPosition().getY(), world.getPlayer().getRoomPosition().getY()) + (int) enemy.getHeight();
                //nodes = new Node[(int) enemy.getWidth() * 2][(int) enemy.getHeight() * 2];
                for (int x = minX; x < maxX; x++) {
                    for (int y = minY; y < maxY; y++) {
                        nodes[x][y] = new Node(x, y);
                    }
                }
                pathFinder = new AStarPathFinder(currentRoom, nodes, (int) enemy.getWidth(), true);
            }
            
            Path path = pathFinder.findPath(enemy, (int) enemy.getRoomPosition().getX(),
                    (int) enemy.getRoomPosition().getY(), (int) world.getPlayer().getRoomPosition().getX(),
                    (int) world.getPlayer().getRoomPosition().getY());

            enemy.setNextStep(path.getStep(0));
        }
    }

}
