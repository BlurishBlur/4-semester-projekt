package rpg.gameengine.core;

import rpg.gameengine.managers.Camera;
import rpg.gameengine.managers.Renderer;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
<<<<<<< HEAD
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
=======
>>>>>>> master
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.world.World;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;
import rpg.common.services.IPostEntityProcessingService;
import rpg.common.util.Logger;
import rpg.common.world.Room;
import rpg.gameengine.managers.GameInputProcessor;

public class Game implements ApplicationListener {

<<<<<<< HEAD
    private TextButton textButton;
    private TextButtonStyle textButtonStyle;
    private BitmapFont buttonFont;
    
    private OrthographicCamera playerCamera;
    private OrthographicCamera hudCamera;
=======
    private Camera playerCamera;
    private Camera hudCamera;
    private Renderer renderer;
>>>>>>> master
    private Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private World world = new World();
    private int fps;
    private int frames;
    private long fpsTimer;
<<<<<<< HEAD
    private float cameraPanTime;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private Sprite hudSprite;
    private BitmapFont font;
    private Map<Entity, Sprite> sprites;
    private Sprite currentRoom, previousRoom;
    private boolean activeHud = true;
    private GameInputProcessor gameInputProcessor;
=======
>>>>>>> master

    @Override
    public void create() {
        fpsTimer = System.currentTimeMillis();
        Gdx.input.setInputProcessor(gameInputProcessor = new GameInputProcessor(gameData));
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setCameraZoom(1.50f);
<<<<<<< HEAD
        sprites = new HashMap<>();
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        font = new BitmapFont();
=======
>>>>>>> master

        for (IGamePluginService plugin : getGamePluginServices()) {
            plugin.start(gameData, world);
        }

        world.setCurrentRoom(world.getPlayer().getWorldPosition());
        renderer = new Renderer();
        renderer.loadRoomSprite(world);

<<<<<<< HEAD
        playerCamera = new OrthographicCamera(gameData.getDisplayWidth() / gameData.getCameraZoom(), gameData.getDisplayHeight() / gameData.getCameraZoom());
        playerCamera.position.set(playerCamera.viewportWidth / 2, playerCamera.viewportHeight / 2, 0);
        playerCamera.update();
        hudCamera = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        hudCamera.position.set(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2, 0);
        hudCamera.update();
        
        
        buttonFont = new BitmapFont();
            textButtonStyle = new TextButtonStyle();
            textButtonStyle.font = buttonFont;

            textButton = new TextButton("Button1", textButtonStyle);
            textButton.setPosition(150, gameData.getDisplayHeight() - 75);
            
            textButton.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y){
                    world.getPlayer().setCurrentHealth(world.getPlayer().getCurrentHealth()+10);
                    System.out.println("yo"); 
                }

            });
            gameInputProcessor.addActor(textButton);
    }

    private void loadRoomSprite() {
        Room room = world.getCurrentRoom();
        previousRoom = currentRoom;
        if (previousRoom != null) {
            Entity player = world.getPlayer();
            previousRoom.setPosition(previousRoom.getWidth() * (player.getWorldVelocity().getX() * -1), previousRoom.getHeight() * (player.getWorldVelocity().getY() * -1));
        }
        currentRoom = new Sprite(new Texture(room.getSpritePath()));
        currentRoom.setPosition(0, 0);
        currentRoom.setSize(room.getWidth(), room.getHeight());
=======
        playerCamera = new Camera(gameData.getDisplayWidth() / gameData.getCameraZoom(), gameData.getDisplayHeight() / gameData.getCameraZoom(), world.getPlayer());
        playerCamera.update(gameData, world);
        hudCamera = new Camera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        hudCamera.update(gameData, world);
>>>>>>> master
    }

    @Override
    public void render() {
        calculateFPS();
        gameData.setDeltaTime(Math.min(Gdx.graphics.getDeltaTime(), 0.0167f));
        update();
<<<<<<< HEAD
        handlePlayerCamera();
        loadSprites();
        draw();
        if(gameData.getKeys().isClicked()){
            System.out.println("click");
        }
=======
        updatePlayerCamera();
        renderer.loadSprites(world);
        renderer.draw(gameData, world, playerCamera);
        drawHud();
>>>>>>> master
        gameData.getKeys().update();
        
        
    }

    private void updatePlayerCamera() {
        if (gameData.isChangingRoom() && world.getRoom(playerCamera.getTarget().getWorldPosition()) != world.getCurrentRoom()) {
            playerCamera.initializeRoomChange(world);
            renderer.loadNewRoomSprite(world);
        }
        playerCamera.update(gameData, world);
    }

    private void update() {
        for (IEntityProcessingService processor : getEntityProcessingServices()) {
            processor.process(gameData, world);
        }
        for (IPostEntityProcessingService postProcessor : getPostEntityProcessingServices()) {
            postProcessor.process(gameData, world);
        }
    }

<<<<<<< HEAD
    private void loadSprites() {
        for (Entity entity : world.getEntities()) {
            if (!sprites.containsKey(entity)) {
                try {
                    Texture texture = new Texture(entity.getSpritePath());
                    Sprite sprite = new Sprite(texture);
                    sprite.setSize(entity.getWidth(), entity.getHeight());
                    sprite.setOriginCenter();
                    sprites.put(entity, sprite);
                }
                catch(NullPointerException e) {
                    Logger.log("No spritepath found for entity of type " + entity.getType() + ": " + entity.toString());
                }
            }
        }
    }

    private void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(playerCamera.combined);
        batch.begin();
        drawMap();
        drawEntitySprites();
        drawDebug();
        batch.end();
        drawHUD();
    }
    
    private void drawHUD() {
        if(gameData.getKeys().isPressed(GameKeys.H)){                      
            activeHud = !activeHud;
        }
        
        if (activeHud){
            hudBatch.begin();
                hudBatch.setProjectionMatrix(hudCamera.combined);
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
    
    private void increaseHP(){
        world.getPlayer().setCurrentHealth(world.getPlayer().getCurrentHealth()+10);
        System.out.println("yo");
    }

    private void drawDebug() {
=======
    public void drawHud() {
>>>>>>> master
        if (gameData.getKeys().isPressed(GameKeys.F1)) {
            gameData.setShowDebug(!gameData.showDebug());
        }
        if (gameData.showDebug()) {
            Entity player = world.getPlayer();
            String message = "FPS: " + fps + "\n"
                    + "Zoom: " + gameData.getCameraZoom() + "\n"
                    + "X: " + player.getRoomPosition().getX() + "\n"
                    + "Y: " + player.getRoomPosition().getY() + "\n"
                    /*+
                    "DX: " + player.getVelocity().getX() + "\n" +
                    "DY: " + player.getVelocity().getY() + "\n" +
                    "Rotation: " + player.getVelocity().getAngle()*/ + "Movement speed: " + player.getMovementSpeed() + "\n"
                    + "Movement speed modifier: " + player.getMovementSpeedModifier();
            renderer.drawHud(gameData, world, hudCamera, message);
        }
    }

    private void calculateFPS() {
        frames++;
        if (System.currentTimeMillis() - fpsTimer > 1000) {
            fps = frames;
            frames = 0;
            fpsTimer = System.currentTimeMillis();
        }
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    private Collection<? extends IGamePluginService> getGamePluginServices() {
        return lookup.lookupAll(IGamePluginService.class);
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return lookup.lookupAll(IPostEntityProcessingService.class);
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
