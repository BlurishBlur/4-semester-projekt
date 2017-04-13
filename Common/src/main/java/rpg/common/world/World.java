package rpg.common.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpg.common.entities.Entity;
import rpg.common.util.Vector;

public class World {

    private final int WORLD_WIDTH = 2;
    private final int WORLD_HEIGHT = 3;
    private Room[][] world;
    private Room currentRoom;
    private Entity player;

    public World() {
        world = new Room[WORLD_WIDTH][WORLD_HEIGHT];
        loadRooms();
        currentRoom = world[0][1];
    }

    public void loadRooms() {
        File folder = new File("../../../Common/src/main/resources/rpg/common/rooms/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.getName().endsWith(".room")) {
                loadRoom(file);
            }
        }
    }

    private void loadRoom(File file) {
        try (Scanner in = new Scanner(file)) {
            Room room = new Room();
            String line;
            int spot;
            String identifier;
            String value;
            while (in.hasNextLine()) {
                line = in.nextLine();
                System.out.println(line);
                spot = line.indexOf("=");
                if (spot > -1) {
                    identifier = line.substring(0, spot).trim();
                    value = line.substring(spot + 1).trim();
                    if (identifier.equals("spritePath")) {
                        room.setSpritePath(value);
                    }
                    else if (identifier.equals("width")) {
                        room.setWidth(Integer.parseInt(value));
                    }
                    else if (identifier.equals("height")) {
                        room.setHeight(Integer.parseInt(value));
                    }
                    else if (identifier.equals("worldPositionX")) {
                        room.setX(Integer.parseInt(value));
                    }
                    else if (identifier.equals("worldPositionY")) {
                        room.setY(Integer.parseInt(value));
                    }
                    else if (identifier.equals("canExitUp")) {
                        room.canExitUp(Boolean.parseBoolean(value));
                    }
                    else if (identifier.equals("canExitDown")) {
                        room.canExitDown(Boolean.parseBoolean(value));
                    }
                    else if (identifier.equals("canExitLeft")) {
                        room.canExitLeft(Boolean.parseBoolean(value));
                    }
                    else if (identifier.equals("canExitRight")) {
                        room.canExitRight(Boolean.parseBoolean(value));
                    }
                }
                else {
                    loadCollidables(room, line.trim());
                }
            }
            world[room.getX()][room.getY()] = room;
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadCollidables(Room room, String line) {
        System.out.println("load methods");
        if (line.startsWith("{")) {
            room.getCollidables().push(new ArrayList<>());
            System.out.println("nyt polygon");
        }
        else if (line.startsWith("(")) {
            String[] point = line.replace("(", "").replace(")", "").split(",");
            room.getCollidables().peek().add(new Vector(Integer.parseInt(point[0].trim()), Integer.parseInt(point[1].trim())));
            System.out.println("new point: " +point[0] + point[1]);
        }
        System.out.println("ingen ifs");
    }

    public void setCurrentRoom(Vector worldPosition) {
        currentRoom = getRoom(worldPosition);
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public Room getRoom(int x, int y) {
        return world[x][y];
    }

    public Room getRoom(Vector worldPosition) {
        return world[(int) worldPosition.getX()][(int) worldPosition.getY()];
    }

    public Entity getPlayer() {
        return player;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

}
