/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.gameengine.core.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.world.World;
import rpg.gameengine.managers.Camera;
import rpg.gameengine.managers.GameInputProcessor;

public class Hud {
    
    private Camera camera;
    private TextButton textButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private GameInputProcessor gameInputProcessor;
    private SpriteBatch hudBatch;
    private Sprite hudSprite;
    private BitmapFont font;
    private GameData gameData;
    private boolean activeHud;
    
    public Hud(Camera camera, GameInputProcessor gameInputProcessor, GameData gameData) {
        this.camera = camera;
        this.gameInputProcessor = gameInputProcessor;
        this.hudBatch = new SpriteBatch();
        this.font = new BitmapFont();
        this.gameData = gameData;

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        textButton = new TextButton("Button1", textButtonStyle);
        textButton.setPosition(150, gameData.getDisplayHeight() - 75);
            
        textButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y){
                //world.getPlayer().setCurrentHealth(world.getPlayer().getCurrentHealth()+10);
                System.out.println("yo"); 
            }

        });
        gameInputProcessor.addActor(textButton);
            
    }
    
    public void drawHUD(World world) {
        if(gameData.getKeys().isPressed(GameKeys.H)){                      
            activeHud = !activeHud;
        }
        
        if (activeHud){
            hudBatch.begin();
                hudBatch.setProjectionMatrix(camera.getProjection());
                Texture texture = new Texture("rpg/gameengine/hud2.jpg");
                hudSprite = new Sprite(texture);
                hudSprite.setSize(200, 400);
                hudSprite.setPosition(40, gameData.getDisplayHeight() - 440);
                hudSprite.draw(hudBatch);
                
                
                font.draw(hudBatch, "Speed " + world.getPlayer().getCurrentHealth(), 60, gameData.getDisplayHeight() - 60);
            hudBatch.end();
            
            gameInputProcessor.draw();
            
        }
    }
    
    
}
