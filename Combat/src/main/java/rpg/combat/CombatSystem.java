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
            for(Entity entity : world.getCurrentRoom().getEntities()) {
                if(event.getEntity() != entity && !entity.getClass().equals(Weapon.class) && isHit(event.getEntity(), entity)) {
                    entity.reduceCurrentHealth(((Weapon) event.getEntity().getWeapon()).getDamage());
                    System.out.println("hit");
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
        if (gameData.getKeys().isPressed(GameKeys.UP)) {
            gameData.getWeapon(player).getRoomPosition().set(player.getRoomPosition().plus(new Vector(0, player.getHeight() / 2)));
            gameData.getWeapon(player).setDirection(0);
            gameData.addEvent(new Event(EventType.ATTACK, player));
        }
        else if (gameData.getKeys().isPressed(GameKeys.DOWN)) {
            gameData.getWeapon(player).getRoomPosition().set(player.getRoomPosition().plus(new Vector(0, -player.getHeight() / 2)));
            gameData.getWeapon(player).setDirection(180);
            gameData.addEvent(new Event(EventType.ATTACK, player));
        }
        else if (gameData.getKeys().isPressed(GameKeys.LEFT)) {
            gameData.getWeapon(player).getRoomPosition().set(player.getRoomPosition().plus(new Vector(-player.getWidth() / 2, 0)));
            gameData.getWeapon(player).setDirection(90);
            gameData.addEvent(new Event(EventType.ATTACK, player));
        }
        else if (gameData.getKeys().isPressed(GameKeys.RIGHT)) {
            gameData.getWeapon(player).getRoomPosition().set(player.getRoomPosition().plus(new Vector(player.getWidth() / 2, 0)));
            gameData.getWeapon(player).setDirection(270);
            gameData.addEvent(new Event(EventType.ATTACK, player));
        }
    }
    
    private void addTestWeapon(GameData gameData, World world) {
        Entity player = world.getPlayer();
        Entity weapon = new Entity();
        weapon.setDefaultMovementSpeed(0);
        weapon.setWidth(player.getWidth());
        weapon.setHeight(player.getHeight());
        weapon.getRoomPosition().set(player.getRoomPosition());
        weapon.setSpritePath("rpg/gameengine/sword.png");
        gameData.addWeapon(player, weapon);
        world.getCurrentRoom().addEntity(weapon);
        System.out.println("Added weapon to player");
    }

}
