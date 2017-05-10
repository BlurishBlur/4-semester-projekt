package rpg.commonweapon;

import rpg.common.entities.Entity;

public class Weapon extends Entity {
    
    private float attackSpeed;
    private float timeSinceLastAttack;
    private Bullet bullet;
    
    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }
    
    public Bullet getBullet() {
        Bullet newBullet = new Bullet();
        newBullet.setDamage(bullet.getDamage());
        newBullet.setDefaultMovementSpeed(bullet.getDefaultMovementSpeed());
        newBullet.setDefaultDuration(bullet.getDefaultDuration());
        newBullet.setSize(bullet.getWidth(), bullet.getHeight());
        newBullet.setSpritePath(bullet.getSpritePath());
        return newBullet;
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
