package rpg.gameengine.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Collection;
import org.openide.util.Lookup;
import rpg.common.data.Entity;
import rpg.common.data.EntityType;
import rpg.common.data.GameData;
import rpg.common.data.World;
import rpg.common.services.IEntityProcessingService;
import rpg.gameengine.managers.GameInputProcessor;

public class Game implements ApplicationListener {
    
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private World world = new World();
    private int fps;
    private long fpsTimer;
    private SpriteBatch batch;
    private BitmapFont font;
    
    private Sprite map, playerSprite;

    @Override
    public void create() {
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setAspectRatio(gameData.getDisplayHeight() / gameData.getDisplayWidth());
        
        map = new Sprite(new Texture(Gdx.files.internal("rpg/gameengine/grass.png")));
        map.setPosition(0, 0);
        map.setSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        
        playerSprite = new Sprite(new Texture(Gdx.files.internal("rpg/gameengine/char.png")));
        playerSprite.setPosition(0, 0);
        playerSprite.setSize(30, 30);
        
        camera = new OrthographicCamera(gameData.getDisplayWidth() / 1.30f, gameData.getDisplayHeight() / 1.30f);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        
        shapeRenderer = new ShapeRenderer();
        
        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
        
        createPlayer();
        
        batch = new SpriteBatch();
        font = new BitmapFont();
        fpsTimer = System.currentTimeMillis();
    }
    
    @Override
    public void render() {
        gameData.setDeltaTime(Math.min(Gdx.graphics.getDeltaTime(), 0.0167f));
        gameData.getKeys().update();
        update();
        handleCamera();
        draw();
    }
    
    private void handleCamera() {
        Entity player = world.getEntity(EntityType.PLAYER);
        camera.position.set(player.getX(), player.getY(), 0);
        if(camera.position.x - camera.viewportWidth / 2 < 0) {
            camera.position.set(0 + camera.viewportWidth / 2, camera.position.y, 0);
        }
        else if(camera.position.x + camera.viewportWidth / 2 > gameData.getDisplayWidth()) {
            camera.position.set(gameData.getDisplayWidth() - camera.viewportWidth / 2, camera.position.y, 0);
        }
        if(camera.position.y - camera.viewportHeight / 2 < 0) {
            camera.position.set(camera.position.x, 0 + camera.viewportHeight / 2, 0);
        }
        else if(camera.position.y + camera.viewportHeight / 2 > gameData.getDisplayHeight()) {
            camera.position.set(camera.position.x, gameData.getDisplayHeight() - camera.viewportHeight / 2, 0);
        }
        camera.update();
    }
    
    private void update() {
        if(System.currentTimeMillis() - fpsTimer > 1000) {
            fps = (int) (gameData.getDeltaTime() * 3600);
            fpsTimer = System.currentTimeMillis();
        }
        for(IEntityProcessingService processor : getEntityProcessingServices()) {
            processor.process(gameData, world);
        }
    }
    
    private void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawMap();
        drawPlayer();
        batch.end();
    }
    
    private void drawMap() {
        map.draw(batch);
    }
    
    private void drawPlayer() {
        Entity player = world.getEntity(EntityType.PLAYER);
        playerSprite.setPosition(player.getX(), player.getY());
        playerSprite.draw(batch);
    }
    
    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
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

    private void createPlayer() {
        Entity player = new Entity();
        player.setType(EntityType.PLAYER);
        player.setX(20);
        player.setY(50);
        player.setSpeed(200);
        player.setWidth(30);
        player.setHeight(30);
        world.addEntity(player);
    }
    
}
