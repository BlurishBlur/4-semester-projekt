package rpg.movement;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.World;
import rpg.common.entities.Entity;
import rpg.common.services.IEntityProcessingService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class MovementControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            float deltaTime = gameData.getDeltaTime();
            entity.getMovement().scalar(deltaTime);
            if (entity.getMovement().isDiagonal()) {
                entity.getMovement().normalize(entity.getMovementSpeed());
            }
            if (entity.getMovement().isMoving()) {
                entity.setDirection(entity.getMovement().getAngle());
            }
            entity.getPosition().add(entity.getMovement());
        }
    }

}
