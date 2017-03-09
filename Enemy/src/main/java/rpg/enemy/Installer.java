package rpg.enemy;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    
    private static EnemyControlSystem enemyControlSystem;

    @Override
    public void restored() {
        enemyControlSystem = new EnemyControlSystem();
    }

}
