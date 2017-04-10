package rpg.common.world;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
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
        
        List<Vector> polygon = asList(new Vector(0, 0), 
                                new Vector(1280, 0), 
                                new Vector(1280, 325),
                                new Vector(814, 325),
                                new Vector(787, 212), 
                                new Vector(722, 117), 
                                new Vector(636, 41), 
                                new Vector(588, 24),
                                new Vector(488, 5), 
                                new Vector(346, 25), 
                                new Vector(245, 92), 
                                new Vector(178, 203),
                                new Vector(148, 315),
                                new Vector(117, 476),
                                new Vector(245, 576),
                                new Vector(363, 642),
                                new Vector(493, 663),
                                new Vector(662, 616),
                                new Vector(752, 530),
                                new Vector(805, 403),
                                new Vector(1280, 404),
                                new Vector(1280, 720),
                                new Vector(0, 720),
                                new Vector(0, 0)
        );
        List<List<Vector>> polygons1 = new ArrayList<>();
        polygons1.add(polygon);
        
        r1.setCollidables(polygons1);
        
        world[0][1] = r1;
        Room r2 = new Room("rpg/gameengine/Map4.png");
        r2.canExitLeft(true);
        r2.canExitUp(true);
        
        List<Vector> polygon1 = asList(new Vector(0, 380), 
                                new Vector(76, 380), 
                                new Vector(76, 407),
                                new Vector(128, 407),
                                new Vector(154, 471), 
                                new Vector(186, 535), 
                                new Vector(192, 720));
        List<Vector> polygon2 = asList(new Vector(0, 314),
                                new Vector(67, 314), 
                                new Vector(67, 298), 
                                new Vector(0, 298), 
                                new Vector(0, 224),
                                new Vector(36, 224),
                                new Vector(46, 226),
                                new Vector(118, 266),
                                new Vector(150, 181),
                                new Vector(174, 72),
                                new Vector(144, 31),
                                new Vector(136, 27),
                                new Vector(136, 1),
                                new Vector(0, 0)
        );
        List<List<Vector>> polygons2 = new ArrayList<>();
        polygons2.add(polygon1);
        polygons2.add(polygon2);
        r2.setCollidables(polygons2);
        
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
