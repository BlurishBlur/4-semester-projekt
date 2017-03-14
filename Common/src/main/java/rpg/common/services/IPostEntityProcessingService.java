package rpg.common.services;

import rpg.common.data.GameData;
import rpg.common.world.World;

public interface IPostEntityProcessingService {
    
    void process(GameData gameData, World world);
    
}
