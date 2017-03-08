package rpg.common.events;

public class Event {
    
    private final EventType type;
    private final String entityID;
    
    public Event(EventType type, String entityID) {
        this.type = type;
        this.entityID = entityID;
    }

    public EventType getType() {
        return type;
    }

    public String getEntityID() {
        return entityID;
    }
    
    
    
}
