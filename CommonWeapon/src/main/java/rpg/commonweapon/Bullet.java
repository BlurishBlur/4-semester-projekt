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
    
}
