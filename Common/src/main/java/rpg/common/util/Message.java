package rpg.common.util;

public class Message {

    private final String message;
    private float duration;
    private float startDuration;
    private float x;
    private float y;
    private float alpha;

    public Message(String message, float duration, float x, float y) {
        this.message = message;
        this.duration = duration;
        startDuration = duration;
        this.x = x;
        this.y = y;
        alpha = 0;
    }

    public String getMessage() {
        return message;
    }

    public float getDuration() {
        return duration;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
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

}
