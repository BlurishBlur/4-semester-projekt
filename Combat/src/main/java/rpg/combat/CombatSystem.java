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
import rpg.commonweapon.Weapon;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class CombatSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        if (gameData.getKeys().isPressed(GameKeys.F1)) {
            addTestWeapon(gameData, world);
        }
        checkPlayerWeaponSwing(gameData, world);
        handleWeaponHit(gameData, world);
    }

    private void handleWeaponHit(GameData gameData, World world) {
        for (Event event : gameData.getEvents(EventType.ATTACK)) {
            for (Entity entity : world.getCurrentRoom().getEntities()) {
                if (event.getEntity() != entity && !entity.getClass().equals(Weapon.class) && isHit(event.getEntity(), entity)) {
                    entity.reduceCurrentHealth(((Weapon) event.getEntity().getWeapon()).getDamage());
                    System.out.println("Attackee health: " + entity.getCurrentHealth());
                    gameData.addEvent(new Event(EventType.WEAPON_HIT, event.getEntity().getWeapon()));
                }
            }
            gameData.removeEvent(event);
        }
    }

    private boolean isHit(Entity attacker, Entity attackee) {
        Entity weapon = attacker.getWeapon();
        float a = weapon.getRoomPosition().getX() - attackee.getRoomPosition().getX();
        float b = weapon.getRoomPosition().getY() - attackee.getRoomPosition().getY();

        double c = Math.sqrt((double) (a * a) + (double) (b * b));

        return c < weapon.getWidth() / 2 + attackee.getWidth() / 2;

        //return Math.sqrt(Math.pow(entity1.getX() - entity2.getX(), 2) + Math.pow(entity1.getY() - entity2.getY(), 2)) < entity1.getRadius() + entity2.getRadius();
    }

    private void checkPlayerWeaponSwing(GameData gameData, World world) {
        Entity player = world.getPlayer();
        Weapon weapon = (Weapon) player.getWeapon();
        if (player.hasWeapon() && !weapon.canAttack()) {
            weapon.increaseTimeSinceLastAttack(gameData.getDeltaTime());
        }
        if (gameData.getKeys().isPressed(GameKeys.UP)) {
            attack(weapon, player, new Vector(0, player.getHeight() / 2), 0, gameData);
        }
        else if (gameData.getKeys().isPressed(GameKeys.DOWN)) {
            attack(weapon, player, new Vector(0, -player.getHeight() / 2), 180, gameData);
        }
        else if (gameData.getKeys().isPressed(GameKeys.LEFT)) {
            attack(weapon, player, new Vector(-player.getWidth() / 2, 0), 90, gameData);
        }
        else if (gameData.getKeys().isPressed(GameKeys.RIGHT)) {
            attack(weapon, player, new Vector(player.getWidth() / 2, 0), 270, gameData);
        }
    }

    private void attack(Weapon weapon, Entity player, Vector vector, int direction, GameData gameData) { //TODO gør den her metode pænere
        try {
            if (weapon.canAttack()) {
                weapon.getRoomPosition().set(player.getRoomPosition().plus(vector));
                weapon.setDirection(direction);
                weapon.resetTimeSinceLastAttack();
                gameData.addEvent(new Event(EventType.ATTACK, player));
                gameData.addEvent(new Event(EventType.WEAPON_USE, weapon));
            }
        }
        catch (NullPointerException e) {
            Logger.log("No weapon equipped.");
        }
    }

    private void addTestWeapon(GameData gameData, World world) {
        Entity player = world.getPlayer();
        Weapon weapon = new Weapon();
        weapon.setDefaultMovementSpeed(0);
        weapon.setWidth(player.getWidth());
        weapon.setHeight(player.getHeight());
        weapon.getRoomPosition().set(player.getRoomPosition());
        weapon.setSpritePath("rpg/gameengine/sword.png");
        weapon.setDamage(10);
        weapon.setAttackSpeed(2);
        weapon.getSounds().put("HIT", "rpg/gameengine/stabsound.wav");
        weapon.getSounds().put("MISS", "rpg/gameengine/woosh.mp3");
        player.setWeapon(weapon);
        System.out.println("Added weapon to player");
    }

}
