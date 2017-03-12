package rpg.common.entities;

import java.io.Serializable;
import java.util.UUID;
import rpg.common.util.Vector;

public class Entity implements Serializable {
    
    private final UUID ID = UUID.randomUUID();
    private EntityType type;
    private Vector position;
    private Vector movement;
    private float movementSpeed;
    private int currentHealth;
    private int maxHealth;
    private float actionTimer = 10;
    private double verticalMovementChance;
    private double horizontalMovementChance;
    private float direction;
    private float width;
    private float height;
    private String spritePath;
    
    public Entity() {
        position = new Vector();
        movement = new Vector();
    }

    public Vector getPosition() {
        return position;
    }
    
    public Vector getMovement() {
        return movement;
    }
    
    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getActionTimer() {
        return actionTimer;
    }

    public void setActionTimer(float actionTimer) {
        this.actionTimer = actionTimer;
    }
    
    public void reduceActionTimer(float deltaTime) {
        actionTimer -= deltaTime;
    }

    public double getVerticalMovementChance() {
        return verticalMovementChance;
    }

    public void setVerticalMovementChance(double verticalMovementChance) {
        this.verticalMovementChance = verticalMovementChance;
    }

    public double getHorizontalMovementChance() {
        return horizontalMovementChance;
    }

    public void setHorizontalMovementChance(double horizontalMovementChance) {
        this.horizontalMovementChance = horizontalMovementChance;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float speed) {
        this.movementSpeed = speed;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
    
    public String getID() {
        return ID.toString();
    }
    
}
