package rpg.common.services;

import rpg.common.data.GameData;
import rpg.common.data.World;

public interface IGamePluginService {
    
    void start(GameData gameData, World world);
    void stop(GameData gameData, World world);
    
}
