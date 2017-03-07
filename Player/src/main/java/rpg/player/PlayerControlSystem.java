package rpg.player;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.Entity;
import rpg.common.data.EntityType;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.data.World;
import rpg.common.services.IEntityProcessingService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        Entity player = world.getEntity(EntityType.PLAYER);
        float deltaTime = gameData.getDeltaTime();
        //player.setDx(0);
        //player.setDy(0);
        if(gameData.getKeys().isDown(GameKeys.W)) {
            player.setDy(player.getSpeed() * deltaTime);
        }
        if(gameData.getKeys().isDown(GameKeys.A)) {
            player.setDx(-player.getSpeed() * deltaTime);
        }
        if(gameData.getKeys().isDown(GameKeys.S)) {
            player.setDy(-player.getSpeed() * deltaTime);
        }
        if(gameData.getKeys().isDown(GameKeys.D)) {
            player.setDx(player.getSpeed() * deltaTime);
        }
        
        player.setX(player.getX() + player.getDx() * deltaTime);
        player.setY(player.getY() + player.getDy() * deltaTime);
    }
    
}
