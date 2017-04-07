package rpg.common.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import rpg.common.util.Vector;

public class Entity implements Serializable {
    
    private final UUID ID = UUID.randomUUID();
    private Vector roomPosition;
    private Vector velocity;
    private Vector worldPosition;
    private Vector worldVelocity;
    private float currentMovementSpeed;
    private float defaultMovementSpeed;
    private float movementSpeedModifier;
    private float sprintModifier;
    private int currentHealth;
    private int maxHealth;
    private int skillPoints;
    private float actionTimer = 2;
    private int armor;
    private double verticalMovementChance;
    private double horizontalMovementChance;
    private float direction;
    private float width;
    private float height;
    private String spritePath;
    private boolean isAnimatable;
    private float currentFrame;
    private int maxFrames;
    private Map<String, String> sounds;
    private Entity weapon;
    private boolean hasHpBar;
    private int currency;
    private int level;
    private int experience;
    
    public Entity() {
        roomPosition = new Vector();
        velocity = new Vector();
        worldPosition = new Vector();
        worldVelocity = new Vector();
        sounds = new ConcurrentHashMap<>();
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public void setExperience(int experience) {
        this.experience = experience;
    }
    
    public int getLevel() {
        return level;
    }

    public void advanceLevel() {
        level++;
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience(int experience) {
        this.experience += experience;
    }
    
    public void addCurrency(int currency) {
        this.currency += currency;
    }
    
    public int getCurrency() {
        return currency;
    }
    
    public boolean hasWeapon() {
        return weapon != null;
    }
    
    public Entity getWeapon() {
        return weapon;
    }
    
    public void setWeapon(Entity weapon) {
        this.weapon = weapon;
    }
    
    public boolean isAnimatable() {
        return isAnimatable;
    }
    
    public boolean hasHpBar() {
        return hasHpBar;
    }
    
    public int getArmor() {
        return this.armor;
    }
    
    public void setArmor(int armor) {
        this.armor = armor;
    }
    
    public int getSkillPoints() {
        return skillPoints;
    }
    
    public void setSkillPoints(int amount) {
        this.skillPoints = amount;
        sounds = new HashMap<>();
    }
    
    public void increaseFrame(float deltaTime) {
        currentFrame += deltaTime * (currentMovementSpeed / (width / 3)); //enten width / 3 eller width / 4
        if(currentFrame > maxFrames + 1) {
            currentFrame = 2;
        }
        //currentFrame = (currentFrame % maxFrames) + 1;
    }
    
    public Map<String, String> getSounds() {
        return sounds;
    }

    public float getDefaultMovementSpeed() {
        return defaultMovementSpeed;
    }

    public void setDefaultMovementSpeed(float defaultMovementSpeed) {
        this.defaultMovementSpeed = defaultMovementSpeed;
    }

    public float getMovementSpeedModifier() {
        return movementSpeedModifier;
    }

    public void setMovementSpeedModifier(float movementSpeedModifier) {
        this.movementSpeedModifier = movementSpeedModifier;
    }
    
    public Vector getWorldVelocity() {
        return worldVelocity;
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
        isAnimatable = spritePath.substring(spritePath.length() - 6, spritePath.length()).equals(".atlas");
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }
    
    public void reduceCurrentHealth(int damage) {
        float damageReduction = (armor / 10.0f) / 100.0f;
        if((int) damageReduction == 0) {
            damageReduction = 1;
        }
        currentHealth -= damage * damageReduction;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        if(maxHealth > 1) {
            hasHpBar = true;
        }
    }

    public float getCurrentMovementSpeed() {
        return currentMovementSpeed;
    }

    public void setCurrentMovementSpeed(float currentMovementSpeed) {
        this.currentMovementSpeed = currentMovementSpeed;
    }

    public float getSprintModifier() {
        return sprintModifier;
    }

    public void setSprintModifier(float sprintModifier) {
        this.sprintModifier = sprintModifier;
    }
    
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
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
    
    public int getCurrentFrame(){
        return (int) currentFrame;
    }
    
    public void setCurrentFrame(int frame){
        this.currentFrame = frame;
    }
    
    public int getMaxFrames(){
        return maxFrames;
    }
    
    public void setMaxFrames(int frames){
        this.maxFrames = frames;
    }
}
