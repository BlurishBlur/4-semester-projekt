package rpg.combat;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.world.World;
import rpg.common.entities.Entity;
import rpg.common.events.Event;
import rpg.common.events.EventType;
import rpg.common.services.IEntityProcessingService;
import rpg.common.util.Logger;
import rpg.common.util.Vector;
import rpg.commonweapon.Bullet;
import rpg.commonweapon.Weapon;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class CombatSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for(Entity entity : world.getCurrentRoom().getEntities(Bullet.class)) {
            Bullet bullet = (Bullet) entity;
            bullet.getVelocity().set(bullet.getDefaultVelocity());
            bullet.reduceCurrentDuration(gameData.getDeltaTime());
            if(bullet.getCurrentDuration() < 0) {
                world.getCurrentRoom().removeEntity(entity);
                gameData.removeEvent(gameData.getEvent(entity));
            }
        }
        if (world.getPlayer() != null) {
            if (world.getPlayer().getWeapon() == null) {
                addSword(world);
            }
            if (gameData.getKeys().isPressed(GameKeys.F1)) {
                addGun(world);
            }
            checkPlayerWeaponSwing(gameData, world);
            handleWeaponHit(gameData, world);
        }
    }

    private void handleWeaponHit(GameData gameData, World world) {
        for (Event event : gameData.getEvents(EventType.ATTACK)) {
            for (Entity entity : world.getCurrentRoom().getEntities()) {
                if (event.getEntity() != entity && entity.hasHpBar() && isHit(event.getEntity(), entity)) {
                    entity.reduceCurrentHealth(((Bullet) event.getEntity()).getDamage());
                    System.out.println("Attackee health: " + entity.getCurrentHealth());
                    gameData.addEvent(new Event(EventType.WEAPON_HIT, event.getEntity()));
                    world.getCurrentRoom().removeEntity(event.getEntity());
                    gameData.removeEvent(event);
                    break;
                }
            }
        }
    }

    private boolean isHit(Entity bullet, Entity attackee) {
        float a = bullet.getRoomPosition().getX() - attackee.getRoomPosition().getX();
        float b = bullet.getRoomPosition().getY() - attackee.getRoomPosition().getY();

        double c = Math.sqrt((double) (a * a) + (double) (b * b));

        return c < bullet.getWidth() / 2 + attackee.getWidth() / 2;
    }

    private void checkPlayerWeaponSwing(GameData gameData, World world) {
        Entity player = world.getPlayer();
        Weapon weapon = (Weapon) player.getWeapon();
        if (player.hasWeapon() && !weapon.canAttack()) {
            weapon.increaseTimeSinceLastAttack(gameData.getDeltaTime());
        }
        if (gameData.getKeys().isPressed(GameKeys.UP)) {
            attack(weapon, new Vector(player.getRoomPosition().getX(), player.getRoomPosition().getY() + player.getHeight()), new Vector(0, 1), gameData, world);
        }
        else if (gameData.getKeys().isPressed(GameKeys.DOWN)) {
            attack(weapon, new Vector(player.getRoomPosition().getX(), player.getRoomPosition().getY() - player.getHeight()), new Vector(0, -1), gameData, world);
        }
        else if (gameData.getKeys().isPressed(GameKeys.LEFT)) {
            attack(weapon, new Vector(player.getRoomPosition().getX() - player.getWidth(), player.getRoomPosition().getY()), new Vector(-1, 0), gameData, world);
        }
        else if (gameData.getKeys().isPressed(GameKeys.RIGHT)) {
            attack(weapon, new Vector(player.getRoomPosition().getX() + player.getWidth(), player.getRoomPosition().getY()), new Vector(1, 0), gameData, world);
        }
    }

    private void attack(Weapon weapon, Vector position, Vector velocity, GameData gameData, World world) { //TODO gør den her metode pænere
        try {
            if (weapon.canAttack()) {
                Bullet bullet = weapon.getBullet();
                bullet.getRoomPosition().set(position);
                velocity.scalar(bullet.getDefaultMovementSpeed());
                bullet.getDefaultVelocity().set(velocity);
                bullet.setDirection(velocity.getAngle());
                bullet.setCurrentDuration(bullet.getDefaultDuration());
                weapon.resetTimeSinceLastAttack();
                world.getCurrentRoom().addEntity(bullet);
                gameData.addEvent(new Event(EventType.ATTACK, bullet));
                gameData.addEvent(new Event(EventType.WEAPON_USE, weapon));
            }
        }
        catch (NullPointerException e) {
            Logger.log("No weapon equipped.");
        }
    }

    private void addGun(World world) {
        Entity player = world.getPlayer();
        Weapon gun = new Weapon();
        gun.setWidth(20);
        gun.setHeight(20);
        gun.getRoomPosition().set(player.getRoomPosition());
        gun.setSpritePath("rpg/gameengine/empty.png");
        gun.setAttackSpeed(0.5f);
        gun.getSounds().put("HIT", "rpg/gameengine/punch.mp3");
        gun.getSounds().put("MISS", "rpg/gameengine/woosh.mp3");
        
        Bullet bullet = new Bullet();
        bullet.setDamage(7);
        bullet.setDefaultMovementSpeed(400);
        bullet.setDefaultDuration(4);
        bullet.setSize(5, 5);
        bullet.setSpritePath("rpg/gameengine/bullet.png");
        
        gun.setBullet(bullet);
        player.setWeapon(gun);
        System.out.println("Added weapon to player");
    }

    private void addSword(World world) {
        Entity player = world.getPlayer();
        Weapon sword = new Weapon();
        sword.setWidth(player.getWidth());
        sword.setHeight(player.getHeight());
        sword.getRoomPosition().set(player.getRoomPosition());
        sword.setSpritePath("rpg/gameengine/sword.atlas");
        sword.setAttackSpeed(2);
        sword.setCurrentFrame(1);
        sword.setMaxFrames(4);
        sword.getSounds().put("HIT", "rpg/gameengine/stabsound.wav");
        sword.getSounds().put("MISS", "rpg/gameengine/woosh.mp3");
        
        Bullet attack = new Bullet();
        attack.setDamage(10);
        attack.setDefaultMovementSpeed(0);
        attack.setDefaultDuration(0);
        attack.setSize(10, 10);
        attack.setSpritePath("rpg/gameengine/empty.png");
        
        sword.setBullet(attack);
        player.setWeapon(sword);
        System.out.println("Added weapon to player");
    }

}
