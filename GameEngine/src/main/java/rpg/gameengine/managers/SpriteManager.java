package rpg.gameengine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.util.Logger;
import rpg.common.world.Room;
import rpg.common.world.World;

public class SpriteManager {

    private SpriteBatch batch;
    private BitmapFont font;
    private Map<Entity, Sprite> sprites;
    private Map<Entity, TextureAtlas> atlases;
    private Map<Entity, HpBar> hpBars;
    private Sprite currentRoom;
    private Sprite previousRoom;

    public SpriteManager() {
        sprites = new ConcurrentHashMap<>();
        atlases = new ConcurrentHashMap<>();
        hpBars = new ConcurrentHashMap<>();
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    public void loadSprites(World world) {
        world.getCurrentRoom().getEntities().stream().map((entity) -> {
            if (!sprites.containsKey(entity)) {
                loadSprite(entity);
            }
            return entity;
        }).filter((entity) -> (entity.hasWeapon() && !sprites.containsKey(entity.getWeapon()))).forEachOrdered((entity) -> {
            loadSprite(entity.getWeapon());
        });
    }

    private void loadSprite(Entity entity) {
        try {
            Sprite sprite;
            if (entity.isAnimatable()) {
                atlases.put(entity, new TextureAtlas(Gdx.files.internal(entity.getSpritePath())));
                sprite = new Sprite(atlases.get(entity).findRegion("0001"));
            }
            else {
                sprite = new Sprite(new Texture(entity.getSpritePath()));
            }
            if (entity.hasHpBar()) {
                hpBars.put(entity, new HpBar(entity));
            }
            sprite.setSize(entity.getWidth(), entity.getHeight());
            sprite.setOriginCenter();
            sprites.put(entity, sprite);
            System.out.println("Loaded sprite: " + entity.getSpritePath());
        }
        catch (NullPointerException e) {
            Logger.log("Couldn't load sprite, no spritepath found for entity of class " + entity.getClass() + ": " + entity.toString());
        }
    }

    public void loadNewRoomSprite(World world, Camera camera) {
        Entity player = world.getPlayer();
        previousRoom = currentRoom;
        previousRoom.setPosition(previousRoom.getWidth() * (player.getWorldVelocity().getX() * -1), previousRoom.getHeight() * (player.getWorldVelocity().getY() * -1));
        loadRoomSprite(world, camera);
    }

    public void loadRoomSprite(World world, Camera camera) {
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
        drawEntitySprites(gameData, world);
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

    private void drawEntitySprites(GameData gameData, World world) {
        world.getCurrentRoom().getEntities().forEach((entity) -> {
            try {
                Sprite entitySprite = sprites.get(entity);
                if (entity.isAnimatable()) {
                    entitySprite.setRegion(atlases.get(entity).findRegion(String.format("%04d", entity.getCurrentFrame())));
                }
                entitySprite.setRotation(entity.getDirection());
                entitySprite.setPosition(entity.getRoomPosition().getX() - entity.getWidth() / 2, entity.getRoomPosition().getY() - entity.getHeight() / 2);
                entitySprite.draw(batch);
                if (entity.hasWeapon()) {
                    Entity weapon = entity.getWeapon();
                    Sprite weaponSprite = sprites.get(weapon);
                    weaponSprite.setRotation(entity.getDirection());
                    weaponSprite.setPosition(entity.getRoomPosition().getX() - weapon.getWidth() / 2, entity.getRoomPosition().getY() - weapon.getHeight() / 2);
                    weaponSprite.draw(batch);
                }
                if (entity.hasHpBar()) {
                    hpBars.get(entity).draw(batch, gameData);
                }
            }
            catch (NullPointerException e) {
                Logger.log("Couldn't draw sprite, no sprite loaded for entity of class " + entity.getClass() + ": " + entity.toString());
            }
        });
    }

}
