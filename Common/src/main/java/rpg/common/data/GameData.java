package rpg.common.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import rpg.common.events.Event;
import rpg.common.events.EventType;

public class GameData {

    private float deltaTime;
    private int displayWidth;
    private int displayHeight;
    private float cameraZoom;
    private boolean showDebug;
    private List<Event> events = new CopyOnWriteArrayList<>();
    private final GameKeys keys = new GameKeys();

    public boolean isShowDebug() {
        return showDebug;
    }

    public void setShowDebug(boolean showDebug) {
        this.showDebug = showDebug;
    }

    public float getCameraZoom() {
        return cameraZoom;
    }

    public void setCameraZoom(float cameraZoom) {
        this.cameraZoom = cameraZoom;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Event> getEvents(EventType... eventTypes) {
        List<Event> results = new ArrayList<>();
        for (Event event : events) {
            for (EventType eventType : eventTypes) {
                if (event.getType() == eventType) {
                    results.add(event);
                }
            }
        }
        return results;
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public GameKeys getKeys() {
        return keys;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

}
