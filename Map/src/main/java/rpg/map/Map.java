/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.map;

import java.io.InputStream;
import java.util.Scanner;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.exceptions.FileFormatException;
import rpg.common.services.IGameInitializationService;
import rpg.common.services.IGamePluginService;
import rpg.common.util.Logger;
import rpg.common.util.Polygon;
import rpg.common.util.Vector;
import rpg.common.world.Room;
import rpg.common.world.World;

@ServiceProviders(value = {
    @ServiceProvider(service = IGameInitializationService.class)
})
public class Map implements IGameInitializationService {
    
    GameData gameData;
    World world;

    @Override
    public void initialize(World world, GameData gameData) {
        this.gameData = gameData;
        this.world = world;
        loadRooms();
    }
    
    private void loadRooms() {
        //File[] listOfFiles = new File(getClass().getClassLoader().getResource("rpg/common/rooms/").toURI()).listFiles();
        for (int i = 0; i < World.WORLD_SIZE; i++) {
            loadRoom(getClass().getClassLoader().getResourceAsStream("rpg/map/rooms/room" + (i + 1) + ".room"));
        }
        world.setCurrentRoom(new Vector(0,1));
    }
    
    private void loadRoom(InputStream file) {
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
            world.setRoom(new Vector(room.getX(), room.getY()), room);
        }
        catch (FileFormatException e) {
            Logger.log("File at path: " + file.toString() + " not formatted correctly.", e);
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
            room.getCollidables().push(new Polygon());
        }
        else if (line.startsWith("(")) {
            String[] point = line.replace("(", "").replace(")", "").split(",");
            room.getCollidables().peek().add(new Vector(Integer.parseInt(point[0].trim()), Integer.parseInt(point[1].trim())));
        }
        else if (!line.startsWith("}") && !line.startsWith("#")) {
            throw new FileFormatException();
        }
    }

}
