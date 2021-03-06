package rpg.movement;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.world.World;
import rpg.common.entities.Entity;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IPostEntityProcessingService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IPostEntityProcessingService.class)
})
public class MovementControlSystem implements IEntityProcessingService, IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getCurrentRoom().getEntities()) {
            if (entity.getVelocity().isMoving()) {
                entity.setDirection(entity.getVelocity().getAngle());
            }
        }
    }

    @Override
    public void postProcess(GameData gameData, World world) {
        for (Entity entity : world.getCurrentRoom().getEntities()) {
            float deltaTime = gameData.getDeltaTime();
            entity.getVelocity().scalar(deltaTime);
            if (entity.getVelocity().isDiagonal()) {
                entity.getVelocity().normalize(entity.getCurrentMovementSpeed());
            }
            if (entity.getVelocity().isMoving()) {
                entity.getRoomPosition().add(entity.getVelocity());
            }

            if (entity.hasWeapon()) {
                entity.getWeapon().getRoomPosition().set(entity.getRoomPosition());
            }
        }
    }

}
