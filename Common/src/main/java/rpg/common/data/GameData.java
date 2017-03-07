package rpg.common.data;

public class GameData {
    
    private float deltaTime;
    private int displayWidth;
    private int displayHeight;
    private final GameKeys keys = new GameKeys();
    
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
