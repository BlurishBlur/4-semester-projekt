package rpg.commonweapon;

import rpg.common.entities.Entity;

public class Weapon extends Entity {
    
    private int damage;
    
    public int getDamage() {
        return damage;
    }
    
    public void setDamage(int damage) {
        this.damage = damage;
    }
    
}
