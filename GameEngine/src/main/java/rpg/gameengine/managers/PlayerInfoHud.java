package rpg.gameengine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import rpg.common.data.GameData;
import rpg.common.world.World;
import rpg.commonplayer.Player;

public class PlayerInfoHud {

    private Camera camera;
    private GameInputProcessor gameInputProcessor; 
    private SpriteBatch hudBatch;
    private Sprite hudBackground;
    private BitmapFont font;
    private GameData gameData;
    private World world;
    private ShapeRenderer shape;

    public PlayerInfoHud(Camera camera, GameInputProcessor gameInputProcessor, GameData gameData, World world) {
        this.camera = camera;
        this.gameData = gameData;
        this.world = world;
        this.hudBatch = new SpriteBatch();
        this.font = new BitmapFont();
        this.shape = new ShapeRenderer();
        this.gameInputProcessor = gameInputProcessor;
        // initializeHud();
    }

    private void initializeHud() {
        // TODO evt hud billeder
    }

    public void drawInfoHud() {

        
        shape.setProjectionMatrix(camera.getProjection());
        shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setAutoShapeType(true);
            shape.setColor(0, 0, 0, 1);
            shape.rect(0, gameData.getDisplayHeight() - 80, gameData.getDisplayWidth(), 80);
            shape.setColor(1, 1, 1, 1);
            shape.set(ShapeRenderer.ShapeType.Line);
            shape.rect(1125, gameData.getDisplayHeight() - 55, 100, 20);
            shape.set(ShapeRenderer.ShapeType.Filled);
            shape.rect(1125, gameData.getDisplayHeight() - 55, 10, 20);                     
        shape.end();

        hudBatch.setProjectionMatrix(camera.getProjection());
        hudBatch.begin();
        //hudBackground.draw(hudBatch);
            font.draw(hudBatch, "Health: " + world.getPlayer().getCurrentHealth(), 50, gameData.getDisplayHeight() - 35);
            font.draw(hudBatch, "Currency: " + world.getPlayer().getCurrency(), 600, gameData.getDisplayHeight() - 35);
            font.draw(hudBatch, "Level: " + ((Player) world.getPlayer()).getPlayerLevel(), 1150, gameData.getDisplayHeight() - 20);
            font.draw(hudBatch, ((Player) world.getPlayer()).getPlayerExperiancePoints() + "/100", 1155, gameData.getDisplayHeight() - 60);
        hudBatch.end();
    }

}
