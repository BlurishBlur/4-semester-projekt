package rpg.common.world;

import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import rpg.common.util.Vector;

public class World {

    private final int WORLD_WIDTH = 2;
    private final int WORLD_HEIGHT = 2;
    private Room[][] world;
    private Room currentRoom;
    private final Map<String, Entity> entities = new ConcurrentHashMap<>();
    
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

    public void addEntity(Entity entity) {
        entities.put(entity.getID(), entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity.getID());
    }

    public void removeEntity(String entityID) {
        entities.remove(entityID);
    }

    public Entity getEntity(String entityID) {
        return entities.get(entityID);
    }

    public Entity getPlayer() {
        return entities.values()
                .parallelStream()
                .filter(entity -> entity.getType() == EntityType.PLAYER)
                .findFirst()
                .get();
    }

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public List<Entity> getEntities(EntityType... entityTypes) {
        List<Entity> results = new ArrayList<>();
        for (Entity entity : entities.values()) {
            for (EntityType entityType : entityTypes) {
                if (entity.getType() == entityType) {
                    results.add(entity);
                }
            }
        }
        return results;
    }

}
