package rpg.common.services;

import rpg.common.data.GameData;
import rpg.common.world.World;

public interface IEntityProcessingService {
    
    void process(GameData gameData, World world);
    
}
