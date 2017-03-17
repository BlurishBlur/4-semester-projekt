package rpg.common.world;

import rpg.common.entities.Entity;
import rpg.common.util.Vector;

public class World {

    private final int WORLD_WIDTH = 2;
    private final int WORLD_HEIGHT = 2;
    private Room[][] world;
    private Room currentRoom;
    
    public World() {
        world = new Room[WORLD_WIDTH][WORLD_HEIGHT];
        Room r1 = new Room("rpg/gameengine/grass.png");
        r1.canExitRight(true);
        r1.canExitUp(true);
        world[0][0] = r1;
        Room r2 = new Room("rpg/gameengine/player.png");
        r2.canExitLeft(true);
        world[1][0] = r2;
        Room r3 = new Room("rpg/gameengine/enemy.png");
        r3.canExitDown(true);
        world[0][1] = r3;
        
        currentRoom = world[0][0];
    }
    
    public void setCurrentRoom(Vector worldPosition) {
        currentRoom = getRoom(worldPosition);
    }
    
    public Room getCurrentRoom() {
        return currentRoom;
    }
    
    public Room getRoom(int x, int y) {
        return world[x][y];
    }
    
    public Room getRoom(Vector worldPosition) {
        return world[(int) worldPosition.getX()][(int) worldPosition.getY()];
    }
    
    public Entity getPlayer() {
        return currentRoom.getPlayer();
    }
    
}
