package rpg.enemy;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.entities.Entity;
import rpg.common.events.Event;
import rpg.common.events.EventType;
import rpg.common.services.IEntityProcessingService;
import rpg.common.services.IGamePluginService;
import rpg.common.world.World;
import rpg.commonenemy.Enemy;
import rpg.commonenemy.EnemySPI;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)
})
public class EnemyControlSystem implements IEntityProcessingService, IGamePluginService, EnemySPI {

    private Entity enemy;

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemy(gameData);
        world.getCurrentRoom().addEntity(enemy);
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getCurrentRoom().getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) entity;
            if (enemy.getCurrentHealth() <= 0) {
                System.out.println("Enemy died");
                gameData.addEvent(new Event(EventType.ENEMY_DIED, enemy));
                world.getCurrentRoom().removeEntity(enemy);
            }
            enemy.getVelocity().set(0, 0);
            handleEdgeCollision(gameData, world, enemy);
            enemy.reduceActionTimer(gameData.getDeltaTime());
            if (enemy.getActionTimer() < 0) {
                enemy.setVerticalMovementChance(Math.random());
                enemy.setHorizontalMovementChance(Math.random());
                enemy.setActionTimer((int) (Math.random() * 4) + 1);
            }

            if (enemy.getNextStep() != null) {
                int diffX = (int) (enemy.getNextStep().getX() - ((int)enemy.getRoomPosition().getX() / World.SCALE));
                int diffY = (int) (enemy.getNextStep().getY() - ((int)enemy.getRoomPosition().getY() / World.SCALE));

                //System.out.println("Diff: " + diffX + " " + diffY);

                if (diffX < 0) {
                    enemy.getVelocity().subtractX(enemy.getCurrentMovementSpeed());
                }
                else if (diffX > 0) {
                    enemy.getVelocity().addX(enemy.getCurrentMovementSpeed());
                }
                if (diffY < 0) {
                    enemy.getVelocity().subtractY(enemy.getCurrentMovementSpeed());
                } else if (diffY > 0) {
                    enemy.getVelocity().addY(enemy.getCurrentMovementSpeed());
                }
            } else {
                /*
            if (enemy.getVerticalMovementChance() < 0.20) { // up
                //enemy.getVelocity().addY(enemy.getCurrentMovementSpeed());
                
            }
            else if (enemy.getVerticalMovementChance() < 0.40) { // down
                enemy.getVelocity().subtractY(enemy.getCurrentMovementSpeed());
            }
            if (enemy.getHorizontalMovementChance() < 0.20) { // left
                enemy.getVelocity().subtractX(enemy.getCurrentMovementSpeed());
            }
            else if (enemy.getHorizontalMovementChance() < 0.40) { // right
                enemy.getVelocity().addX(enemy.getCurrentMovementSpeed());
            }*/
            }

            if (gameData.getKeys().isPressed(GameKeys.K)) {
                enemy.setCurrentHealth(0);
                System.out.println("Killing all enemies");
            }
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.getCurrentRoom().removeEntity(enemy);
    }

    @Override
    public Entity createEnemy(GameData gameData) {
        Entity newEnemy = new Enemy();
        //newEnemy.getRoomPosition().set(640, 360);
        newEnemy.getRoomPosition().set(368, 526);
        newEnemy.setDefaultMovementSpeed(100);
        newEnemy.setCurrentMovementSpeed(newEnemy.getDefaultMovementSpeed());
        newEnemy.setMaxHealth(50);
        newEnemy.setCurrentHealth(newEnemy.getMaxHealth());
        newEnemy.setSize(50, 50);
        newEnemy.setSpritePath("rpg/gameengine/enemy.png");
        return newEnemy;
    }

    private void handleEdgeCollision(GameData gameData, World world, Entity enemy) {
        if (enemy.getRoomPosition().getX() - (enemy.getWidth() / 2) < 0) {
            enemy.getRoomPosition().setX(0 + (enemy.getWidth() / 2));
            enemy.getVelocity().setX(0);
        } else if (enemy.getRoomPosition().getX() + (enemy.getWidth() / 2) > world.getCurrentRoom().getWidth()) {
            enemy.getRoomPosition().setX(world.getCurrentRoom().getWidth() - (enemy.getWidth() / 2));
            enemy.getVelocity().setX(0);
        }
        if (enemy.getRoomPosition().getY() - (enemy.getHeight() / 2) < 0) {
            enemy.getRoomPosition().setY(0 + (enemy.getHeight() / 2));
            enemy.getVelocity().setY(0);
        } else if (enemy.getRoomPosition().getY() + (enemy.getHeight() / 2) > world.getCurrentRoom().getHeight()) {
            enemy.getRoomPosition().setY(world.getCurrentRoom().getHeight() - (enemy.getHeight() / 2));
            enemy.getVelocity().setY(0);
        }
    }

}
