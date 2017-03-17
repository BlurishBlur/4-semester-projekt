package rpg.combat;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.world.World;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.events.Event;
import rpg.common.events.EventType;
import rpg.common.services.IEntityProcessingService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class CombatSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Event event : gameData.getEvents()) {
            if (event.getType() == EventType.PLAYER_ATTACK) {
                for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
                    if (isHit(gameData.getWeapon(event.getEntity()), enemy)) {
                        System.out.println("enemy hit");
                    }
                }
                gameData.removeEvent(event);
            }
        }
    }

    private boolean isHit(Entity weaponAttack, Entity attackee) {
        float a = weaponAttack.getRoomPosition().getX() - attackee.getRoomPosition().getX();
        float b = weaponAttack.getRoomPosition().getY() - attackee.getRoomPosition().getY();

        double c = Math.sqrt((double) (a * a) + (double) (b * b));

        return c < weaponAttack.getWidth() / 2 + attackee.getWidth() / 2;

        //return Math.sqrt(Math.pow(entity1.getX() - entity2.getX(), 2) + Math.pow(entity1.getY() - entity2.getY(), 2)) < entity1.getRadius() + entity2.getRadius();
    }

}
