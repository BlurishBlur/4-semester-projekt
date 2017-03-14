package rpg.common.entities;

import java.io.Serializable;
import java.util.UUID;
import rpg.common.util.Vector;

public class Entity implements Serializable {
    
    private final UUID ID = UUID.randomUUID();
    private EntityType type;
    private Vector roomPosition;
    private Vector velocity;
    private Vector worldPosition;
    private float movementSpeed;
    private int currentHealth;
    private int maxHealth;
    private float actionTimer = 2;
    private double verticalMovementChance;
    private double horizontalMovementChance;
    private float direction;
    private float width;
    private float height;
    private boolean canMove = true;
    private String spritePath;
    
    public Entity() {
        roomPosition = new Vector();
        velocity = new Vector();
        worldPosition = new Vector();
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
    
    public Vector getWorldPosition() {
        return worldPosition;
    }

    public Vector getRoomPosition() {
        return roomPosition;
    }
    
    public Vector getVelocity() {
        return velocity;
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
