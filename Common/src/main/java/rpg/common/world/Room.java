package rpg.common.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import rpg.common.entities.Entity;
import rpg.common.util.Polygon;
import rpg.common.util.Vector;

public class Room implements Serializable {

    private final Map<String, Entity> entities = new ConcurrentHashMap<>();
    private int width = 1280;
    private int height = 720;
    private int x;
    private int y;
    private boolean canExitUp;
    private boolean canExitDown;
    private boolean canExitLeft;
    private boolean canExitRight;
    private String spritePath;
    private Stack<Polygon> collidables;

    public Room(String spritePath) {
        this.spritePath = spritePath;
    }

    public Room() {
        collidables = new Stack();
    }

    public boolean blocked(Entity entity, float targetX, float targetY) {
        /*return isPointInPolygons(collidables,
                new Vector(entity.getRoomPosition().getX() + targetX, entity.getRoomPosition().getY() + targetY));*/
        return isPointInPolygons(collidables, new Vector(targetX, targetY));
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Stack<Polygon> getCollidables() {
        return collidables;
    }

    public void setCollidables(Stack<Polygon> collidables) {
        this.collidables = collidables;
    }

    public void canExitUp(boolean canExitUp) {
        this.canExitUp = canExitUp;
    }

    public void canExitDown(boolean canExitDown) {
        this.canExitDown = canExitDown;
    }

    public void canExitLeft(boolean canExitLeft) {
        this.canExitLeft = canExitLeft;
    }

    public void canExitRight(boolean canExitRight) {
        this.canExitRight = canExitRight;
    }

    public boolean canExitUp() {
        return canExitUp;
    }

    public boolean canExitDown() {
        return canExitDown;
    }

    public boolean canExitLeft() {
        return canExitLeft;
    }

    public boolean canExitRight() {
        return canExitRight;
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getID(), entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity.getID());
    }

    public void removeEntity(String entityID) {
        entities.remove(entityID);
    }

    public Entity getEntity(String entityID) {
        return entities.get(entityID);
    }

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public <E extends Entity> List<Entity> getEntities(Class<E>... entityTypes) {
        List<Entity> results = new ArrayList<>();
        for (Entity entity : entities.values()) {
            for (Class<E> entityType : entityTypes) {
                if (entityType.equals(entity.getClass())) {
                    results.add(entity);
                }
            }
        }
        return results;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }
    
    public float getCost(Entity entity, int sx, int sy, int tx, int ty) {
        return 1;
    }

}
