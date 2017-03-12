package rpg.common.services;

import rpg.common.data.GameData;
import rpg.common.data.World;

public interface IPostEntityProcessingService {
    
    void process(GameData gameData, World world);
    
}
