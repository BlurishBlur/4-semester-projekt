package rpg.gameengine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.entities.Entity;
import rpg.common.util.Message;
import rpg.common.util.MessageHandler;
import rpg.common.world.World;

public class HudManager {

    private Camera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    public HudManager(Camera camera) {
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
    }

    public void draw(GameData gameData, World world) {
        batch.setProjectionMatrix(camera.getProjection());
        batch.begin();
        drawDebug(gameData, world);
        drawMessages(gameData);
        batch.end();
    }

    private void drawMessages(GameData gameData) {
        for (Message message : MessageHandler.getMessages()) {
            font.setColor(font.getColor().r, font.getColor().b, font.getColor().g, 1);
            float yOffset = 0;
            if (message.getDuration() > 0) {
                if (message.getStartDuration() - 1 < message.getDuration()) {
                    float alpha = message.getAndIncreaseAlpha(gameData.getDeltaTime());
                    font.setColor(font.getColor().r, font.getColor().b, font.getColor().g, alpha);
                }
                if (message.getDuration() < 1) {
                    float alpha = message.getAndDecreaseAlpha(gameData.getDeltaTime());
                    font.setColor(font.getColor().r, font.getColor().b, font.getColor().g, alpha);
                }
            }
            font.draw(batch, message.getMessage(), message.getX(), message.getY() + yOffset);
            message.reduceDuration(gameData.getDeltaTime());
            if (message.getDuration() < 0) {
                MessageHandler.getMessages().remove(message);
            }
        }
    }

    private void drawDebug(GameData gameData, World world) {
        if (gameData.getKeys().isPressed(GameKeys.F1)) {
            gameData.setShowDebug(!gameData.showDebug());
            world.getPlayer().setSkillPoints(world.getPlayer().getSkillPoints() + 1);
        }
        if (gameData.showDebug()) {
            Entity player = world.getPlayer();
            String message = "FPS: " + Gdx.graphics.getFramesPerSecond() + "\n"
                    + "Zoom: " + gameData.getCameraZoom() + "\n"
                    + "X: " + player.getRoomPosition().getX() + "\n"
                    + "Y: " + player.getRoomPosition().getY() + "\n"
                    /*+
                    "DX: " + player.getVelocity().getX() + "\n" +
                    "DY: " + player.getVelocity().getY() + "\n" +
                    "Rotation: " + player.getVelocity().getAngle()*/ + "Movement speed: " + player.getCurrentMovementSpeed() + "\n"
                    + "Movement speed modifier: " + player.getMovementSpeedModifier();
            font.draw(batch, message, 7.5f, 127.5f);
        }
    }

}
