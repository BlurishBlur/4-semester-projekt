package rpg.astarcnc;

import java.util.ArrayList;
import java.util.Collections;
import rpg.common.data.GameData;
import rpg.common.world.Room;
import rpg.common.entities.Entity;
import rpg.common.services.IEntityProcessingService;
import rpg.common.world.World;

public class AStarPathFinder implements PathFinder, IEntityProcessingService {

    private ArrayList closed = new ArrayList();
    private SortedList open = new SortedList();
    private Room room;
    private int maxSearchDistance;
    private Node[][] nodes;
    private boolean allowDiagMovement;

    public AStarPathFinder(Room room, Node[][] nodes, int maxSearchDistance, boolean allowDiagMovement) {
        this.room = room;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;
        this.nodes = nodes;
    }

    public AStarPathFinder(Room room, int maxSearchDistance, boolean allowDiagMovement) {
        this.room = room;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;

        nodes = new Node[room.getWidth()][room.getHeight()];
        for (int x = 0; x < room.getWidth(); x++) {
            for (int y = 0; y < room.getHeight(); y++) {
                nodes[x][y] = new Node(x, y);
            }
        }
    }

    public Path findPath(Entity entity, int sx, int sy, int tx, int ty) {
        // easy first check, if the destination is blocked, we can't get there

        /*if (room.blocked(entity, tx, ty)) {
            return null;
        }*/
        // initial state for A*. The closed group is empty. Only the starting
        // tile is in the open list and it'e're already there
        nodes[sx][sy].setCost(0);
        nodes[sx][sy].setDepth(0);
        closed.clear();
        open.clear();
        open.add(nodes[sx][sy]);

        nodes[tx][ty].setParent(null);

        // while we haven'n't exceeded our max search depth
        int maxDepth = 0;
        while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
            // pull out the first node in our open list, this is determined to 

            // be the most likely to be the next step based on our heuristic
            Node current = getFirstInOpen();
            if (current == nodes[tx][ty]) {
                break;
            }

            removeFromOpen(current);
            addToClosed(current);

            // search through all the neighbours of the current node evaluating
            // them as next steps
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    // not a neighbour, its the current tile

                    if ((x == 0) && (y == 0)) {
                        continue;
                    }

                    // if we're not allowing diaganol movement then only 
                    // one of x or y can be set
                    if (!allowDiagMovement) {
                        if ((x != 0) && (y != 0)) {
                            continue;
                        }
                    }

                    // determine the location of the neighbour and evaluate it
                    int xp = x + current.getX();
                    int yp = y + current.getY();

                    if (isValidLocation(entity, sx, sy, xp, yp)) {
                        // the cost to get to this node is cost the current plus the movement

                        // cost to reach this node. Note that the heursitic value is only used
                        // in the sorted open list
                        float nextStepCost = current.getCost() + getMovementCost(entity, current.getX(), current.getY(), xp, yp);
                        Node neighbour = nodes[xp][yp];

                        // if the new cost we've determined for this node is lower than 
                        // it has been previously makes sure the node hasn'e've
                        // determined that there might have been a better path to get to
                        // this node so it needs to be re-evaluated
                        System.out.println(xp + ", " + yp);
                        if (nextStepCost < neighbour.getCost()) {
                            if (inOpenList(neighbour)) {
                                removeFromOpen(neighbour);
                            }
                            if (inClosedList(neighbour)) {
                                removeFromClosed(neighbour);
                            }
                        }

                        // if the node hasn't already been processed and discarded then
                        // reset it's cost to our current cost and add it as a next possible
                        // step (i.e. to the open list)
                        if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                            neighbour.setCost(nextStepCost);
                            neighbour.setHeuristic(getHeuristicCost(entity, xp, yp, tx, ty));
                            maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                            addToOpen(neighbour);
                        }
                    }
                }
            }
        }

        // since we'e've run out of search 
        // there was no path. Just return null
        if (nodes[tx][ty].getParent() == null) {
            return null;
        }

        // At this point we've definitely found a path so we can uses the parent
        // references of the nodes to find out way from the target location back
        // to the start recording the nodes on the way.
        Path path = new Path();
        Node target = nodes[tx][ty];
        while (target != nodes[sx][sy]) {
            path.prependStep(target.getX(), target.getY());
            target = target.getParent();
        }
        path.prependStep(sx, sy);

        // thats it, we have our path 
        return path;
    }

    /**
     * Get the first element from the open list. This is the next one to be
     * searched.
     *
     * @return The first element in the open list
     */
    protected Node getFirstInOpen() {
        return (Node) open.first();
    }

    /**
     * Add a node to the open list
     *
     * @param node The node to be added to the open list
     */
    protected void addToOpen(Node node) {
        open.add(node);
    }

    /**
     * Check if a node is in the open list
     *
     * @param node The node to check for
     * @return True if the node given is in the open list
     */
    protected boolean inOpenList(Node node) {
        return open.contains(node);
    }

    /**
     * Remove a node from the open list
     *
     * @param node The node to remove from the open list
     */
    protected void removeFromOpen(Node node) {
        open.remove(node);
    }

    /**
     * Add a node to the closed list
     *
     * @param node The node to add to the closed list
     */
    protected void addToClosed(Node node) {
        closed.add(node);
    }

    /**
     * Check if the node supplied is in the closed list
     *
     * @param node The node to search for
     * @return True if the node specified is in the closed list
     */
    protected boolean inClosedList(Node node) {
        return closed.contains(node);
    }

    /**
     * Remove a node from the closed list
     *
     * @param node The node to remove from the closed list
     */
    protected void removeFromClosed(Node node) {
        closed.remove(node);
    }

    /**
     * Check if a given location is valid for the supplied mover
     *
     * @param mover The mover that would hold a given location
     * @param sx The starting x coordinate
     * @param sy The starting y coordinate
     * @param x The x coordinate of the location to check
     * @param y The y coordinate of the location to check
     * @return True if the location is valid for the given mover
     */
    protected boolean isValidLocation(Entity entity, int sx, int sy, int x, int y) {
        boolean invalid = (x < 0) || (y < 0) || (x >= room.getWidth()) || (y >= room.getHeight());

        if ((!invalid) && ((sx != x) || (sy != y))) {
            invalid = room.blocked(entity, x, y);
        }

        return !invalid;
    }

    /**
     * Get the cost to move through a given location
     *
     * @param mover The entity that is being moved
     * @param sx The x coordinate of the tile whose cost is being determined
     * @param sy The y coordiante of the tile whose cost is being determined
     * @param tx The x coordinate of the target location
     * @param ty The y coordinate of the target location
     * @return The cost of movement through the given tile
     */
    public float getMovementCost(Entity entity, int sx, int sy, int tx, int ty) {
        return room.getCost(entity, sx, sy, tx, ty);
    }

    /**
     * Get the heuristic cost for the given location. This determines in which
     * order the locations are processed.
     *
     * @param mover The entity that is being moved
     * @param startX The x coordinate of the tile whose cost is being determined
     * @param startY The y coordiante of the tile whose cost is being determined
     * @param tx The x coordinate of the target location
     * @param ty The y coordinate of the target location
     * @return The heuristic cost assigned to the tile
     */
    public float getHeuristicCost(Entity entity, int startX, int startY, int targetX, int targetY) {
        float dx = targetX - startX;
        float dy = targetY - startY;

        float result = (float) (Math.sqrt((dx * dx) + (dy * dy)));
        return result;
    }

    @Override
    public void process(GameData gameData, World world) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * A simple sorted list
     *
     * @author kevin
     */
    private class SortedList {

        /**
         * The list of elements
         */
        private ArrayList list = new ArrayList();

        /**
         * Retrieve the first element from the list
         *
         * @return The first element from the list
         */
        public Object first() {
            return list.get(0);
        }

        /**
         * Empty the list
         */
        public void clear() {
            list.clear();
        }

        /**
         * Add an element to the list - causes sorting
         *
         * @param o The element to add
         */
        public void add(Object o) {
            list.add(o);
            Collections.sort(list);
        }

        /**
         * Remove an element from the list
         *
         * @param o The element to remove
         */
        public void remove(Object o) {
            list.remove(o);
        }

        /**
         * Get the number of elements in the list
         *
         * @return The number of element in the list
         */
        public int size() {
            return list.size();
        }

        /**
         * Check if an element is in the list
         *
         * @param o The element to search for
         * @return True if the element is in the list
         */
        public boolean contains(Object o) {
            return list.contains(o);
        }
    }
}
