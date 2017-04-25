package rpg.gameengine.managers;

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

public class SkillpointsHud {

    private Camera camera;
    private TextButton healthSkillUpButton;
    private TextButton movementSkillUpButton;
    private TextButton armorSkillUpButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private GameInputProcessor gameInputProcessor;
    private SpriteBatch hudBatch;
    private Sprite hudBackground;
    private BitmapFont font;
    private GameData gameData;
    private World world;
    private boolean activeHud;

    public SkillpointsHud(Camera camera, GameInputProcessor gameInputProcessor, GameData gameData, World world) {
        this.camera = camera;
        this.gameInputProcessor = gameInputProcessor;
        this.hudBatch = new SpriteBatch();
        this.font = new BitmapFont();
        this.gameData = gameData;
        this.world = world;
        initializeHud();
    }

    public void drawSkillPointsHud() {
        if (gameData.getKeys().isPressed(GameKeys.H)) {
            activeHud = !activeHud;
        }
        if (activeHud) {
            hudBatch.setProjectionMatrix(camera.getProjection());
            hudBatch.begin();
            hudBackground.draw(hudBatch);

            font.draw(hudBatch, "Skill points: " + world.getPlayer().getSkillPoints(), 50, gameData.getDisplayHeight() - 60);
            font.draw(hudBatch, "Max health: " + world.getPlayer().getCurrentHealth(), 50, gameData.getDisplayHeight() - 90);
            font.draw(hudBatch, "Speed modifier: " + world.getPlayer().getMovementSpeedModifier(), 50, gameData.getDisplayHeight() - 110);
            font.draw(hudBatch, "Damage reduction: " + world.getPlayer().getArmor() + "%", 50, gameData.getDisplayHeight() - 130);
            hudBatch.end();
            
            if (world.getPlayer().getSkillPoints() > 0) {
                drawSkillUpButtons();
            }
        }
    }

    private void drawSkillUpButtons() {
        gameInputProcessor.draw();
    }

    private void initializeHud() {
        Texture texture = new Texture("rpg/gameengine/hud2.jpg");
        hudBackground = new Sprite(texture);
        hudBackground.setSize(200, 400);
        hudBackground.setPosition(40, gameData.getDisplayHeight() - 440);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        healthSkillUpButton = new TextButton("+", textButtonStyle);
        healthSkillUpButton.setPosition(155, gameData.getDisplayHeight() - 104);
        healthSkillUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (world.getPlayer().getSkillPoints() > 0) {
                    world.getPlayer().setCurrentHealth(world.getPlayer().getCurrentHealth() + 10);
                    world.getPlayer().setSkillPoints(world.getPlayer().getSkillPoints() - 1);
                }
            }
        });

        movementSkillUpButton = new TextButton("+", textButtonStyle);
        movementSkillUpButton.setPosition(180, gameData.getDisplayHeight() - 124);
        movementSkillUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (world.getPlayer().getSkillPoints() > 0) {
                    world.getPlayer().setMovementSpeedModifier(world.getPlayer().getMovementSpeedModifier() + 0.10f);
                    world.getPlayer().setSkillPoints(world.getPlayer().getSkillPoints() - 1);
                }
            }
        });

        armorSkillUpButton = new TextButton("+", textButtonStyle);
        armorSkillUpButton.setPosition(200, gameData.getDisplayHeight() - 144);
        armorSkillUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (world.getPlayer().getSkillPoints() > 0) {
                    world.getPlayer().setArmor(world.getPlayer().getArmor() + 2);
                    world.getPlayer().setSkillPoints(world.getPlayer().getSkillPoints() - 1);
                }
            }
        });

        gameInputProcessor.addActor(armorSkillUpButton);
        gameInputProcessor.addActor(movementSkillUpButton);
        gameInputProcessor.addActor(healthSkillUpButton);
    }

}
