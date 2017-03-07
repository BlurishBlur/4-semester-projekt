package rpg.player;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    
    private static PlayerControlSystem playerControlSystem;

    @Override
    public void restored() {
        playerControlSystem = new PlayerControlSystem();
    }

}
