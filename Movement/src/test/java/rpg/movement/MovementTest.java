package rpg.movement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;
import rpg.common.util.Vector;
import rpg.common.world.Room;
import rpg.common.world.World;

/**
 *
 * @author Niels
 */
public class MovementTest {

    private MovementControlSystem movementControlSystem;
    private Entity entity;
    private GameData gameData;
    private World world;

    @Before
    public void setUp() {
        movementControlSystem = new MovementControlSystem();

        Room room = new Room();
        room.setWidth(1280);
        room.setHeight(720);
        room.setX(0);
        room.setY(1);
        world = mock(World.class);
        when(world.getCurrentRoom()).thenReturn(room);

        gameData = mock(GameData.class);
        when(gameData.getDeltaTime()).thenReturn(60f / 3600f);

        entity = new Entity();
        entity.getRoomPosition().set(0, 0);
        entity.getWorldPosition().set(world.getCurrentRoom().getX(), 
                world.getCurrentRoom().getY());
        entity.setDefaultMovementSpeed(200);
        entity.setMovementSpeedModifier(1);
        entity.setSize(50, 50);
        entity.setCurrentMovementSpeed(entity.getDefaultMovementSpeed() * 
                entity.getMovementSpeedModifier() * gameData.getDeltaTime());
        room.addEntity(entity);
    }

    @After
    public void tearDown() {
        movementControlSystem = null;
        entity = null;
        gameData = null;
        world = null;
    }

    @Test
    public void testEntityMovement() {
        assertTrue(entity.getRoomPosition().getX() == 0);
        assertTrue(entity.getRoomPosition().getY() == 0);

        entity.getVelocity().set(new Vector(10, 10));
        movementControlSystem.postProcess(gameData, world);

        assertFalse(entity.getRoomPosition().getX() == 0);
        assertFalse(entity.getRoomPosition().getY() == 0);
    }

    @Test
    public void testEntityDirection() {
        entity.getVelocity().set(new Vector(0, 10));
        movementControlSystem.process(gameData, world);
        assertTrue(entity.getDirection() == 90);

        entity.getVelocity().set(new Vector(0, -10));
        movementControlSystem.process(gameData, world);
        assertTrue(entity.getDirection() == -90);
    }

}
