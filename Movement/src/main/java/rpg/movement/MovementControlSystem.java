package rpg.movement;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.world.World;
import rpg.common.entities.Entity;
import rpg.common.services.IEntityProcessingService;
import rpg.common.util.Vector;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class MovementControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getCurrentRoom().getEntities()) {
            float deltaTime = gameData.getDeltaTime();
            entity.getVelocity().scalar(deltaTime);
            if (entity.getVelocity().isDiagonal()) {
                entity.getVelocity().normalize(entity.getCurrentMovementSpeed());
            }
            if (entity.getVelocity().isMoving()) {
                entity.setDirection(entity.getVelocity().getAngle());

                if (entity.getVelocity().getX() < 0 && isPointInPolygons(world.getCurrentRoom().getCollidables(), new Vector(entity.getRoomPosition().getX() - entity.getWidth() / 2, entity.getRoomPosition().getY()))) {
                    entity.getVelocity().setX(0);
                    //entity.getRoomPosition().setX(entity.getRoomPosition().getX() + 1);
                }
                else if (entity.getVelocity().getX() > 0 && isPointInPolygons(world.getCurrentRoom().getCollidables(), new Vector(entity.getRoomPosition().getX() + entity.getWidth() / 2, entity.getRoomPosition().getY()))) {
                    entity.getVelocity().setX(0);
                }
                if (entity.getVelocity().getY() < 0 && isPointInPolygons(world.getCurrentRoom().getCollidables(), new Vector(entity.getRoomPosition().getX(), entity.getRoomPosition().getY() - entity.getHeight() / 2))) {
                    entity.getVelocity().setY(0);
                }
                else if (entity.getVelocity().getY() > 0 && isPointInPolygons(world.getCurrentRoom().getCollidables(), new Vector(entity.getRoomPosition().getX(), entity.getRoomPosition().getY() + entity.getHeight() / 2))) {
                    entity.getVelocity().setY(0);
                }

                entity.getRoomPosition().add(entity.getVelocity());

            }

            if (entity.hasWeapon()) {
                entity.getWeapon().getRoomPosition().set(entity.getRoomPosition());
            }
        }
    }

    private boolean isPointInPolygons(List<List<Vector>> polygons, Vector point) {
        if (polygons != null) {
            for (List<Vector> polygon : polygons) {
                if (isPointInPolygon(polygon, point)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPointInPolygon(List<Vector> polygon, Vector point) {
        if (polygon != null) {
            Vector lastVertice = polygon.get(polygon.size() - 1);
            boolean oddNodes = false;
            for (int i = 0; i < polygon.size(); i++) {
                Vector vertice = polygon.get(i);
                if ((vertice.getY() < point.getY() && lastVertice.getY() >= point.getY()) || (lastVertice.getY() < point.getY() && vertice.getY() >= point.getY())) {
                    if (vertice.getX() + (point.getY() - vertice.getY()) / (lastVertice.getY() - vertice.getY()) * (lastVertice.getX() - vertice.getX()) < point.getX()) {
                        oddNodes = !oddNodes;
                    }
                }
                lastVertice = vertice;
            }
            return oddNodes;
        }
        else {
            return false;
        }
    }

}
