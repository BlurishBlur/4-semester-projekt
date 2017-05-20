package rpg.currency;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.entities.Entity;
import rpg.common.events.Event;
import rpg.common.events.EventType;
import rpg.common.world.Room;
import rpg.common.world.World;

/**
 *
 * @author Niels
 */
public class CurrencyTest {

    private CurrencySystem currencySystem;
    private Entity player;
    private GameData gameData;
    private World world;

    @Before
    public void setUp() {
        currencySystem = new CurrencySystem();

        Room room = new Room();
        room.setWidth(1280);
        room.setHeight(720);
        room.setX(0);
        room.setY(1);
        world = mock(World.class);
        when(world.getCurrentRoom()).thenReturn(room);

        gameData = mock(GameData.class);
        GameKeys gameKeys = new GameKeys();
        when(gameData.getKeys()).thenReturn(gameKeys);
        when(gameData.getDeltaTime()).thenReturn(60f / 3600f);

        player = new Entity();
        player.getRoomPosition().set(1000, 1000);
        player.getWorldPosition().set(world.getCurrentRoom().getX(), world.getCurrentRoom().getY());
        player.setDefaultMovementSpeed(200);
        player.setMovementSpeedModifier(1);
        player.setSize(5, 5);
        player.setCurrentMovementSpeed(player.getDefaultMovementSpeed() * player.getMovementSpeedModifier() * gameData.getDeltaTime());
        room.addEntity(player);
        when(world.getPlayer()).thenReturn(player);
    }

    @After
    public void tearDown() {
        currencySystem = null;
        player = null;
        gameData = null;
        world = null;
    }

    @Test
    public void testCurrencyCreationAndPickup() {
        assertTrue(world.getCurrentRoom().getEntities(Currency.class).isEmpty());
        Entity enemy = new Entity();
        enemy.getWorldPosition().set(50, 50);
        List<Event> events = new ArrayList<>();
        events.add(new Event(EventType.ENEMY_DIED, enemy));
        when(gameData.getEvents(EventType.ENEMY_DIED)).thenReturn(events);
        currencySystem.process(gameData, world);
        waitForThread();
        events.clear();
        assertFalse(world.getCurrentRoom().getEntities(Currency.class).isEmpty());
        
        player.getRoomPosition().set(50, 50);
        player.setSize(500, 500);
        currencySystem.process(gameData, world);
        assertTrue(world.getCurrentRoom().getEntities(Currency.class).isEmpty());
    }
    
    private void waitForThread() {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            System.err.println("Thread interrupted");
        }
    }

}
