package rpg.gameengine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.text.NumberFormat;
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
            if (message.getDuration() > 0) {
                if (message.getStartDuration() - 1 < message.getDuration()) {
                    font.setColor(font.getColor().r, font.getColor().b, font.getColor().g, message.getAndIncreaseAlpha(gameData.getDeltaTime()));
                }
                if (message.getDuration() < 1) {
                    font.setColor(font.getColor().r, font.getColor().b, font.getColor().g, message.getAndDecreaseAlpha(gameData.getDeltaTime()));
                }
            }
            font.draw(batch, message.getMessage(), message.getX(), message.getY());
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
        if (world.getPlayer() != null && gameData.showDebug()) {
            Entity player = world.getPlayer();
            /*Runtime runtime = Runtime.getRuntime();

            NumberFormat format = NumberFormat.getInstance();

            StringBuilder sb = new StringBuilder();
            long maxMemory = runtime.maxMemory();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();

            sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
            sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
            sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
            sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
            System.out.println(sb.toString());
            
            System.out.println("Java heap: " + Gdx.app.getJavaHeap());
            System.out.println("Native heap: " + Gdx.app.getNativeHeap());*/
            
            String message = "FPS: " + Gdx.graphics.getFramesPerSecond() + "\n"
                    + "Zoom: " + gameData.getCameraZoom() + "\n"
                    + "X: " + player.getRoomPosition().getX() + "\n"
                    + "Y: " + player.getRoomPosition().getY() + "\n"
                    /*+
                    "DX: " + player.getVelocity().getX() + "\n" +
                    "DY: " + player.getVelocity().getY() + "\n" +
                    "Rotation: " + player.getVelocity().getAngle()*/ + "Movement speed: " + player.getCurrentMovementSpeed() + "\n"
                    + "Movement speed modifier: " + (player.getSprintModifier() * player.getMovementSpeedModifier());
            font.draw(batch, message, 7.5f, 127.5f); // y += 20 for hver linje
        }
    }

}
