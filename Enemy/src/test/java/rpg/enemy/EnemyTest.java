package rpg.enemy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class EnemyTest {

    private EnemyControlSystem enemyControlSystem;
    private Entity enemy;
    private GameData gameData;
    private World world;

    @Before
    public void setUp() {
        enemyControlSystem = new EnemyControlSystem();

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
        
        enemy = enemyControlSystem.createEnemy(gameData);
        enemy.setCurrentMovementSpeed(enemy.getDefaultMovementSpeed() * enemy.getMovementSpeedModifier() * gameData.getDeltaTime());
        room.addEntity(enemy);
    }

    @After
    public void tearDown() {
        enemyControlSystem = null;
        enemy = null;
        gameData = null;
        world = null;
    }
    
    @Test
    public void testEnemyEdgeCollision() {
        enemy.getRoomPosition().set(-50, -50);
        enemyControlSystem.process(gameData, world);
        assertTrue(enemy.getRoomPosition().getX() == enemy.getWidth() / 2);
        assertTrue(enemy.getRoomPosition().getY() == enemy.getHeight() / 2);
    }

}
