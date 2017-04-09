package rpg.common.world;

import rpg.common.entities.Entity;
import rpg.common.util.Vector;

public class World {

    private final int WORLD_WIDTH = 2;
    private final int WORLD_HEIGHT = 3;
    private Room[][] world;
    private Room currentRoom;
    private Entity player;
    
    public World() {
        world = new Room[WORLD_WIDTH][WORLD_HEIGHT];
        Room r1 = new Room("rpg/gameengine/SafeHouse.png");
        r1.canExitRight(true);
        world[0][1] = r1;
        Room r2 = new Room("rpg/gameengine/Map4.png");
        r2.canExitLeft(true);
        r2.canExitUp(true);
        world[1][1] = r2;
        Room r3 = new Room("rpg/gameengine/Map1.png");
        r3.canExitDown(true);
        world[1][2] = r3;
        
        currentRoom = world[0][1];
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
        return player;
    }
    
    public void setPlayer(Entity player) {
        this.player = player;
    }
    
}
