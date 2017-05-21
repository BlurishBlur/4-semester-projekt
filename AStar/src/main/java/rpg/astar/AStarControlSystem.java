/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.astar;

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
    private PathFinder pathFinder;
    private Path path;
    private int k = 0;

    @Override
    public void process(GameData gameData, World world) {
        if (world.getPlayer() != null) {
            for (Entity entity : world.getCurrentRoom().getEntities(Enemy.class)) {
                Enemy enemy = (Enemy) entity;
                if (currentRoom == null && nodes == null && !world.getCurrentRoom().equals(currentRoom)) {

                    currentRoom = world.getCurrentRoom();
                    nodes = new Node[currentRoom.getWidth() / World.SCALE][currentRoom.getHeight() / World.SCALE];

                    for (int x = 0; x < currentRoom.getWidth() / World.SCALE; x++) {
                        for (int y = 0; y < currentRoom.getHeight() / World.SCALE; y++) {
                            nodes[x][y] = new Node(x, y);
                            nodes[x][y].setBlocked(currentRoom.isNodeBlocked(x * World.SCALE, y * World.SCALE));
                        }
                    }
                    pathFinder = new AStarPathFinder(currentRoom, nodes, 1000, true);
                }

                path = pathFinder.findPath(enemy, (int) enemy.getRoomPosition().getX() / World.SCALE,
                        (int) enemy.getRoomPosition().getY() / World.SCALE, (int) world.getPlayer().getRoomPosition().getX() / World.SCALE,
                        (int) world.getPlayer().getRoomPosition().getY() / World.SCALE);

                if (path != null) {
                    enemy.setNextStep(path.getStep(1));
                }
            }
        }
    }
}
