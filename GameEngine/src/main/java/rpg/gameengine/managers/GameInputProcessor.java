package rpg.gameengine.managers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;

public class GameInputProcessor extends InputAdapter {
    
    private final GameData gameData;
    
    public GameInputProcessor(GameData gameData) {
        this.gameData = gameData;
    }
    
    @Override
    public boolean keyDown(int key) {
        if(key == Keys.W) {
            gameData.getKeys().setKey(GameKeys.W, true);
        }
        if(key == Keys.A) {
            gameData.getKeys().setKey(GameKeys.A, true);
        }
        if(key == Keys.S) {
            gameData.getKeys().setKey(GameKeys.S, true);
        }
        if(key == Keys.D) {
            gameData.getKeys().setKey(GameKeys.D, true);
        }
        if(key == Keys.UP) {
            gameData.getKeys().setKey(GameKeys.UP, true);
        }
        if(key == Keys.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT, true);
        }
        if(key == Keys.DOWN) {
            gameData.getKeys().setKey(GameKeys.DOWN, true);
        }
        if(key == Keys.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT, true);
        }
        return true;
    }
    
    @Override
    public boolean keyUp(int key) {
        if(key == Keys.W) {
            gameData.getKeys().setKey(GameKeys.W, false);
        }
        if(key == Keys.A) {
            gameData.getKeys().setKey(GameKeys.A, false);
        }
        if(key == Keys.S) {
            gameData.getKeys().setKey(GameKeys.S, false);
        }
        if(key == Keys.D) {
            gameData.getKeys().setKey(GameKeys.D, false);
        }
        if(key == Keys.UP) {
            gameData.getKeys().setKey(GameKeys.UP, false);
        }
        if(key == Keys.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT, false);
        }
        if(key == Keys.DOWN) {
            gameData.getKeys().setKey(GameKeys.DOWN, false);
        }
        if(key == Keys.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT, false);
        }
        return true;
    }
    
}
