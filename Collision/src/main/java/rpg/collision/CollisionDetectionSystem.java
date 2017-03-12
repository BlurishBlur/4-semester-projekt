package rpg.collision;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.World;
import rpg.common.entities.Entity;
import rpg.common.services.IPostEntityProcessingService;

@ServiceProviders(value = {
    @ServiceProvider(service = IPostEntityProcessingService.class)
})
public class CollisionDetectionSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            detectEdgeCollision(gameData, entity);
        }
    }
    
    private void detectEdgeCollision(GameData gameData, Entity entity) {
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
