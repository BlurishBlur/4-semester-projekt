package rpg.commonweapon;

import rpg.common.data.GameData;
import rpg.common.entities.Entity;

public interface WeaponSPI {
    
    Entity createWeapon(Entity owner, GameData gameData);
    
}
