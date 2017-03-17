package rpg.gameengine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.HashMap;
import java.util.Map;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.util.Logger;
import rpg.common.world.Room;
import rpg.common.world.World;

public class Renderer {

    private SpriteBatch batch;
    private BitmapFont font;
    private Map<Entity, Sprite> sprites;
    private Sprite currentRoom;
    private Sprite previousRoom;

    public Renderer() {
        sprites = new HashMap<>();
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    public void loadSprites(World world) {
        for (Entity entity : world.getCurrentRoom().getEntities()) {
            if (!sprites.containsKey(entity)) {
                try {
                    Texture texture = new Texture(entity.getSpritePath());
                    Sprite sprite = new Sprite(texture);
                    sprite.setSize(entity.getWidth(), entity.getHeight());
                    sprite.setOriginCenter();
                    sprites.put(entity, sprite);
                }
                catch (NullPointerException e) {
                    Logger.log("No spritepath found for entity of type " + entity.getType() + ": " + entity.toString());
                }
            }
        }
    }

    public void loadNewRoomSprite(World world) {
        Entity player = world.getPlayer();
        previousRoom = currentRoom;
        previousRoom.setPosition(previousRoom.getWidth() * (player.getWorldVelocity().getX() * -1), previousRoom.getHeight() * (player.getWorldVelocity().getY() * -1));
        loadRoomSprite(world);
    }

    public void loadRoomSprite(World world) {
        Room newRoom = world.getCurrentRoom();
        currentRoom = new Sprite(new Texture(newRoom.getSpritePath()));
        currentRoom.setPosition(0, 0);
        currentRoom.setSize(newRoom.getWidth(), newRoom.getHeight());
    }

    public void draw(GameData gameData, World world, Camera camera) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.getProjection());
        batch.begin();
        drawMap();
        drawEntitySprites(world);
        batch.end();
    }

    public void drawDebug(GameData gameData, World world, Camera camera, String message) {
        batch.begin();
        batch.setProjectionMatrix(camera.getProjection());
        font.draw(batch, message, 7.5f, 127.5f);
        batch.end();
    }

    private void drawMap() {
        batch.disableBlending();
        if (previousRoom != null) {
            previousRoom.draw(batch);
        }
        currentRoom.draw(batch);
        batch.enableBlending();
    }

    private void drawEntitySprites(World world) {
        for (Entity entity : world.getCurrentRoom().getEntities(EntityType.PLAYER, EntityType.ENEMY)) {
            try {
                Sprite sprite = sprites.get(entity);
                sprite.setRotation(entity.getDirection());
                sprite.setPosition(entity.getRoomPosition().getX() - entity.getWidth() / 2, entity.getRoomPosition().getY() - entity.getHeight() / 2);
                sprite.draw(batch);
            }
            catch (NullPointerException e) {
                Logger.log("No sprite found for entity of type " + entity.getType() + ": " + entity.toString());
            }
        }
        for (Entity entity : world.getCurrentRoom().getEntities(EntityType.MELEE)) {
            Sprite sprite = sprites.get(entity);
            sprite.setRotation(entity.getDirection());
            sprite.setPosition(entity.getRoomPosition().getX() - entity.getWidth() / 2, entity.getRoomPosition().getY() - entity.getHeight() / 2);
            sprite.draw(batch);
        }
    }

}
