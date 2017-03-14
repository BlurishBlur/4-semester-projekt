package rpg.collision;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.services.IPostEntityProcessingService;
import rpg.common.world.World;

@ServiceProviders(value = {
    @ServiceProvider(service = IPostEntityProcessingService.class)
})
public class CollisionDetectionSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            detectEdgeCollision(gameData, world, entity);
        }
    }
    
    private void detectEdgeCollision(GameData gameData, World world, Entity entity) {
        if (entity.getRoomPosition().getX() - (entity.getWidth() / 2) < 0) {
            entity.getRoomPosition().setX(0 + (entity.getWidth() / 2));
            entity.getVelocity().setX(0);
        }
        else if (entity.getRoomPosition().getX() + (entity.getWidth() / 2) > gameData.getDisplayWidth()) {
            if(world.getCurrentRoom().canExitRight() && !gameData.isChangingRoom()) {
                gameData.setIsChangingRoom(true);
                entity.setCanMove(false);
                entity.getWorldPosition().addX(1);
                System.out.println("changing room");
            }
            
            
            //entity.getRoomPosition().setX(gameData.getDisplayWidth() - (entity.getWidth() / 2));
            //entity.getVelocity().setX(0);
        }
        if (entity.getRoomPosition().getY() - (entity.getHeight() / 2) < 0) {
            entity.getRoomPosition().setY(0 + (entity.getHeight() / 2));
            entity.getVelocity().setY(0);
        }
        else if (entity.getRoomPosition().getY() + (entity.getHeight() / 2) > gameData.getDisplayHeight()) {
            entity.getRoomPosition().setY(gameData.getDisplayHeight() - (entity.getHeight() / 2));
            entity.getVelocity().setY(0);
        }
    }
    
}
