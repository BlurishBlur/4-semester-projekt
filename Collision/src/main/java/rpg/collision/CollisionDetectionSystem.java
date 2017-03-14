package rpg.collision;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.services.IPostEntityProcessingService;
import rpg.common.world.World;

@ServiceProviders(value = {
    @ServiceProvider(service = IPostEntityProcessingService.class)
})
public class CollisionDetectionSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.PLAYER)) {
            detectEdgeCollision(gameData, world, entity);
        }
    }

    private void detectEdgeCollision(GameData gameData, World world, Entity entity) {
        if (entity.getRoomPosition().getX() - (entity.getWidth() / 2) < 0) {
            if (world.getCurrentRoom().canExitLeft() && !gameData.isChangingRoom()) {
                gameData.setIsChangingRoom(true);
                entity.getWorldVelocity().set(-1, 0);
                entity.getWorldPosition().add(entity.getWorldVelocity());
            }
            else {
                entity.getRoomPosition().setX(0 + (entity.getWidth() / 2));
                entity.getVelocity().setX(0);
            }
        }
        else if (entity.getRoomPosition().getX() + (entity.getWidth() / 2) > gameData.getDisplayWidth()) {
            if (world.getCurrentRoom().canExitRight() && !gameData.isChangingRoom()) {
                gameData.setIsChangingRoom(true);
                entity.getWorldVelocity().set(1, 0);
                entity.getWorldPosition().add(entity.getWorldVelocity());
            }
            else {
                entity.getRoomPosition().setX(gameData.getDisplayWidth() - (entity.getWidth() / 2));
                entity.getVelocity().setX(0);
            }
        }
        if (entity.getRoomPosition().getY() - (entity.getHeight() / 2) < 0) {
            if (world.getCurrentRoom().canExitDown() && !gameData.isChangingRoom()) {
                gameData.setIsChangingRoom(true);
                entity.getWorldVelocity().set(0, -1);
                entity.getWorldPosition().add(entity.getWorldVelocity());
            }
            else {
                entity.getRoomPosition().setY(0 + (entity.getHeight() / 2));
                entity.getVelocity().setY(0);
            }
        }
        else if (entity.getRoomPosition().getY() + (entity.getHeight() / 2) > gameData.getDisplayHeight()) {
            if (world.getCurrentRoom().canExitUp() && !gameData.isChangingRoom()) {
                gameData.setIsChangingRoom(true);
                entity.getWorldVelocity().set(0, 1);
                entity.getWorldPosition().add(entity.getWorldVelocity());
            }
            else {
                entity.getRoomPosition().setY(gameData.getDisplayHeight() - (entity.getHeight() / 2));
                entity.getVelocity().setY(0);
            }
        }
    }

}
