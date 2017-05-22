package rpg.common.world;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Scanner;
import rpg.common.entities.Entity;
import rpg.common.exceptions.FileFormatException;
import rpg.common.util.Logger;
import rpg.common.util.Polygon;
import rpg.common.util.Vector;

public class World implements Serializable {

    
    private Room[][] world;
    private Room currentRoom;
    private Entity player;
    public static final int WORLD_WIDTH = 4;
    public static final int WORLD_HEIGHT = 3;
    public static final int WORLD_SIZE = 10;
    public static int SCALE = 20;

    public World() {
        world = new Room[WORLD_WIDTH][WORLD_HEIGHT];
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
    
    public void setRoom(Vector roomPosition, Room room) {
        world[(int) roomPosition.getX()][(int) roomPosition.getY()] = room;
    }

    public Entity getPlayer() {
        return player;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

}
