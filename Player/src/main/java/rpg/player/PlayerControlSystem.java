package rpg.player;

import com.badlogic.gdx.math.Vector2;
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
        float deltaTime = gameData.getDeltaTime();
        player.setDx(0);
        player.setDy(0);
        if (gameData.getKeys().isDown(GameKeys.W)) {
            player.setDy(player.getMovementSpeed() * deltaTime);
        }
        else if (gameData.getKeys().isDown(GameKeys.S)) {
            player.setDy(-player.getMovementSpeed() * deltaTime);
        }
        if (gameData.getKeys().isDown(GameKeys.A)) {
            player.setDx(-player.getMovementSpeed() * deltaTime);
        }
        else if (gameData.getKeys().isDown(GameKeys.D)) {
            player.setDx(player.getMovementSpeed() * deltaTime);
        }
    }

    private Entity createPlayer() {
        Entity newPlayer = new Entity();
        newPlayer.setType(EntityType.PLAYER);
        newPlayer.setX(25);
        newPlayer.setY(25);
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
