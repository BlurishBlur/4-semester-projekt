package rpg.movement;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.World;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.services.IEntityProcessingService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class MovementControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntites(EntityType.ENEMY)) {
            float deltaTime = gameData.getDeltaTime();
            enemy.setDx(0);
            enemy.setDy(0);
            enemy.setDirection(0);
            enemy.reduceActionTimer();
            if (enemy.getActionTimer() < 0) {
                enemy.setVerticalMovementChance(Math.random());
                enemy.setHorizontalMovementChance(Math.random());
                enemy.setActionTimer((int) (Math.random() * 90) + 10);
            }
            if (enemy.getVerticalMovementChance() < 0.20) { // up
                enemy.setDy(enemy.getSpeed() * deltaTime);
                enemy.setDirection(360);
            }
            else if (enemy.getVerticalMovementChance() < 0.40) { // down
                enemy.setDy(-enemy.getSpeed() * deltaTime);
                enemy.setDirection(180);
            }
            if (enemy.getHorizontalMovementChance() < 0.20) { // left
                enemy.setDx(-enemy.getSpeed() * deltaTime);
                int angle = enemy.getDirection() + 90;
                if (angle > 90) {
                    angle = (angle % 360) / 2;
                }
                enemy.setDirection(angle);
            }
            else if (enemy.getHorizontalMovementChance() < 0.40) { // right
                enemy.setDx(enemy.getSpeed() * deltaTime);
                int angle = enemy.getDirection() + 270;
                if (angle > 270) {
                    angle /= 2;
                }
                enemy.setDirection(angle);
            }
            enemy.setDirection(0);

            enemy.setX(enemy.getX() + enemy.getDx());
            enemy.setY(enemy.getY() + enemy.getDy());

            checkEdgeCollision(gameData, enemy);
        }
    }

    private void checkEdgeCollision(GameData gameData, Entity enemy) { // skal flyttes ud i seperat modul, da det skal ske for alle entites
        if (enemy.getX() - (enemy.getWidth() / 2) < 0) {
            enemy.setX(0 + (enemy.getWidth() / 2));
            enemy.setDx(0);
        } else if (enemy.getX() + (enemy.getWidth() / 2) > gameData.getDisplayWidth()) {
            enemy.setX(gameData.getDisplayWidth() - (enemy.getWidth() / 2));
            enemy.setDx(0);
        }
        if (enemy.getY() - (enemy.getHeight() / 2) < 0) {
            enemy.setY(0 + (enemy.getHeight() / 2));
            enemy.setDy(0);
        } else if (enemy.getY() + (enemy.getHeight() / 2) > gameData.getDisplayHeight()) {
            enemy.setY(gameData.getDisplayHeight() - (enemy.getHeight() / 2));
            enemy.setDy(0);
        }
    }

}
