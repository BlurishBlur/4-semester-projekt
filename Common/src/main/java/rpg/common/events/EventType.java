package rpg.common.events;

import java.io.Serializable;

public enum EventType implements Serializable {
    
    ATTACK, ENEMY_DIED, COIN_PICKUP, XP_PICKUP, PUNCH_NO_HIT, WEAPON_USE, WEAPON_HIT, LEVEL_UP;
}
