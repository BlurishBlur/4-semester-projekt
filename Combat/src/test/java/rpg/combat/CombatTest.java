package rpg.combat;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.entities.Entity;
import rpg.common.events.Event;
import rpg.common.world.Room;
import rpg.common.world.World;
import rpg.commonweapon.Bullet;

/**
 *
 * @author Niels
 */
public class CombatTest {

    private CombatSystem combatSystem;
    private Entity player;
    private Entity enemy;
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
        when(gameData.getDeltaTime()).thenReturn(2f);
        List<Event> events = new ArrayList<>();
        when(gameData.getEvents()).thenReturn(events);

        player = new Entity();
        player.getRoomPosition().set(0, 0);
        player.getWorldPosition().set(world.getCurrentRoom().getX(), world.getCurrentRoom().getY());
        player.setSize(50, 50);
        room.addEntity(player);
        when(world.getPlayer()).thenReturn(player);
        
        enemy = new Entity();
        enemy.getRoomPosition().set(0, 35);
        enemy.getWorldPosition().set(world.getCurrentRoom().getX(), world.getCurrentRoom().getY());
        enemy.setSize(50, 50);
        enemy.setMaxHealth(100);
        enemy.setCurrentHealth(enemy.getMaxHealth());
        room.addEntity(enemy);
    }

    @After
    public void tearDown() {
        combatSystem = null;
        player = null;
        enemy = null;
        gameData = null;
        world = null;
    }
    
    @Test
    public void testBulletVelocity() {
        Bullet bullet = new Bullet();
        bullet.getDefaultVelocity().set(10, 10);
        world.getCurrentRoom().addEntity(bullet);
        
        assertTrue(bullet.getVelocity().getX() == 0);
        assertTrue(bullet.getVelocity().getY() == 0);
        combatSystem.process(gameData, world);
        assertFalse(bullet.getVelocity().getX() == 0);
        assertFalse(bullet.getVelocity().getY() == 0);
    }
    
    @Test
    public void testBulletExpiration() {
        Bullet bullet = new Bullet();
        bullet.setCurrentDuration(1);
        world.getCurrentRoom().addEntity(bullet);
        assertFalse(world.getCurrentRoom().getEntities(Bullet.class).isEmpty());
        combatSystem.process(gameData, world);
        assertTrue(world.getCurrentRoom().getEntities(Bullet.class).isEmpty());
    }
    
    @Test
    public void testWeaponHit() {
        assertTrue(enemy.getCurrentHealth() == 100);
        gameData.getKeys().setKey(GameKeys.UP, true);
        combatSystem.process(gameData, world);
        gameData.getKeys().setKey(GameKeys.UP, false);
        assertFalse(enemy.getCurrentHealth() == 100);
    }

}
