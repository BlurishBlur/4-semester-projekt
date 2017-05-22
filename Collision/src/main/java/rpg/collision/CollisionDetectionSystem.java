package rpg.collision;

import java.util.Stack;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.services.IEntityProcessingService;
import rpg.common.util.Polygon;
import rpg.common.util.Vector;
import rpg.common.world.World;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class CollisionDetectionSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getCurrentRoom().getEntities()) {
            if (entity.getVelocity().isMoving()) {
                if (entity.getVelocity().getX() < 0 && isPointInPolygons(world.getCurrentRoom().getCollidables(), 
                        new Vector(entity.getRoomPosition().getX() - entity.getWidth() / 2, entity.getRoomPosition().getY()))) {
                    entity.getVelocity().setX(0);
                }
                if (entity.getVelocity().getX() > 0 && isPointInPolygons(world.getCurrentRoom().getCollidables(), 
                        new Vector(entity.getRoomPosition().getX() + entity.getWidth() / 2, entity.getRoomPosition().getY()))) {
                    entity.getVelocity().setX(0);
                }
                if (entity.getVelocity().getY() < 0 && isPointInPolygons(world.getCurrentRoom().getCollidables(), 
                        new Vector(entity.getRoomPosition().getX(), entity.getRoomPosition().getY() - entity.getHeight() / 2))) {
                    entity.getVelocity().setY(0);
                }
                if (entity.getVelocity().getY() > 0 && isPointInPolygons(world.getCurrentRoom().getCollidables(), 
                        new Vector(entity.getRoomPosition().getX(), entity.getRoomPosition().getY() + entity.getHeight() / 2))) {
                    entity.getVelocity().setY(0);
                }
            }
        }
    }

    private boolean isPointInPolygons(Stack<Polygon> polygons, Vector point) {
        if (polygons != null) {
            for (Polygon polygon : polygons) {
                if (isPointInPolygon(polygon, point)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPointInPolygon(Polygon polygon, Vector point) {
        Vector lastVertice = polygon.getLast();
        boolean intersects = false;
        for (int i = 0; i < polygon.size(); i++) {
            Vector vertice = polygon.get(i);
            if ((vertice.getY() < point.getY() && lastVertice.getY() >= point.getY()) || (lastVertice.getY() < point.getY() && vertice.getY() >= point.getY())) {
                if (vertice.getX() + (point.getY() - vertice.getY()) / (lastVertice.getY() - vertice.getY()) * (lastVertice.getX() - vertice.getX()) < point.getX()) {
                    intersects = !intersects;
                }
            }
            lastVertice = vertice;
        }
        return intersects;
    }

}
