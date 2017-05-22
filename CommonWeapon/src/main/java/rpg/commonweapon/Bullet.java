package rpg.commonweapon;

import rpg.common.entities.Entity;
import rpg.common.util.Vector;

/**
 *
 * @author Niels
 */
public class Bullet extends Entity {
    
    private int damage;
    private Vector defaultVelocity;
    private float defaultDuration;
    private float currentDuration;
            
    public Bullet() {
        defaultVelocity = new Vector();
    }
    
    public Vector getDefaultVelocity() {
        return defaultVelocity;
    }
    
    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public float getDefaultDuration() {
        return defaultDuration;
    }
    
    public void setDefaultDuration(float defaultDuration) {
        this.defaultDuration = defaultDuration;
    }
    
    public void setCurrentDuration(float currentDuration) {
        this.currentDuration = currentDuration;
    }
    
    public float getCurrentDuration() {
        return currentDuration;
    }
    
    public void reduceCurrentDuration(float deltaTime) {
        this.currentDuration -= deltaTime;
    }
    
}
