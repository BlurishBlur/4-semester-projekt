package rpg.gameengine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.events.Event;
import rpg.common.events.EventType;
import rpg.common.world.World;

public class SoundManager {

    private Map<Entity, Sound> walkingSounds;
    private Map<Entity, Sound> weaponMissSounds;
    private Map<Entity, Sound> weaponHitSounds;
    private Map<String, Sound> miscSounds;
    private float timer = 0;

    public SoundManager() {
        walkingSounds = new HashMap<>();
        weaponMissSounds = new HashMap<>();
        weaponHitSounds = new HashMap<>();
        miscSounds = new HashMap<>();
        playMusic();
        loadMiscSounds();
    }
    
    private void playMusic(){
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("rpg/gameengine/Concentration.mp3"));
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    public void loadSounds(World world) {
        loadWalkingSounds(world);
        loadCombatSounds(world);
    }
    
    private void loadWalkingSounds(World world) {
        for (Entity entity : world.getCurrentRoom().getEntities()) {
            if (!entity.getSounds().isEmpty() && !walkingSounds.containsKey(entity)) {
                Sound toLoad = Gdx.audio.newSound(Gdx.files.internal(entity.getSounds().get("GRASS")));
                walkingSounds.put(entity, toLoad);
            }
        }
    }
    
    private void loadMiscSounds() {
        miscSounds.putIfAbsent("COIN_PICKUP", Gdx.audio.newSound(Gdx.files.internal("rpg/gameengine/coinsound.wav")));
        miscSounds.putIfAbsent("XP_PICKUP", Gdx.audio.newSound(Gdx.files.internal("rpg/gameengine/xp_lyd.mp3")));
        miscSounds.putIfAbsent("HIT_HAND", Gdx.audio.newSound(Gdx.files.internal("rpg/gameengine/punch.mp3")));
        miscSounds.putIfAbsent("MAN_HIT", Gdx.audio.newSound(Gdx.files.internal("rpg/gameengine/man_hit.wav")));
        miscSounds.putIfAbsent("MAN_DYING", Gdx.audio.newSound(Gdx.files.internal("rpg/gameengine/man_dying.wav")));
        miscSounds.putIfAbsent("LEVEL_UP", Gdx.audio.newSound(Gdx.files.internal("rpg/gameengine/level_up_sound.wav")));
    }
    
    private void loadCombatSounds(World world) {
        for (Entity entity : world.getCurrentRoom().getEntities()) {
            if (entity.hasWeapon() && !entity.getWeapon().getSounds().isEmpty() && !weaponMissSounds.containsKey(entity.getWeapon())) {
                Entity weapon = entity.getWeapon();
                if (weapon.getSounds().containsKey("MISS")) {
                    Sound soundToLoad = Gdx.audio.newSound(Gdx.files.internal(weapon.getSounds().get("MISS")));
                    weaponMissSounds.put(weapon, soundToLoad);
                }
                if (weapon.getSounds().containsKey("HIT")) {
                    Sound soundToLoad = Gdx.audio.newSound(Gdx.files.internal(weapon.getSounds().get("HIT")));
                    weaponHitSounds.put(weapon, soundToLoad);
                }
            }
        }
    }

    public void playSounds(GameData gameData, World world) {
        playWalkingSounds(world, gameData);
        playCombatSounds(gameData);
        playMiscSounds(gameData);
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

    private void playCombatSounds(GameData gameData) {
        for (Event event : gameData.getEvents()) {
            if(event.getType() == EventType.WEAPON_USE && weaponMissSounds.containsKey(event.getEntity())){
                weaponMissSounds.get(event.getEntity()).play();
                gameData.removeEvent(event);
            }
            if(event.getType() == EventType.WEAPON_HIT && weaponHitSounds.containsKey(event.getEntity())){
                weaponHitSounds.get(event.getEntity()).play(0.7f);
                miscSounds.get("MAN_HIT").play(0.3f);
                gameData.removeEvent(event);
            }
        }
    }
    
    private void playMiscSounds(GameData gameData){
        for (Event event : gameData.getEvents()) {
            if(event.getType() == EventType.COIN_PICKUP){
                miscSounds.get("COIN_PICKUP").play();
                gameData.removeEvent(event);
            }
            if(event.getType() == EventType.XP_PICKUP){
                miscSounds.get("XP_PICKUP").play();
                gameData.removeEvent(event);
            }
            if(event.getType() == EventType.ENEMY_DIED){
                new Thread(() -> {
                    miscSounds.get("MAN_DYING").play(0.5f);
                }).start();
                gameData.removeEvent(event);
            }
            if(event.getType() == EventType.LEVEL_UP){
                miscSounds.get("LEVEL_UP").play(0.5f);
                gameData.removeEvent(event);
            }
        }
    }
}
