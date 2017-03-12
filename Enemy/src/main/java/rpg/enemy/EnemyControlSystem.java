package rpg.enemy;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.World;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.services.IGamePluginService;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class)
})
public class EnemyControlSystem implements IGamePluginService {
    
    private Entity enemy;
    
    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemy();
        world.addEntity(enemy);
    }
    
    private Entity createEnemy() {
        Entity newEnemy = new Entity();
        newEnemy.setType(EntityType.ENEMY);
        newEnemy.setX(640);
        newEnemy.setY(360);
        newEnemy.setMovementSpeed(100);
        newEnemy.setMaxHealth(50);
        newEnemy.setCurrentHealth(newEnemy.getMaxHealth());
        newEnemy.setWidth(30);
        newEnemy.setHeight(30);
        newEnemy.setSpritePath("rpg/gameengine/enemy.png");
        return newEnemy;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
    }
    
}
