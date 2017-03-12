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
        for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
            enemy.getMovement().set(0, 0);
            enemy.reduceActionTimer(gameData.getDeltaTime());
            if (enemy.getActionTimer() < 0) {
                enemy.setVerticalMovementChance(Math.random());
                enemy.setHorizontalMovementChance(Math.random());
                enemy.setActionTimer((int) (Math.random() * 90) + 10);
            }
            if (enemy.getVerticalMovementChance() < 0.20) { // up
                enemy.getMovement().addY(enemy.getMovementSpeed());
            }
            else if (enemy.getVerticalMovementChance() < 0.40) { // down
                enemy.getMovement().subtractY(enemy.getMovementSpeed());
            }
            if (enemy.getHorizontalMovementChance() < 0.20) { // left
                enemy.getMovement().subtractX(enemy.getMovementSpeed());
            }
            else if (enemy.getHorizontalMovementChance() < 0.40) { // right
                enemy.getMovement().addX(enemy.getMovementSpeed());
            }
        }
    }

    private Entity createEnemy() {
        Entity newEnemy = new Entity();
        newEnemy.setType(EntityType.ENEMY);
        newEnemy.getPosition().set(640, 360);
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
