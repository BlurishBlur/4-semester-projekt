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
    private PathFinder pathFinder;
    private Path path;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getCurrentRoom().getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) entity;
            //AStarPathFinder pathFinder;
            if (currentRoom != null && nodes != null && world.getCurrentRoom().equals(currentRoom)) {
                //pathFinder = new AStarPathFinder(currentRoom, nodes, (int) enemy.getWidth(), true);
                //System.out.println("opretter IKKE nye");
            } else {
                //System.out.println("opretter nye");
                currentRoom = world.getCurrentRoom();
                nodes = new Node[currentRoom.getWidth() / 20][currentRoom.getHeight() / 20];
                /*int minX = 0;
                int minY = 0;
                int maxX = world.getCurrentRoom().getWidth();
                int maxY = world.getCurrentRoom().getHeight();*/
                int minX = (int) Math.min(enemy.getRoomPosition().getX() - 1, world.getPlayer().getRoomPosition().getX() - 1);
                int maxX = (int) Math.max(enemy.getRoomPosition().getX() + 1, world.getPlayer().getRoomPosition().getX()) + (int) enemy.getWidth() + 1;
                int minY = (int) Math.min(enemy.getRoomPosition().getY() - 1, world.getPlayer().getRoomPosition().getY() - 1);
                int maxY = (int) Math.max(enemy.getRoomPosition().getY() + 1, world.getPlayer().getRoomPosition().getY()) + (int) enemy.getHeight() + 1;
                //nodes = new Node[(int) enemy.getWidth() * 2][(int) enemy.getHeight() * 2];
                for (int x = 0; x < currentRoom.getWidth() / 20; x++) {
                    for (int y = 0; y < currentRoom.getHeight() / 20; y++) {
                        nodes[x][y] = new Node(x, y);
                        //System.out.println(x + " " + y);
                    }
                }
                pathFinder = new AStarPathFinder(currentRoom, nodes, 2000, false);
            }
            if(path == null) {
            path = pathFinder.findPath(enemy, (int) enemy.getRoomPosition().getX() / 20,
                    (int) enemy.getRoomPosition().getY() / 20, (int) world.getPlayer().getRoomPosition().getX() / 20,
                    (int) world.getPlayer().getRoomPosition().getY() / 20);

            //System.out.println("Player position: " + (int)world.getPlayer().getRoomPosition().getX() / 20 + ", " + (int)world.getPlayer().getRoomPosition().getY() / 20);
            //System.out.println("Enemy position: " + (int)enemy.getRoomPosition().getX() / 20 + ", " + (int)enemy.getRoomPosition().getY() / 20);
            if (path != null) {
                enemy.setNextStep(path.getStep(1));

                for (int i = 0; i < path.getLength(); i++) {
                    System.out.println(path.getStep(i).getX() + ", " + path.getStep(i).getY());
                    
                }
                System.out.println("");
            }
            }
        }
    }

}
