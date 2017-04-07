package rpg.player;

import rpg.commonplayer.Player;
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

    private Player player;

    @Override
    public void start(GameData gameData, World world) {
        player = createPlayer();
        world.setPlayer(player);
        world.getCurrentRoom().addEntity(player);
    }

    @Override
    public void process(GameData gameData, World world) {
        player.getVelocity().set(0, 0);
        handleEdgeCollision(gameData, world, player);
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

    private Player createPlayer() {
        Player newPlayer = new Player();
        newPlayer.getRoomPosition().set(25, 25);
        newPlayer.getWorldPosition().set(0, 0);
        newPlayer.setDefaultMovementSpeed(200);
        newPlayer.setLevel(1);
        newPlayer.setExperiencePoints(0);
        newPlayer.setMaxHealth(100);
        newPlayer.setCurrentHealth(newPlayer.getMaxHealth());
        newPlayer.setMovementSpeedModifier(1);
        //newPlayer.setSpritePath("rpg/gameengine/player.png");
        newPlayer.setWidth(50);
        newPlayer.setHeight(50);
        newPlayer.setSpritePath("rpg/gameengine/testTexture.atlas");
        newPlayer.setCurrentFrame(1);
        newPlayer.setMaxFrames(3);
        newPlayer.getSounds().put("GRASS", "rpg/gameengine/Footstep Grass 2.wav");
        return newPlayer;
    }

    private void handleEdgeCollision(GameData gameData, World world, Entity player) {
        if (player.getRoomPosition().getX() - (player.getWidth() / 2) < 0) {
            if (world.getCurrentRoom().canExitLeft() && !gameData.isChangingRoom()) {
                gameData.setIsChangingRoom(true);
                player.getWorldVelocity().set(-1, 0);
                player.getWorldPosition().add(player.getWorldVelocity());
            }
            else {
                player.getRoomPosition().setX(0 + (player.getWidth() / 2));
                player.getVelocity().setX(0);
            }
        }
        else if (player.getRoomPosition().getX() + (player.getWidth() / 2) > world.getCurrentRoom().getWidth()) {
            if (world.getCurrentRoom().canExitRight() && !gameData.isChangingRoom()) {
                gameData.setIsChangingRoom(true);
                player.getWorldVelocity().set(1, 0);
                player.getWorldPosition().add(player.getWorldVelocity());
            }
            else {
                player.getRoomPosition().setX(world.getCurrentRoom().getWidth() - (player.getWidth() / 2));
                player.getVelocity().setX(0);
            }
        }
        if (player.getRoomPosition().getY() - (player.getHeight() / 2) < 0) {
            if (world.getCurrentRoom().canExitDown() && !gameData.isChangingRoom()) {
                gameData.setIsChangingRoom(true);
                player.getWorldVelocity().set(0, -1);
                player.getWorldPosition().add(player.getWorldVelocity());
            }
            else {
                player.getRoomPosition().setY(0 + (player.getHeight() / 2));
                player.getVelocity().setY(0);
            }
        }
        else if (player.getRoomPosition().getY() + (player.getHeight() / 2) > world.getCurrentRoom().getHeight()) {
            if (world.getCurrentRoom().canExitUp() && !gameData.isChangingRoom()) {
                gameData.setIsChangingRoom(true);
                player.getWorldVelocity().set(0, 1);
                player.getWorldPosition().add(player.getWorldVelocity());
            }
            else {
                player.getRoomPosition().setY(world.getCurrentRoom().getHeight() - (player.getHeight() / 2));
                player.getVelocity().setY(0);
            }
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.getCurrentRoom().removeEntity(player);
    }

}
