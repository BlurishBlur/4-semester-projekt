package rpg.combat;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.data.World;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.events.Event;
import rpg.common.events.EventType;
import rpg.common.services.IEntityProcessingService;
import rpg.common.util.Vector;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class CombatSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        Entity player = world.getEntity(EntityType.PLAYER);
        if (gameData.getKeys().isPressed(GameKeys.UP)) {
            gameData.getWeapon(player).getPosition().set(player.getPosition().plus(new Vector(0, player.getHeight() / 2)));
            gameData.getWeapon(player).setDirection(0);
            gameData.addEvent(new Event(EventType.PLAYER_ATTACK, player));
        }
        else if (gameData.getKeys().isPressed(GameKeys.DOWN)) {
            gameData.getWeapon(player).getPosition().set(player.getPosition().plus(new Vector(0, -player.getHeight() / 2)));
            gameData.getWeapon(player).setDirection(180);
            gameData.addEvent(new Event(EventType.PLAYER_ATTACK, player));
        }
        else if (gameData.getKeys().isPressed(GameKeys.LEFT)) {
            gameData.getWeapon(player).getPosition().set(player.getPosition().plus(new Vector(-player.getWidth() / 2, 0)));
            gameData.getWeapon(player).setDirection(90);
            gameData.addEvent(new Event(EventType.PLAYER_ATTACK, player));
        }
        else if (gameData.getKeys().isPressed(GameKeys.RIGHT)) {
            gameData.getWeapon(player).getPosition().set(player.getPosition().plus(new Vector(player.getWidth() / 2, 0)));
            gameData.getWeapon(player).setDirection(270);
            gameData.addEvent(new Event(EventType.PLAYER_ATTACK, player));
        }

        for (Event event : gameData.getEvents()) {
            if (event.getType() == EventType.PLAYER_ATTACK) {
                for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
                    if (isHit(gameData.getWeapon(event.getEntity()), enemy)) {
                        System.out.println("ramt");
                    }
                }
                gameData.removeEvent(event);
            }
        }
    }

    private boolean isHit(Entity weaponAttack, Entity attackee) {
        float a = weaponAttack.getPosition().getX() - attackee.getPosition().getX();
        float b = weaponAttack.getPosition().getY() - attackee.getPosition().getY();

        double c = Math.sqrt((double) (a * a) + (double) (b * b));

        return c < weaponAttack.getWidth() / 2 + attackee.getWidth() / 2;

        //return Math.sqrt(Math.pow(entity1.getX() - entity2.getX(), 2) + Math.pow(entity1.getY() - entity2.getY(), 2)) < entity1.getRadius() + entity2.getRadius();
    }

}
