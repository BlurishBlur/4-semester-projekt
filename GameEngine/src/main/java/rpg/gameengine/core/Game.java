package rpg.gameengine.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    
    private static OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private World world = new World();
    private int fps;
    private long fpsTimer;
    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        
        camera = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        camera.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
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
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //gameData.setDeltaTime(Gdx.graphics.getDeltaTime());
        if(System.currentTimeMillis() - fpsTimer > 1000) {
            fps = (int) (Gdx.graphics.getDeltaTime() * 3600);
            fpsTimer = System.currentTimeMillis();
            System.out.println(fps);
        }
        //update();
        //draw();
        //gameData.getKeys().update();
    }
    
    private void update() {
        
        /*batch.begin();
        font.draw(batch, "FPS: " + fps, 100, 100);
        batch.end();*/
        /*for(IEntityProcessingService processor : getEntityProcessingServices()) {
            processor.process(gameData, world);
        }*/
    }
    
    private void draw() {
        /*Entity player = world.getEntity(EntityType.PLAYER);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(player.getX(), player.getY(), player.getX() + 10, player.getY() + 10);
        shapeRenderer.end();*/
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
        player.setSpeed(1000);
        world.addEntity(player);
    }
    
}
