package rpg.common.events;

import java.io.Serializable;
import rpg.common.entities.Entity;

public class Event implements Serializable {
    
    private final EventType type;
    private final Entity entity;
    
    public Event(EventType type, Entity entity) {
        this.type = type;
        this.entity = entity;
    }

    public EventType getType() {
        return type;
    }

    public Entity getEntity() {
        return entity;
    }
    
    
    
}
