package rpg.common.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;

public class Room {
    
    private final Map<String, Entity> entities = new ConcurrentHashMap<>();
    private int width = 1280;
    private int height = 720;
    private boolean canExitUp;
    private boolean canExitDown;
    private boolean canExitLeft;
    private boolean canExitRight;
    private String spritePath;
    
    public Room(String spritePath) {
        this.spritePath = spritePath;
    }
    
    public void canExitUp(boolean canExitUp) {
        this.canExitUp = canExitUp;
    }
    
    public void canExitDown(boolean canExitDown) {
        this.canExitDown = canExitDown;
    }
    
    public void canExitLeft(boolean canExitLeft) {
        this.canExitLeft = canExitLeft;
    }
    
    public void canExitRight(boolean canExitRight) {
        this.canExitRight = canExitRight;
    }
    
    public boolean canExitUp() {
        return canExitUp;
    }
    
    public boolean canExitDown() {
        return canExitDown;
    }
    
    public boolean canExitLeft() {
        return canExitLeft;
    }
    
    public boolean canExitRight() {
        return canExitRight;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }
    
    
    
}
