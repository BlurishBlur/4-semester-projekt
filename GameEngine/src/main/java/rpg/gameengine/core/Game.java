package rpg.gameengine.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import rpg.common.data.GameData;
import rpg.common.data.World;
import rpg.gameengine.managers.GameInputProcessor;

public class Game implements ApplicationListener {
    
    private static OrthographicCamera camera;
    private final GameData gameData = new GameData();
    private World world = new World();

    @Override
    public void create() {
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        
        camera = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        camera.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        camera.update();
        
        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
    }
    
    @Override
    public void render() {
        gameData.setDeltaTime(Gdx.graphics.getDeltaTime());
        gameData.getKeys().update();
        update();
        draw();
    }
    
    private void update() {
        
    }
    
    private void draw() {
        
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
