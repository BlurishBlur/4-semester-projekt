package rpg.common.data;

import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    private final Map<String, Entity> entities = new ConcurrentHashMap<>();

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

    public Entity getEntity(EntityType entityType) {
        return entities.values()
                .parallelStream()
                .filter(entity -> entity.getType() == entityType)
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
