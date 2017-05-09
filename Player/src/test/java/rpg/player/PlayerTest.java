package rpg.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.entities.Entity;
import rpg.common.util.Vector;
import rpg.common.world.Room;
import rpg.common.world.World;

/**
 *
 * @author Niels
 */
public class PlayerTest {

    private PlayerControlSystem playerControlSystem;
    private Entity player;
    private GameData gameData;
    private World world;

    @Before
    public void setUp() {
        playerControlSystem = new PlayerControlSystem();

        Room room = new Room();
        room.setWidth(1280);
        room.setHeight(720);
        room.setX(0);
        room.setY(1);
        world = mock(World.class);
        when(world.getCurrentRoom()).thenReturn(room);

        gameData = mock(GameData.class);
        GameKeys gameKeys = new GameKeys();
        when(gameData.getDeltaTime()).thenReturn(60f / 3600f);
        when(gameData.getKeys()).thenReturn(gameKeys);
        when(gameData.isChangingRoom()).thenReturn(false);
        
        player = playerControlSystem.createPlayer(world);
        player.setCurrentMovementSpeed(player.getDefaultMovementSpeed() * player.getMovementSpeedModifier() * gameData.getDeltaTime());
        room.addEntity(player);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testPlayerVelocity() {
        assertTrue(player.getVelocity().getX() == 0);
        assertTrue(player.getVelocity().getY() == 0);
        
        gameData.getKeys().setKey(GameKeys.W, true);
        gameData.getKeys().setKey(GameKeys.D, true);
        playerControlSystem.process(gameData, world);
        gameData.getKeys().setKey(GameKeys.W, false);
        gameData.getKeys().setKey(GameKeys.D, false);
        
        assertFalse(player.getVelocity().getX() == 0);
        assertFalse(player.getVelocity().getY() == 0);
    }

}
