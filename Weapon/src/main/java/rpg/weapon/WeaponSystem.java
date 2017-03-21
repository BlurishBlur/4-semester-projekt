package rpg.weapon;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.world.World;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.events.Event;
import rpg.common.events.EventType;
import rpg.common.services.IEntityProcessingService;
import rpg.common.util.Vector;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class WeaponSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        Entity player = world.getPlayer();
        if (gameData.getKeys().isPressed(GameKeys.UP)) {
            gameData.getWeapon(player).getRoomPosition().set(player.getRoomPosition().plus(new Vector(0, player.getHeight() / 2)));
            gameData.getWeapon(player).setDirection(0);
            gameData.addEvent(new Event(EventType.PLAYER_ATTACK, player));
        }
        else if (gameData.getKeys().isPressed(GameKeys.DOWN)) {
            gameData.getWeapon(player).getRoomPosition().set(player.getRoomPosition().plus(new Vector(0, -player.getHeight() / 2)));
            gameData.getWeapon(player).setDirection(180);
            gameData.addEvent(new Event(EventType.PLAYER_ATTACK, player));
        }
        else if (gameData.getKeys().isPressed(GameKeys.LEFT)) {
            gameData.getWeapon(player).getRoomPosition().set(player.getRoomPosition().plus(new Vector(-player.getWidth() / 2, 0)));
            gameData.getWeapon(player).setDirection(90);
            gameData.addEvent(new Event(EventType.PLAYER_ATTACK, player));
        }
        else if (gameData.getKeys().isPressed(GameKeys.RIGHT)) {
            gameData.getWeapon(player).getRoomPosition().set(player.getRoomPosition().plus(new Vector(player.getWidth() / 2, 0)));
            gameData.getWeapon(player).setDirection(270);
            gameData.addEvent(new Event(EventType.PLAYER_ATTACK, player));
        }

        if (gameData.getKeys().isPressed(GameKeys.F1)) {
            addTestWeapon(gameData, world);
        }
    }

    private void addTestWeapon(GameData gameData, World world) {
        Entity player = world.getPlayer();
        Entity weapon = new Entity();
        weapon.setType(EntityType.MELEE);
        weapon.setMovementSpeed(0);
        weapon.setWidth(player.getWidth());
        weapon.setHeight(player.getHeight());
        weapon.getRoomPosition().set(player.getRoomPosition());
        weapon.setSpritePath("rpg/gameengine/sword.png");
        gameData.addWeapon(player, weapon);
        world.getCurrentRoom().addEntity(weapon);
        System.out.println("Added weapon to player: " + gameData.getWeapon(player).getType().toString());
    }

}
