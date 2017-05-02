/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.astarcnc;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.services.IEntityProcessingService;
import rpg.common.world.World;
import rpg.common.entities.Entity;
import rpg.commonenemy.Enemy;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class AStarControlSystem implements IEntityProcessingService{

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getCurrentRoom().getEntities(Enemy.class)){
            Enemy enemy = (Enemy) entity;
            AStarPathFinder pathFinder = new AStarPathFinder(world.getCurrentRoom(), 1500, true);
            Path path = pathFinder.findPath(enemy, (int) enemy.getRoomPosition().getX(),
                    (int) enemy.getRoomPosition().getY(), (int) world.getPlayer().getRoomPosition().getX(),
                    (int) world.getPlayer().getRoomPosition().getY());
            
            enemy.setNextStep(path.getStep(0));
        }
    }
    
}
