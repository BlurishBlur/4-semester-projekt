package rpg.common.util;

import rpg.common.entities.Entity;

public class Message {

    private final String message;
    private float duration;
    private float startDuration;
    private float x;
    private float y;
    private float alpha;
    private Entity anchor;

    public Message(String message, float duration, float x, float y) {
        this.message = message;
        this.duration = duration;
        startDuration = duration;
        this.x = x;
        this.y = y;
        alpha = 0;
    }
    
    public Message(String message, float duration, Entity anchor) {
        this(message, duration, anchor.getRoomPosition().getX(), anchor.getRoomPosition().getY());
        this.anchor = anchor;
    }

    public String getMessage() {
        return message;
    }

    public float getDuration() {
        return duration;
    }

    public float getX() {
        float xOffset = 0;
        if(anchor != null) {
            xOffset = -anchor.getWidth() / 2;
        }
        return x + xOffset;
    }

    public float getY() {
        float yOffset = 0;
        if(anchor != null) {
            yOffset = (alpha * anchor.getHeight());
        }
        return y + yOffset;
    }

    public void reduceDuration(float deltaTime) {
        duration -= deltaTime;
    }
    
    public float getStartDuration() {
        return startDuration;
    }
    
    public float getAndIncreaseAlpha(float deltaTime) {
        return alpha += deltaTime;
    }
    
    public float getAndDecreaseAlpha(float deltaTime) {
        return alpha -= deltaTime;
    }
    
    public boolean hasAnchor() {
        return anchor != null;
    }

}
