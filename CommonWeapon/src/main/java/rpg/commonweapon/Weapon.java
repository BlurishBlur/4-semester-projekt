package rpg.commonweapon;

import rpg.common.entities.Entity;

public class Weapon extends Entity {
    
    private int damage;
    private float attackSpeed;
    private float timeSinceLastAttack;
    
    public int getDamage() {
        return damage;
    }
    
    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    public float getAttackSpeed() {
        return attackSpeed;
    }
    
    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
    
    public void resetTimeSinceLastAttack() {
        timeSinceLastAttack = 0;
    }
    
    public void increaseTimeSinceLastAttack(float deltaTime) {
        timeSinceLastAttack += deltaTime;
    }
    
    public boolean canAttack() {
        return timeSinceLastAttack > attackSpeed;
    }
    
}
