package rpg.common.events;

import rpg.common.entities.Entity;

public class Event {
    
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
