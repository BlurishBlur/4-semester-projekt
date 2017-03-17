package rpg.gameengine.core;

import rpg.gameengine.managers.Camera;
import rpg.gameengine.managers.Renderer;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.Collection;
import org.openide.util.Lookup;
import rpg.common.entities.Entity;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.world.World;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;
import rpg.common.services.IPostEntityProcessingService;
import rpg.common.util.Logger;
import rpg.gameengine.core.hud.Hud;
import rpg.gameengine.managers.GameInputProcessor;

public class Game implements ApplicationListener {

    private Camera playerCamera;
    private Camera hudCamera;
    private Renderer renderer;
    private Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private World world = new World();
    private int fps;
    private int frames;
    private long fpsTimer;
    private Hud hud;
    private GameInputProcessor gameInputProcessor;

    @Override
    public void create() {
        fpsTimer = System.currentTimeMillis();
        Gdx.input.setInputProcessor(gameInputProcessor = new GameInputProcessor(gameData));
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setCameraZoom(1.50f);
        


        for (IGamePluginService plugin : getGamePluginServices()) {
            plugin.start(gameData, world);
        }

        world.setCurrentRoom(world.getPlayer().getWorldPosition());
        renderer = new Renderer();
        renderer.loadRoomSprite(world);

        playerCamera = new Camera(gameData.getDisplayWidth() / gameData.getCameraZoom(), gameData.getDisplayHeight() / gameData.getCameraZoom(), world.getPlayer());
        playerCamera.update(gameData, world);
        hudCamera = new Camera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        hudCamera.update(gameData, world);
        hud = new Hud(hudCamera, gameInputProcessor, gameData);
    }

    @Override
    public void render() {
        calculateFPS();
        gameData.setDeltaTime(Math.min(Gdx.graphics.getDeltaTime(), 0.0167f));
        update();
        updatePlayerCamera();
        renderer.loadSprites(world);
        renderer.draw(gameData, world, playerCamera);
        drawDebug();
        hud.drawHUD(world);
        gameData.getKeys().update();
        
    }

    private void updatePlayerCamera() {
        if (gameData.isChangingRoom() && world.getRoom(playerCamera.getTarget().getWorldPosition()) != world.getCurrentRoom()) {
            playerCamera.initializeRoomChange(world);
            renderer.loadNewRoomSprite(world);
        }
        playerCamera.update(gameData, world);
    }

    private void update() {
        for (IEntityProcessingService processor : getEntityProcessingServices()) {
            processor.process(gameData, world);
        }
        for (IPostEntityProcessingService postProcessor : getPostEntityProcessingServices()) {
            postProcessor.process(gameData, world);
        }
    }

    public void drawDebug() {
        if (gameData.getKeys().isPressed(GameKeys.F1)) {
            gameData.setShowDebug(!gameData.showDebug());
        }
        if (gameData.showDebug()) {
            Entity player = world.getPlayer();
            String message = "FPS: " + fps + "\n"
                    + "Zoom: " + gameData.getCameraZoom() + "\n"
                    + "X: " + player.getRoomPosition().getX() + "\n"
                    + "Y: " + player.getRoomPosition().getY() + "\n"
                    /*+
                    "DX: " + player.getVelocity().getX() + "\n" +
                    "DY: " + player.getVelocity().getY() + "\n" +
                    "Rotation: " + player.getVelocity().getAngle()*/ + "Movement speed: " + player.getMovementSpeed() + "\n"
                    + "Movement speed modifier: " + player.getMovementSpeedModifier();
            renderer.drawHud(gameData, world, hudCamera, message);
        }
    }

    private void calculateFPS() {
        frames++;
        if (System.currentTimeMillis() - fpsTimer > 1000) {
            fps = frames;
            frames = 0;
            fpsTimer = System.currentTimeMillis();
        }
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    private Collection<? extends IGamePluginService> getGamePluginServices() {
        return lookup.lookupAll(IGamePluginService.class);
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return lookup.lookupAll(IPostEntityProcessingService.class);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
