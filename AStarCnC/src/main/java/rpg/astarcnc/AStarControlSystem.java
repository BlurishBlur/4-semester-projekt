/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.astarcnc;

import rpg.common.data.GameData;
import rpg.common.services.IEntityProcessingService;
import rpg.common.world.World;
import rpg.common.entities.Entity;
import rpg.commonenemy.Enemy;

/**
 *
 * @author Antonio
 */
public class AStarControlSystem implements IEntityProcessingService{

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getCurrentRoom().getEntities(Enemy.class)){
            
        }
    }
    
}
