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
        if(key == Keys.PLUS) {
            gameData.getKeys().setKey(GameKeys.PLUS, true);
        }
        if(key == Keys.MINUS) {
            gameData.getKeys().setKey(GameKeys.MINUS, true);
        }
        if(key == Keys.F1) {
            gameData.getKeys().setKey(GameKeys.F1, true);
        }
        if(key == Keys.SHIFT_LEFT) {
            gameData.getKeys().setKey(GameKeys.SHIFT, true);
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
        if(key == Keys.PLUS) {
            gameData.getKeys().setKey(GameKeys.PLUS, false);
        }
        if(key == Keys.MINUS) {
            gameData.getKeys().setKey(GameKeys.MINUS, false);
        }
        if(key == Keys.F1) {
            gameData.getKeys().setKey(GameKeys.F1, false);
        }
        if(key == Keys.SHIFT_LEFT) {
            gameData.getKeys().setKey(GameKeys.SHIFT, false);
        }
        return true;
    }
    
}
