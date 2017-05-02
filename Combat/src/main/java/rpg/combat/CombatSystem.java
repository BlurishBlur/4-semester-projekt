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
        }
        if (world.getPlayer() != null) {
            if (world.getPlayer().getWeapon() == null) {
                addHand(world);
            }
            if (gameData.getKeys().isPressed(GameKeys.F1)) {
                addSword(gameData, world);
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

        //return Math.sqrt(Math.pow(entity1.getX() - entity2.getX(), 2) + Math.pow(entity1.getY() - entity2.getY(), 2)) < entity1.getRadius() + entity2.getRadius();
    }

    private void checkPlayerWeaponSwing(GameData gameData, World world) {
        Entity player = world.getPlayer();
        Weapon weapon = (Weapon) player.getWeapon();
        if (player.hasWeapon() && !weapon.canAttack()) {
            weapon.increaseTimeSinceLastAttack(gameData.getDeltaTime());
        }
        if (gameData.getKeys().isPressed(GameKeys.UP)) {
            //attack(weapon, player, new Vector(0, player.getHeight() / 2), 0, gameData);
            //attack(weapon, player, bullet, gameData);
            attack(weapon, new Vector(5, 5), new Vector(player.getRoomPosition().getX(), player.getRoomPosition().getY() + player.getHeight()), new Vector(0, 250), 0, gameData, world);
        }
        else if (gameData.getKeys().isPressed(GameKeys.DOWN)) {
            //attack(weapon, player, new Vector(0, -player.getHeight() / 2), 180, gameData);
            attack(weapon, new Vector(5, 5), new Vector(player.getRoomPosition().getX(), player.getRoomPosition().getY() - player.getHeight()), new Vector(0, -250), 180, gameData, world);
        }
        else if (gameData.getKeys().isPressed(GameKeys.LEFT)) {
            //attack(weapon, player, new Vector(-player.getWidth() / 2, 0), 90, gameData);
            attack(weapon, new Vector(5, 5), new Vector(player.getRoomPosition().getX() - player.getWidth(), player.getRoomPosition().getY()), new Vector(-250, 0), 90, gameData, world);
        }
        else if (gameData.getKeys().isPressed(GameKeys.RIGHT)) {
            //attack(weapon, player, new Vector(player.getWidth() / 2, 0), 270, gameData);
            attack(weapon, new Vector(5, 5), new Vector(player.getRoomPosition().getX() + player.getWidth(), player.getRoomPosition().getY()), new Vector(250, 0), 270, gameData, world);
        }
    }

    private void attack(Weapon weapon, Vector size, Vector position, Vector velocity, int direction, GameData gameData, World world) { //TODO gør den her metode pænere
        try {
            if (weapon.canAttack()) {
                Bullet bullet = new Bullet();
                bullet.setDamage(weapon.getDamage());
                bullet.setSpritePath("rpg/gameengine/pink_dot.png");
                System.out.println("ny bullet");
                bullet.setSize(size.getX(), size.getY());
                bullet.getRoomPosition().set(position);
                bullet.getDefaultVelocity().set(velocity);
                System.out.println(velocity);
                bullet.setDirection(direction);
                //weapon.getRoomPosition().set(player.getRoomPosition().plus(vector));
                //weapon.setDirection(direction);
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

    private void addHand(World world) {
        Entity player = world.getPlayer();
        Weapon weapon = new Weapon();
        weapon.setDefaultMovementSpeed(0);
        weapon.setWidth(20);
        weapon.setHeight(20);
        weapon.getRoomPosition().set(player.getRoomPosition());
        weapon.setSpritePath("rpg/gameengine/hand.png");
        weapon.setDamage(2);
        weapon.setAttackSpeed(0.5f);
        weapon.getSounds().put("HIT", "rpg/gameengine/punch.mp3");
        weapon.getSounds().put("MISS", "rpg/gameengine/woosh.mp3");
        player.setWeapon(weapon);
        System.out.println("Added weapon to player");
    }

    private void addSword(GameData gameData, World world) {
        Entity player = world.getPlayer();
        Weapon weapon = new Weapon();
        weapon.setDefaultMovementSpeed(0);
        weapon.setWidth(player.getWidth());
        weapon.setHeight(player.getHeight());
        weapon.getRoomPosition().set(player.getRoomPosition());
        weapon.setSpritePath("rpg/gameengine/sword.atlas");
        weapon.setDamage(10);
        weapon.setAttackSpeed(2);
        weapon.setCurrentFrame(1);
        weapon.setMaxFrames(4);
        weapon.getSounds().put("HIT", "rpg/gameengine/stabsound.wav");
        weapon.getSounds().put("MISS", "rpg/gameengine/woosh.mp3");
        player.setWeapon(weapon);
        System.out.println("Added weapon to player");
    }

}
