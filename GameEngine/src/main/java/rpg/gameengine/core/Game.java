package rpg.gameengine.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.world.World;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;
import rpg.common.services.IPostEntityProcessingService;
import rpg.common.util.Logger;
import rpg.common.util.Vector;
import rpg.common.world.Room;
import rpg.gameengine.managers.GameInputProcessor;

public class Game implements ApplicationListener {

    private Camera playerCamera;
    private Camera hudCamera;
    private Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private World world = new World();
    private int fps;
    private int frames;
    private long fpsTimer;
    private float cameraPanTime;
    private SpriteBatch batch;
    private BitmapFont font;
    private Map<Entity, Sprite> sprites;
    private Sprite currentRoom, previousRoom;

    @Override
    public void create() {
        fpsTimer = System.currentTimeMillis();
        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setCameraZoom(1.50f);
        sprites = new HashMap<>();
        batch = new SpriteBatch();
        font = new BitmapFont();

        for (IGamePluginService plugin : getGamePluginServices()) {
            plugin.start(gameData, world);
        }

        world.setCurrentRoom(world.getPlayer().getWorldPosition());
        loadRoomSprite();

        playerCamera = new Camera(gameData.getDisplayWidth() / gameData.getCameraZoom(), gameData.getDisplayHeight() / gameData.getCameraZoom(), world.getPlayer());
        playerCamera.update(gameData, world);
        hudCamera = new Camera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        hudCamera.update(gameData, world);
    }

    private void loadRoomSprite() {
        Room room = world.getCurrentRoom();
        previousRoom = currentRoom;
        if (previousRoom != null) {
            Entity player = world.getPlayer();
            previousRoom.setPosition(previousRoom.getWidth() * (player.getWorldVelocity().getX() * -1), previousRoom.getHeight() * (player.getWorldVelocity().getY() * -1));
        }
        currentRoom = new Sprite(new Texture(room.getSpritePath()));
        currentRoom.setPosition(0, 0);
        currentRoom.setSize(room.getWidth(), room.getHeight());
    }

    @Override
    public void render() {
        calculateFPS();
        gameData.setDeltaTime(Math.min(Gdx.graphics.getDeltaTime(), 0.0167f));
        update();
        updatePlayerCamera();
        loadSprites();
        draw();
        gameData.getKeys().update();
    }

    private void updatePlayerCamera() {
        if(gameData.isChangingRoom() && world.getRoom(playerCamera.getTarget().getWorldPosition()) != world.getCurrentRoom()) {
            playerCamera.initializeRoomChange(world);
            loadRoomSprite();
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

    private void loadSprites() {
        for (Entity entity : world.getEntities()) {
            if (!sprites.containsKey(entity)) {
                try {
                    Texture texture = new Texture(entity.getSpritePath());
                    Sprite sprite = new Sprite(texture);
                    sprite.setSize(entity.getWidth(), entity.getHeight());
                    sprite.setOriginCenter();
                    sprites.put(entity, sprite);
                }
                catch(NullPointerException e) {
                    Logger.log("No spritepath found for entity of type " + entity.getType() + ": " + entity.toString());
                }
            }
        }
    }

    private void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(playerCamera.getProjection());
        batch.begin();
        drawMap();
        drawEntitySprites();
        drawHud();
        batch.end();
    }

    private void drawHud() {
        if (gameData.getKeys().isPressed(GameKeys.F1)) {
            gameData.setShowDebug(!gameData.isShowDebug());
        }
        if (gameData.isShowDebug()) {
            Entity player = world.getPlayer();
            batch.setProjectionMatrix(hudCamera.getProjection());
            font.draw(batch, "FPS: " + fps + "\n"
                    + "Zoom: " + gameData.getCameraZoom() + "\n"
                    + "X: " + player.getRoomPosition().getX() + "\n"
                    + "Y: " + player.getRoomPosition().getY() + "\n"
                    /*+
                    "DX: " + player.getVelocity().getX() + "\n" +
                    "DY: " + player.getVelocity().getY() + "\n" +
                    "Rotation: " + player.getVelocity().getAngle()*/ + "Movement speed: " + player.getMovementSpeed() + "\n"
                    + "Movement speed modifier: " + player.getMovementSpeedModifier(), 7.5f, 127.5f);
        }
    }

    private void drawMap() {
        batch.disableBlending();
        if (previousRoom != null) {
            previousRoom.draw(batch);
        }
        currentRoom.draw(batch);
        batch.enableBlending();
    }

    private void drawEntitySprites() {
        for (Entity entity : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)) {
            try {
                Sprite sprite = sprites.get(entity);
                sprite.setRotation(entity.getDirection());
                sprite.setPosition(entity.getRoomPosition().getX() - entity.getWidth() / 2, entity.getRoomPosition().getY() - entity.getHeight() / 2);
                sprite.draw(batch);
            }
            catch (NullPointerException e) {
                Logger.log("No sprite found for entity of type " + entity.getType() + ": " + entity.toString());
            }
        }
        for (Entity entity : world.getEntities(EntityType.MELEE)) {
            Sprite sprite = sprites.get(entity);
            sprite.setRotation(entity.getDirection());
            sprite.setPosition(entity.getRoomPosition().getX() - entity.getWidth() / 2, entity.getRoomPosition().getY() - entity.getHeight() / 2);
            sprite.draw(batch);
        }
    }

    private void calculateFPS() {
        if (System.currentTimeMillis() - fpsTimer > 1000) {
            fps = frames;
            frames = 0;
            fpsTimer = System.currentTimeMillis();
        }
        frames++;
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
