package rpg.common.util;

public class Message {

    private final String message;
    private float duration;
    private float x;
    private float y;

    public Message(String message, float duration, float x, float y) {
        this.message = message;
        this.duration = duration;
        this.x = x;
        this.y = y;
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

}
