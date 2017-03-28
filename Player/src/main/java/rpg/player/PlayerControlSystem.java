package rpg.player;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.entities.Entity;
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
        world.setPlayer(player);
        world.getCurrentRoom().addEntity(player);
    }

    @Override
    public void process(GameData gameData, World world) {
        Entity player = world.getPlayer();
        player.getVelocity().set(0, 0);
        player.setSprintModifier(1);
        if (gameData.getKeys().isDown(GameKeys.W)) {
            player.getVelocity().addY(player.getCurrentMovementSpeed());
        }
        else if (gameData.getKeys().isDown(GameKeys.S)) {
            player.getVelocity().subtractY(player.getCurrentMovementSpeed());
        }
        if (gameData.getKeys().isDown(GameKeys.A)) {
            player.getVelocity().subtractX(player.getCurrentMovementSpeed());
        }
        else if (gameData.getKeys().isDown(GameKeys.D)) {
            player.getVelocity().addX(player.getCurrentMovementSpeed());

        }
        if (player.getVelocity().isMoving()) {
            player.increaseFrame(gameData.getDeltaTime());
        }
        else {
            player.setCurrentFrame(1);
        }
        if (gameData.getKeys().isDown(GameKeys.SHIFT)) {
            player.setSprintModifier(1.75f);
        }
        player.setCurrentMovementSpeed(player.getDefaultMovementSpeed() * player.getMovementSpeedModifier() * player.getSprintModifier());
    }

    private Entity createPlayer() {
        Entity newPlayer = new Player();
        newPlayer.getRoomPosition().set(25, 25);
        newPlayer.getWorldPosition().set(0, 0);
        newPlayer.setDefaultMovementSpeed(200);
        newPlayer.setMaxHealth(100);
        newPlayer.setCurrentHealth(newPlayer.getMaxHealth());
        newPlayer.setMovementSpeedModifier(1);
        //newPlayer.setSpritePath("rpg/gameengine/player.png");
        newPlayer.setWidth(30);
        newPlayer.setHeight(30);
        newPlayer.setSpritePath("rpg/gameengine/testTexture.atlas");
        newPlayer.setCurrentFrame(1);
        newPlayer.setMaxFrames(3);
        //newPlayer.getSounds().put("GRASS", "rpg/gameengine/Footstep Grass 2.wav");
        return newPlayer;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.getCurrentRoom().removeEntity(player);
    }

}
