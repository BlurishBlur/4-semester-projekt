package rpg.common.services;

import rpg.common.data.GameData;
import rpg.common.data.World;

public interface IEntityProcessingService {
    
    void process(GameData gameData, World world);
    
}
