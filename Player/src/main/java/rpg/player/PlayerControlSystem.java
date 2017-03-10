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
        Entity player = world.getEntity(EntityType.PLAYER);
        float deltaTime = gameData.getDeltaTime();
        player.setDx(0);
        player.setDy(0);
        player.setDirection(0);
        if (gameData.getKeys().isDown(GameKeys.W)) {
            player.setDy(player.getSpeed() * deltaTime);
            player.setDirection(360);
        }
        else if (gameData.getKeys().isDown(GameKeys.S)) {
            player.setDy(-player.getSpeed() * deltaTime);
            player.setDirection(180);
        }
        if (gameData.getKeys().isDown(GameKeys.A)) {
            player.setDx(-player.getSpeed() * deltaTime);
            int angle = player.getDirection() + 90;
            if(angle > 90) {
                angle = (angle % 360) / 2;
            }
            player.setDirection(angle);
        }
        else if (gameData.getKeys().isDown(GameKeys.D)) {
            player.setDx(player.getSpeed() * deltaTime);
            int angle = player.getDirection() + 270;
            if(angle > 270) {
                angle /= 2;
            }
            player.setDirection(angle);
        }
        
        float diagonalFactor = (float) Math.sqrt(Math.pow(player.getSpeed(), 2) + Math.pow(player.getSpeed(), 2)) / player.getSpeed();
        if(player.getDx() != 0 && player.getDy() != 0) {
            player.setDx(player.getDx() / diagonalFactor);
            player.setDy(player.getDy() / diagonalFactor);
        }

        player.setX(player.getX() + player.getDx());
        player.setY(player.getY() + player.getDy());

        checkEdgeCollision(gameData, player);
    }

    private void checkEdgeCollision(GameData gameData, Entity player) { // skal flyttes ud i seperat modul, da det skal ske for alle entites
        if (player.getX() - (player.getWidth() / 2) < 0) {
            player.setX(0 + (player.getWidth() / 2));
            player.setDx(0);
        } else if (player.getX() + (player.getWidth() / 2) > gameData.getDisplayWidth()) {
            player.setX(gameData.getDisplayWidth() - (player.getWidth() / 2));
            player.setDx(0);
        }
        if (player.getY() - (player.getHeight() / 2) < 0) {
            player.setY(0 + (player.getHeight() / 2));
            player.setDy(0);
        } else if (player.getY() + (player.getHeight() / 2) > gameData.getDisplayHeight()) {
            player.setY(gameData.getDisplayHeight() - (player.getHeight() / 2));
            player.setDy(0);
        }
    }

    private Entity createPlayer() {
        Entity newPlayer = new Entity();
        newPlayer.setType(EntityType.PLAYER);
        newPlayer.setX(25);
        newPlayer.setY(25);
        newPlayer.setSpeed(200);
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
