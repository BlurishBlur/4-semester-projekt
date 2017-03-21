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
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)
})
public class PlayerControlSystem implements IEntityProcessingService, IGamePluginService {

    private Entity player;

    @Override
    public void start(GameData gameData, World world) {
        player = createPlayer();
        world.getCurrentRoom().addEntity(player);
    }

    @Override
    public void process(GameData gameData, World world) {
        Entity player = world.getPlayer();
        float sprintBoost = 1;
        player.getVelocity().set(0, 0);
        //player.setMovementSpeedModifier(1);
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
        if(player.getVelocity().isMoving()) {
            player.increaseFrame(gameData.getDeltaTime());
        }
        else {
            player.setCurrentFrame(1);
        }
        if(gameData.getKeys().isDown(GameKeys.SHIFT)) {
            //player.setMovementSpeedModifier(player.getMovementSpeedModifier() + 0.75f);
            sprintBoost = 1.75f;
        }
        player.setMovementSpeed(player.getDefaultMovementSpeed() * player.getMovementSpeedModifier() * sprintBoost);
    }

    private Entity createPlayer() {
        Entity newPlayer = new Entity();
        newPlayer.setType(EntityType.PLAYER);
        newPlayer.getRoomPosition().set(25, 25);
        newPlayer.getWorldPosition().set(0, 0);
        newPlayer.setDefaultMovementSpeed(200);
        newPlayer.setMaxHealth(100);
        newPlayer.setCurrentHealth(newPlayer.getMaxHealth());
        newPlayer.setMovementSpeedModifier(1);
        //newPlayer.setWidth(30);
        //newPlayer.setHeight(30);
        //newPlayer.setSpritePath("rpg/gameengine/player.png");
        newPlayer.setWidth(80);
        newPlayer.setHeight(80);
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
