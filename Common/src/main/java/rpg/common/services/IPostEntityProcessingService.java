package rpg.common.services;

import rpg.common.data.GameData;
import rpg.common.world.World;

public interface IPostEntityProcessingService {
    
    void postProcess(GameData gameData, World world);
    
}
