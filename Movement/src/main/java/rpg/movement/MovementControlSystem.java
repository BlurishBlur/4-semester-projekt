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
            if(entity.getMovement().isDiagonal()) {
                entity.getMovement().normalize(entity.getMovementSpeed());
            }
            if (entity.getMovement().isMoving()) {
                entity.setDirection(entity.getMovement().getAngle());
            }
            entity.getPosition().add(entity.getMovement());

            checkEdgeCollision(gameData, entity);
        }
    }

    private void checkEdgeCollision(GameData gameData, Entity entity) {
        if (entity.getPosition().getX() - (entity.getWidth() / 2) < 0) {
            entity.getPosition().setX(0 + (entity.getWidth() / 2));
            entity.getMovement().setX(0);
        }
        else if (entity.getPosition().getX() + (entity.getWidth() / 2) > gameData.getDisplayWidth()) {
            entity.getPosition().setX(gameData.getDisplayWidth() - (entity.getWidth() / 2));
            entity.getMovement().setX(0);
        }
        if (entity.getPosition().getY() - (entity.getHeight() / 2) < 0) {
            entity.getPosition().setY(0 + (entity.getHeight() / 2));
            entity.getMovement().setY(0);
        }
        else if (entity.getPosition().getY() + (entity.getHeight() / 2) > gameData.getDisplayHeight()) {
            entity.getPosition().setY(gameData.getDisplayHeight() - (entity.getHeight() / 2));
            entity.getMovement().setY(0);
        }
    }

}
