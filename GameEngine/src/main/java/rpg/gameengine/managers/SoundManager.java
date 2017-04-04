package rpg.gameengine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.events.Event;
import rpg.common.world.World;

public class SoundManager {

    private Map<Entity, Sound> walkingSounds;
    private Map<String, Sound> punchingSounds;
    private float timer = 0;

    public SoundManager() {
        walkingSounds = new HashMap<>();
        punchingSounds = new HashMap<>();
        punchingSounds.put("NOHIT", Gdx.audio.newSound(Gdx.files.internal("rpg/gameengine/woosh.mp3")));
        punchingSounds.put("HIT", Gdx.audio.newSound(Gdx.files.internal("rpg/gameengine/punch.mp3")));
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("rpg/gameengine/Concentration.mp3"));
        backgroundMusic.play();
    }

    public void loadSounds(World world) {
        for (Entity entity : world.getCurrentRoom().getEntities()) {
            if (!entity.getSounds().isEmpty() && !walkingSounds.containsKey(entity)) {
                Sound toLoad = Gdx.audio.newSound(Gdx.files.internal(entity.getSounds().get("GRASS")));
                walkingSounds.put(entity, toLoad);
            }
        }
    }

    public void playSounds(GameData gameData, World world) {
        playWalkingSounds(world, gameData);
        playPunchSounds(world, gameData);
    }

    private void playWalkingSounds(World world, GameData gameData) {
        for (Entity entity : world.getCurrentRoom().getEntities()) {
            if (timer > calculatePlayRate(entity)) {
                if (!entity.getSounds().isEmpty() && entity.getVelocity().isMoving()) {
                    walkingSounds.get(entity).play();
                    System.out.println("LYD SPILLER");
                    timer = 0;
                }
            }
        }
        timer += gameData.getDeltaTime();
    }

    private float calculatePlayRate(Entity entity) {
        return 40 / entity.getCurrentMovementSpeed();
    }

    private void playPunchSounds(World world, GameData gameData) {
        for (Event event : gameData.getEvents()) {
            punchingSounds.get("NOHIT").play();
        }
    }
}
