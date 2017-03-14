package rpg.player;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.world.World;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
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
        Entity player = world.getPlayer();
        player.getVelocity().set(0, 0);
        if (player.isCanMove()) {
            if (gameData.getKeys().isDown(GameKeys.W)) {
                player.getVelocity().addY(player.getMovementSpeed());
            } else if (gameData.getKeys().isDown(GameKeys.S)) {
                player.getVelocity().subtractY(player.getMovementSpeed());
            }
            if (gameData.getKeys().isDown(GameKeys.A)) {
                player.getVelocity().subtractX(player.getMovementSpeed());
            } else if (gameData.getKeys().isDown(GameKeys.D)) {
                player.getVelocity().addX(player.getMovementSpeed());
            }
        }
    }

    private Entity createPlayer() {
        Entity newPlayer = new Entity();
        newPlayer.setType(EntityType.PLAYER);
        newPlayer.getRoomPosition().set(25, 25);
        newPlayer.getWorldPosition().set(0, 0);
        newPlayer.setMovementSpeed(200);
        newPlayer.setMaxHealth(100);
        newPlayer.setCurrentHealth(newPlayer.getMaxHealth());
        newPlayer.setWidth(30);
        newPlayer.setHeight(30);
        newPlayer.setSpritePath("rpg/gameengine/player.png");
        return newPlayer;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }

}
