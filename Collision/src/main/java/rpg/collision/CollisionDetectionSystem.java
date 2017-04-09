package rpg.collision;

import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.services.IPostEntityProcessingService;
import rpg.common.util.Vector;
import rpg.common.world.World;

@ServiceProviders(value = {
    @ServiceProvider(service = IPostEntityProcessingService.class)
})
public class CollisionDetectionSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        Vector a = new Vector(25, 25);
        Vector b = new Vector(25, 150);
        Vector c = new Vector(150, 150);
        Vector d = new Vector(150, 25);
        List<Vector> p = new ArrayList<>();
        p.add(a);
        p.add(b);
        p.add(c);
        p.add(d);
        
        Vector point = world.getPlayer().getRoomPosition();
        
        System.out.println(isPointInPolygon(p, point));
        
        
        for (Entity entity : world.getCurrentRoom().getEntities()) {
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
