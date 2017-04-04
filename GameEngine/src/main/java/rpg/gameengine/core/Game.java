package rpg.gameengine.core;

import rpg.gameengine.managers.Camera;
import rpg.gameengine.managers.SpriteManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import java.util.Collection;
import org.openide.util.Lookup;
import rpg.common.entities.Entity;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.world.World;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;
import rpg.common.services.IPostEntityProcessingService;
import rpg.gameengine.managers.Hud;
import rpg.gameengine.managers.GameInputProcessor;
import rpg.gameengine.managers.SoundManager;

public class Game implements ApplicationListener {

    private Camera playerCamera;
    private Camera hudCamera;
    private SpriteManager renderer;
    private Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private World world = new World();
    private Hud hud;
    private GameInputProcessor gameInputProcessor;
    private SoundManager soundManager;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(gameInputProcessor = new GameInputProcessor(gameData));
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setCameraZoom(1.50f);

        for (IGamePluginService plugin : getGamePluginServices()) {
            plugin.start(gameData, world);
        }
        
        //walk = Gdx.audio.newSound(Gdx.files.internal(world.getEntity(EntityType.PLAYER).getSounds().get("GRASS").toString()));

        world.setCurrentRoom(world.getPlayer().getWorldPosition());
        renderer = new SpriteManager();
        renderer.loadRoomSprite(world);
        
        soundManager = new SoundManager();
        soundManager.loadSounds(world);

        playerCamera = new Camera(gameData.getDisplayWidth() / gameData.getCameraZoom(), gameData.getDisplayHeight() / gameData.getCameraZoom(), world.getPlayer());
        playerCamera.update(gameData, world);
        hudCamera = new Camera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        hudCamera.update(gameData, world);
        hud = new Hud(hudCamera, gameInputProcessor, gameData, world);
    }

    @Override
    public void render() {
        gameData.setDeltaTime(Math.min(Gdx.graphics.getDeltaTime(), (float) 60f / 3600f));
        update();
        updatePlayerCamera();
        renderer.loadSprites(world);
        renderer.draw(gameData, world, playerCamera);
        drawDebug();
        hud.drawHud();
        gameData.getKeys().update();
    }

    private void updatePlayerCamera() {
        if (gameData.isChangingRoom() && world.getRoom(playerCamera.getTarget().getWorldPosition()) != world.getCurrentRoom()) {
            world.getRoom(playerCamera.getTarget().getWorldPosition()).addEntity(playerCamera.getTarget());
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
            world.getPlayer().setSkillPoints(world.getPlayer().getSkillPoints() + 1);
        }
        if (gameData.showDebug()) {
            Entity player = world.getPlayer();
            String message = "FPS: " + Gdx.graphics.getFramesPerSecond() + "\n"
                    + "Zoom: " + gameData.getCameraZoom() + "\n"
                    + "X: " + player.getRoomPosition().getX() + "\n"
                    + "Y: " + player.getRoomPosition().getY() + "\n"
                    /*+
                    "DX: " + player.getVelocity().getX() + "\n" +
                    "DY: " + player.getVelocity().getY() + "\n" +
                    "Rotation: " + player.getVelocity().getAngle()*/ + "Movement speed: " + player.getCurrentMovementSpeed() + "\n"
                    + "Movement speed modifier: " + player.getMovementSpeedModifier();
            renderer.drawDebug(gameData, world, hudCamera, message);
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
