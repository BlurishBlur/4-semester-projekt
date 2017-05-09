package rpg.combat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.entities.Entity;
import rpg.common.world.Room;
import rpg.common.world.World;

/**
 *
 * @author Niels
 */
public class CombatTest {

    private CombatSystem combatSystem;
    private Entity player;
    private GameData gameData;
    private World world;

    @Before
    public void setUp() {
        combatSystem = new CombatSystem();

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
        player.setSize(500, 500);
        player.setCurrentMovementSpeed(player.getDefaultMovementSpeed() * player.getMovementSpeedModifier() * gameData.getDeltaTime());
        room.addEntity(player);
        when(world.getPlayer()).thenReturn(player);
    }

    @After
    public void tearDown() {
        combatSystem = null;
        player = null;
        gameData = null;
        world = null;
    }

    @Test
    public void test() {
    }

}
