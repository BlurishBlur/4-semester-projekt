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
import rpg.common.world.Room;
import rpg.gameengine.managers.GameInputProcessor;

public class Game implements ApplicationListener {

    private OrthographicCamera playerCamera;
    private OrthographicCamera hudCamera;
    private Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private World world = new World();
    private int fps;
    private int frames;
    private long fpsTimer;
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
        loadRoomSprite(0, 0);

        playerCamera = new OrthographicCamera(gameData.getDisplayWidth() / gameData.getCameraZoom(), gameData.getDisplayHeight() / gameData.getCameraZoom());
        playerCamera.position.set(playerCamera.viewportWidth / 2, playerCamera.viewportHeight / 2, 0);
        playerCamera.update();
        hudCamera = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        hudCamera.position.set(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2, 0);
        hudCamera.update();
    }

    private void loadRoomSprite(int x, int y) {
        Room room = world.getCurrentRoom();
        previousRoom = currentRoom;
        currentRoom = new Sprite(new Texture(room.getSpritePath()));
        currentRoom.setPosition(x, y);
        currentRoom.setSize(room.getWidth(), room.getHeight());
    }

    @Override
    public void render() {
        if (System.currentTimeMillis() - fpsTimer > 1000) {
            fps = frames;
            frames = 0;
            fpsTimer = System.currentTimeMillis();
        }
        frames++;
        gameData.setDeltaTime(Math.min(Gdx.graphics.getDeltaTime(), 0.0167f));
        update();
        //handleRoomChange();
        handlePlayerCamera();
        loadSprites();
        draw();
        gameData.getKeys().update();
    }

    private void handlePlayerCamera() {
        Entity player = world.getPlayer();
        playerCamera.viewportWidth = gameData.getDisplayWidth() / gameData.getCameraZoom();
        playerCamera.viewportHeight = gameData.getDisplayHeight() / gameData.getCameraZoom();
        if (gameData.isChangingRoom()) {
            if (world.getRoom(world.getPlayer().getWorldPosition()) != world.getCurrentRoom()) {
                world.setCurrentRoom(world.getPlayer().getWorldPosition());
                loadRoomSprite(world.getCurrentRoom().getWidth() + 1, 0);
                player.getRoomPosition().setX(world.getCurrentRoom().getWidth() + (player.getWidth() / 2));
                System.out.println("loading new map");
                player.setCanMove(true);
            } else {
                playerCamera.position.interpolate(new Vector3(world.getCurrentRoom().getWidth() + playerCamera.viewportWidth / 2, playerCamera.viewportHeight / 2, 0), 1.0f * gameData.getDeltaTime(), Interpolation.linear);
                playerCamera.update();
                System.out.println("panning camera");
            }
        }
        else if (player.getRoomPosition().getX() != playerCamera.position.x || player.getRoomPosition().getY() != playerCamera.position.y) {
            //playerCamera.position.set(player.getPosition().getX(), player.getPosition().getY(), 0);
            playerCamera.position.lerp(new Vector3(player.getRoomPosition().getX(), player.getRoomPosition().getY(), 0), 2.5f * gameData.getDeltaTime());
            if (playerCamera.position.x - playerCamera.viewportWidth / 2 < 0) {
                playerCamera.position.set(0 + playerCamera.viewportWidth / 2, playerCamera.position.y, 0);
            }
            else if (playerCamera.position.x + playerCamera.viewportWidth / 2 > gameData.getDisplayWidth()) {
                playerCamera.position.set(gameData.getDisplayWidth() - playerCamera.viewportWidth / 2, playerCamera.position.y, 0);
            }
            if (playerCamera.position.y - playerCamera.viewportHeight / 2 < 0) {
                playerCamera.position.set(playerCamera.position.x, 0 + playerCamera.viewportHeight / 2, 0);
            }
            else if (playerCamera.position.y + playerCamera.viewportHeight / 2 > gameData.getDisplayHeight()) {
                playerCamera.position.set(playerCamera.position.x, gameData.getDisplayHeight() - playerCamera.viewportHeight / 2, 0);
            }
            playerCamera.update();
        }

    }

    private void handleRoomChange() {
        if (gameData.isChangingRoom()) {
            System.out.println("hi");
            if (world.getRoom(world.getPlayer().getWorldPosition()) != world.getCurrentRoom()) {
                loadRoomSprite(world.getCurrentRoom().getWidth() + 1, 0);
                world.setCurrentRoom(world.getPlayer().getWorldPosition());
                System.out.println("loading new map");
            } else {
                if(previousRoom.getX() > 0) {
                    previousRoom.translate(-1, 0);
                }
                playerCamera.position.lerp(new Vector3(world.getCurrentRoom().getWidth() + playerCamera.viewportWidth / 2, playerCamera.viewportHeight / 2, 0), 1.0f * gameData.getDeltaTime());
                playerCamera.update();
                System.out.println("panning camera");
            }
        }
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
            if (sprites.get(entity) == null) {
                Texture texture = new Texture(Gdx.files.internal(entity.getSpritePath()));
                Sprite sprite = new Sprite(texture);
                sprite.setSize(entity.getWidth(), entity.getHeight());
                sprite.setOriginCenter();
                sprites.put(entity, sprite);
            }
        }
    }

    private void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(playerCamera.combined);
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
            batch.setProjectionMatrix(hudCamera.combined);
            font.draw(batch, "FPS: " + fps + "\n"
                    + "Zoom: " + gameData.getCameraZoom() + "\n"
                    + "X: " + player.getRoomPosition().getX() + "\n"
                    + "Y: " + player.getRoomPosition().getY() + "\n" /*+
                    "DX: " + player.getVelocity().getX() + "\n" +
                    "DY: " + player.getVelocity().getY() + "\n" +
                    "Rotation: " + player.getVelocity().getAngle()*/, 7.5f, 127.5f);
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
            Sprite sprite = sprites.get(entity);
            sprite.setRotation(entity.getDirection());
            sprite.setPosition(entity.getRoomPosition().getX() - entity.getWidth() / 2, entity.getRoomPosition().getY() - entity.getHeight() / 2);
            sprite.draw(batch);
        }
        for (Entity entity : world.getEntities(EntityType.MELEE)) {
            Sprite sprite = sprites.get(entity);
            sprite.setRotation(entity.getDirection());
            sprite.setPosition(entity.getRoomPosition().getX() - entity.getWidth() / 2, entity.getRoomPosition().getY() - entity.getHeight() / 2);
            sprite.draw(batch);
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
