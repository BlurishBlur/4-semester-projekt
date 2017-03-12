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
            float diagonalFactor = (float) Math.sqrt(Math.pow(entity.getMovementSpeed(), 2) + Math.pow(entity.getMovementSpeed(), 2)) / entity.getMovementSpeed();
            if (entity.getDx() != 0 && entity.getDy() != 0) {
                entity.setDx(entity.getDx() / diagonalFactor);
                entity.setDy(entity.getDy() / diagonalFactor);
            }

            if (entity.getDx() != 0 || entity.getDy() != 0) {
                double angle = Math.toDegrees(Math.atan2(entity.getDy(), entity.getDx()));
                entity.setDirection(angle);
            }

            entity.setX(entity.getX() + entity.getDx());
            entity.setY(entity.getY() + entity.getDy());

            checkEdgeCollision(gameData, entity);
        }
    }

    private void checkEdgeCollision(GameData gameData, Entity entity) {
        if (entity.getX() - (entity.getWidth() / 2) < 0) {
            entity.setX(0 + (entity.getWidth() / 2));
            entity.setDx(0);
        }
        else if (entity.getX() + (entity.getWidth() / 2) > gameData.getDisplayWidth()) {
            entity.setX(gameData.getDisplayWidth() - (entity.getWidth() / 2));
            entity.setDx(0);
        }
        if (entity.getY() - (entity.getHeight() / 2) < 0) {
            entity.setY(0 + (entity.getHeight() / 2));
            entity.setDy(0);
        }
        else if (entity.getY() + (entity.getHeight() / 2) > gameData.getDisplayHeight()) {
            entity.setY(gameData.getDisplayHeight() - (entity.getHeight() / 2));
            entity.setDy(0);
        }
    }

}
