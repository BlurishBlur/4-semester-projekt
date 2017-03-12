package rpg.enemy;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.World;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)
})
public class EnemyControlSystem implements IEntityProcessingService, IGamePluginService {

    private Entity enemy;

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemy();
        world.addEntity(enemy);
    }

    @Override
    public void process(GameData gameData, World world) {
        float deltaTime = gameData.getDeltaTime();
        for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
            enemy.setDx(0);
            enemy.setDy(0);
            enemy.reduceActionTimer();
            if (enemy.getActionTimer() < 0) {
                enemy.setVerticalMovementChance(Math.random());
                enemy.setHorizontalMovementChance(Math.random());
                enemy.setActionTimer((int) (Math.random() * 90) + 10);
            }
            if (enemy.getVerticalMovementChance() < 0.20) { // up
                enemy.setDy(enemy.getMovementSpeed() * deltaTime);
            }
            else if (enemy.getVerticalMovementChance() < 0.40) { // down
                enemy.setDy(-enemy.getMovementSpeed() * deltaTime);
            }
            if (enemy.getHorizontalMovementChance() < 0.20) { // left
                enemy.setDx(-enemy.getMovementSpeed() * deltaTime);
            }
            else if (enemy.getHorizontalMovementChance() < 0.40) { // right
                enemy.setDx(enemy.getMovementSpeed() * deltaTime);
            }
        }
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
