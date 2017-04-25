package rpg.commonenemy;

import rpg.common.data.GameData;
import rpg.common.entities.Entity;

public interface EnemySPI {
    
    Entity createEnemy(GameData gameData);
    
}
