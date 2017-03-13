package rpg.gameengine.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.data.World;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;
import rpg.common.services.IPostEntityProcessingService;
import rpg.gameengine.managers.GameInputProcessor;

public class Game implements ApplicationListener {

    private OrthographicCamera playerCamera;
    private OrthographicCamera hudCamera;
    private Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private World world = new World();
    private int fps;
    private long fpsTimer;
    private SpriteBatch batch;
    private BitmapFont font;
    private Map<Entity, Sprite> sprites;

    private Sprite map; // TODO load den her sprite ordentligt

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setCameraZoom(1.50f);
        sprites = new HashMap<>();
        batch = new SpriteBatch();
        font = new BitmapFont();
        fpsTimer = System.currentTimeMillis();

        for (IGamePluginService plugin : getGamePluginServices()) {
            plugin.start(gameData, world);
        }

        playerCamera = new OrthographicCamera(gameData.getDisplayWidth() / gameData.getCameraZoom(), gameData.getDisplayHeight() / gameData.getCameraZoom());
        playerCamera.position.set(playerCamera.viewportWidth / 2, playerCamera.viewportHeight / 2, 0);
        playerCamera.update();
        hudCamera = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        hudCamera.position.set(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2, 0);
        hudCamera.update();

        map = new Sprite(new Texture(Gdx.files.internal("rpg/gameengine/grass.png")));
        map.setPosition(0, 0);
        map.setSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
    }

    @Override
    public void render() {
        if (System.currentTimeMillis() - fpsTimer > 1000) {
            fps = (int) (gameData.getDeltaTime() * 3600);
            fpsTimer = System.currentTimeMillis();
        }
        gameData.setDeltaTime(Math.min(Gdx.graphics.getDeltaTime(), 0.0167f));
        update();
        handlePlayerCamera();
        loadSprites();
        draw();
        gameData.getKeys().update();
    }

    private void handlePlayerCamera() {
        Entity player = world.getEntity(EntityType.PLAYER);
        playerCamera.viewportWidth = gameData.getDisplayWidth() / gameData.getCameraZoom();
        playerCamera.viewportHeight = gameData.getDisplayHeight() / gameData.getCameraZoom();
        playerCamera.position.set(player.getPosition().getX(), player.getPosition().getY(), 0);
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
            Entity player = world.getEntity(EntityType.PLAYER);
            batch.setProjectionMatrix(hudCamera.combined);
            font.draw(batch, "FPS: " + fps + "\n" +
                    "Zoom: " + gameData.getCameraZoom() + "\n" +
                    "X: " + player.getPosition().getX() + "\n" +
                    "Y: " + player.getPosition().getY() + "\n" +
                    "DX: " + player.getVelocity().getX() + "\n" +
                    "DY: " + player.getVelocity().getY() + "\n" +
                    "Rotation: " + player.getVelocity().getAngle(), 7.5f, 127.5f);
        }
    }

    private void drawMap() {
        map.draw(batch);
    }

    private void drawEntitySprites() {
        for (Entity entity : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)) {
            Sprite sprite = sprites.get(entity);
            sprite.setRotation(entity.getDirection());
            sprite.setPosition(entity.getPosition().getX() - entity.getWidth() / 2, entity.getPosition().getY() - entity.getHeight() / 2);
            sprite.draw(batch);
        }
        for (Entity entity : world.getEntities(EntityType.MELEE)) {
            Sprite sprite = sprites.get(entity);
            sprite.setRotation(entity.getDirection());
            sprite.setPosition(entity.getPosition().getX() - entity.getWidth() / 2, entity.getPosition().getY() - entity.getHeight() / 2);
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
