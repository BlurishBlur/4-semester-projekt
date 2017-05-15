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
    
    public boolean isNodeBlocked(int x, int y) {
        boolean blocked = false;
        
        if(isPointInPolygonsAI(collidables, new Vector(x,y))) {
            blocked = true;
        }
        if(isPointInPolygonsAI(collidables, new Vector(x+World.SCALE,y))) {
            blocked = true;
        }
        if(isPointInPolygonsAI(collidables, new Vector(x,y+World.SCALE))) {
            blocked = true;
        }
        if(isPointInPolygonsAI(collidables, new Vector(x+World.SCALE,y+World.SCALE))) {
            blocked = true;
        }
        return blocked;
    }

    public boolean blocked(float targetX, float targetY) {
        boolean collided = false;
        System.out.println("Blocked location: "+targetX + " "+targetY);

        if (isPointInPolygonsAI(collidables, new Vector(targetX, targetY))) {
            //System.out.println("Bottom left"+targetX + " " + targetY);
            collided = true;
        }
        if (isPointInPolygonsAI(collidables, new Vector(targetX + 50, targetY))) {
            //System.out.println("Bottom right"+targetX + " " + targetY);

            collided = true;
        }
        if (isPointInPolygonsAI(collidables, new Vector(targetX, targetY + 50))) {
            //System.out.println("Top left"+targetX + " " + targetY);

            collided = true;
        }
        if (isPointInPolygonsAI(collidables, new Vector(targetX + 50, targetY + 50))) {
            //System.out.println("Top right"+targetX + " " + targetY);

            collided = true;
        }
        if (isPointInPolygonsAI(collidables, new Vector(targetX + 25, targetY + 50))) {
            //System.out.println("Top middle"+targetX + " " + targetY);

            collided = true;
        }
        if (isPointInPolygonsAI(collidables, new Vector(targetX, targetY+25))) {
            //System.out.println("left middle"+targetX + " " + targetY);

            collided = true;
        }
        if (isPointInPolygonsAI(collidables, new Vector(targetX+50, targetY+25))) {
            //System.out.println("right middle"+targetX + " " + targetY);

            collided = true;
        }
        if (isPointInPolygonsAI(collidables, new Vector(targetX+25, targetY))) {
            //System.out.println("bottom middle"+targetX + " " + targetY);

            collided = true;
        }

        return collided;
    }

    private boolean isPointInPolygonsAI(Stack<Polygon> polygons, Vector point) {
        if (polygons != null) {
            for (Polygon polygon : polygons) {

                if (isPointInPolygonAI(polygon, point.getY(), point.getX())) {
                    return true;
                }/*            
                if (isPointInPolygonAI(polygon, point.getY(), point.getX()+50)) {
                    return true;
                }
                if (isPointInPolygonAI(polygon, point.getY()+50, point.getX())) {
                    return true;
                }
                if (isPointInPolygonAI(polygon, point.getY()+50, point.getX()+50)) {
                    return true;
                }
                if (isPointInPolygonAI(polygon, point.getY()-30, point.getX())) {
                    return true;
                }
                if (isPointInPolygonAI(polygon, point.getY()+30, point.getX()+30)) {
                    return true;
                }
                if (isPointInPolygonAI(polygon, point.getY()-30, point.getX()+30)) {
                    return true;
                }
                if (isPointInPolygonAI(polygon, point.getY()+30, point.getX()-30)) {
                    return true;
                }*/

            }
        }
        return false;
    }

    private boolean isPointInPolygonAI(Polygon polygon, float y, float x) {
        Vector lastVertice = polygon.getLast();
        boolean oddNodes = false;
        for (int i = 0; i < polygon.size(); i++) {
            Vector vertice = polygon.get(i);
            if ((vertice.getY() < y && lastVertice.getY() >= y) || (lastVertice.getY() < y && vertice.getY() >= y)) {
                if (vertice.getX() + (y - vertice.getY()) / (lastVertice.getY() - vertice.getY()) * (lastVertice.getX() - vertice.getX()) < x) {
                    oddNodes = !oddNodes;
                }
            }

            lastVertice = vertice;
        }
        return oddNodes;
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
