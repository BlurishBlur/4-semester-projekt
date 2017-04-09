package rpg.movement;

import java.util.ArrayList;
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
        Vector a = new Vector(230, 83);
        Vector b = new Vector(716, 96);
        Vector c = new Vector(738, 579);
        Vector d = new Vector(194, 593);
        List<Vector> p = new ArrayList<>();
        p.add(a);
        p.add(b);
        p.add(c);
        p.add(d);

        for (Entity entity : world.getCurrentRoom().getEntities()) {
            float deltaTime = gameData.getDeltaTime();
            entity.getVelocity().scalar(deltaTime);
            if (entity.getVelocity().isDiagonal()) {
                entity.getVelocity().normalize(entity.getCurrentMovementSpeed());
            }
            if (entity.getVelocity().isMoving()) {
                entity.setDirection(entity.getVelocity().getAngle());
                
                if (entity.getVelocity().getX() < 0 && !isPointInPolygon(p, new Vector(entity.getRoomPosition().getX() - entity.getWidth() / 2, entity.getRoomPosition().getY()))) {
                    entity.getVelocity().setX(0);
                    //entity.getRoomPosition().setX(entity.getRoomPosition().getX() + 1);
                }
                else if (entity.getVelocity().getX() > 0 && !isPointInPolygon(p, new Vector(entity.getRoomPosition().getX() + entity.getWidth() / 2, entity.getRoomPosition().getY()))) {
                    entity.getVelocity().setX(0);
                }
                if (entity.getVelocity().getY() < 0 && !isPointInPolygon(p, new Vector(entity.getRoomPosition().getX(), entity.getRoomPosition().getY() - entity.getHeight() / 2))) {
                    entity.getVelocity().setY(0);
                }
                else if (entity.getVelocity().getY() > 0 && !isPointInPolygon(p, new Vector(entity.getRoomPosition().getX(), entity.getRoomPosition().getY() + entity.getHeight() / 2))) {
                    entity.getVelocity().setY(0);
                }

                entity.getRoomPosition().add(entity.getVelocity());

            }

            if (entity.hasWeapon()) {
                entity.getWeapon().getRoomPosition().set(entity.getRoomPosition());
            }
        }
    }

    private boolean isPointInPolygon(List<Vector> polygon, Vector point) {
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

}
