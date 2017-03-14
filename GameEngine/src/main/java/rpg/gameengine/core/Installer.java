package rpg.gameengine.core;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    
    private static Game game;

    @Override
    public void restored() {
        game = new Game();
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "RPG";
        cfg.width = 1280;
        cfg.height = 720;
        cfg.useGL30 = false;
        cfg.foregroundFPS = 60;
        cfg.backgroundFPS = 60;
        cfg.vSyncEnabled = true;
        
        new LwjglApplication(game, cfg);
    }

}
