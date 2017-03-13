package rpg.acquireweapon;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import rpg.common.data.GameData;
import rpg.common.data.GameKeys;
import rpg.common.data.World;
import rpg.common.entities.Entity;
import rpg.common.entities.EntityType;
import rpg.common.services.IEntityProcessingService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class WeaponAcquiringSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        if(gameData.getKeys().isPressed(GameKeys.F1)) {
            Entity player = world.getEntity(EntityType.PLAYER);
            Entity weapon = new Entity();
            weapon.setType(EntityType.MELEE);
            weapon.setMovementSpeed(0);
            weapon.setWidth(player.getWidth());
            weapon.setHeight(player.getHeight());
            weapon.getPosition().set(player.getPosition());
            weapon.setSpritePath("rpg/gameengine/sword.png");
            gameData.addWeapon(player, weapon);
            world.addEntity(weapon);
            System.out.println("Added weapon to player: " + gameData.getWeapon(player).getType().toString());
        }
    }
    
}
