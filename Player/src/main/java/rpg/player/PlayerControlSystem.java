package rpg.player;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.data.World;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)
})
public class PlayerControlSystem implements IEntityProcessingService, IGamePluginService {

    private Entity player;

    @Override
    public void start(GameData gameData, World world) {
        player = createPlayer();
        world.addEntity(player);
    }

    @Override
    public void process(GameData gameData, World world) {
        Entity player = world.getEntity(EntityType.PLAYER);
        player.getVelocity().set(0, 0);
        if (gameData.getKeys().isDown(GameKeys.W)) {
            player.getVelocity().addY(player.getMovementSpeed());
            movePlayer(player);
        } else if (gameData.getKeys().isDown(GameKeys.S)) {
            player.getVelocity().subtractY(player.getMovementSpeed());
            movePlayer(player);
        }
        if (gameData.getKeys().isDown(GameKeys.A)) {
            player.getVelocity().subtractX(player.getMovementSpeed());
            movePlayer(player);
        } else if (gameData.getKeys().isDown(GameKeys.D)) {
            player.getVelocity().addX(player.getMovementSpeed());
            
        }
        if(player.getVelocity().isMoving()){
            movePlayer(player);
        }
        else{
            player.setCurrentFrame(1);
        }
    }

    private void movePlayer(Entity player) {
        player.setCurrentFrame(player.getCurrentFrame() + 1);
        if (player.getCurrentFrame() > player.getMaxFrames()) {
            player.setCurrentFrame(1);
        }
    }

    private Entity createPlayer() {
        Entity newPlayer = new Entity();
        newPlayer.setType(EntityType.PLAYER);
        newPlayer.getPosition().set(25, 25);
        newPlayer.setMovementSpeed(200);
        newPlayer.setMaxHealth(100);
        newPlayer.setCurrentHealth(newPlayer.getMaxHealth());
        newPlayer.setWidth(100);
        newPlayer.setHeight(100);
        newPlayer.setSpritePath("rpg/gameengine/noget.atlas");
        newPlayer.setCurrentFrame(1);
        newPlayer.setMaxFrames(4);
        return newPlayer;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }

}
