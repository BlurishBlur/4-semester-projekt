package rpg.common.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import rpg.common.entities.Entity;
import rpg.common.exceptions.FileFormatException;
import rpg.common.util.Logger;
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
        File[] listOfFiles = new File("../../../Common/src/main/resources/rpg/common/rooms/").listFiles();
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
                spot = line.indexOf("=");
                if (spot > -1) {
                    identifier = line.substring(0, spot).trim();
                    value = line.substring(spot + 1).trim();
                    loadRoomData(room, identifier, value);
                }
                else {
                    loadCollidables(room, line.trim());
                }
            }
            world[room.getX()][room.getY()] = room;
        }
        catch (FileNotFoundException e) {
            Logger.log("File at path: " + file.getPath() + " not found.", e);
        }
        catch (FileFormatException e) {
            Logger.log("File at path: " + file.getPath() + " not formatted correctly.", e);
        }
    }

    private void loadRoomData(Room room, String identifier, String value) {
        switch (identifier) {
            case "spritePath":
                room.setSpritePath(value);
                break;
            case "width":
                room.setWidth(Integer.parseInt(value));
                break;
            case "height":
                room.setHeight(Integer.parseInt(value));
                break;
            case "worldPositionX":
                room.setX(Integer.parseInt(value));
                break;
            case "worldPositionY":
                room.setY(Integer.parseInt(value));
                break;
            case "canExitUp":
                room.canExitUp(Boolean.parseBoolean(value));
                break;
            case "canExitDown":
                room.canExitDown(Boolean.parseBoolean(value));
                break;
            case "canExitLeft":
                room.canExitLeft(Boolean.parseBoolean(value));
                break;
            case "canExitRight":
                room.canExitRight(Boolean.parseBoolean(value));
                break;
            default:
                break;
        }
    }

    private void loadCollidables(Room room, String line) throws FileFormatException {
        if (line.startsWith("{")) {
            room.getCollidables().push(new ArrayList<>());
        }
        else if (line.startsWith("(")) {
            String[] point = line.replace("(", "").replace(")", "").split(",");
            room.getCollidables().peek().add(new Vector(Integer.parseInt(point[0].trim()), Integer.parseInt(point[1].trim())));
        }
        else if(!line.startsWith("}") && !line.startsWith("#")) {
            throw new FileFormatException();
        }
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
