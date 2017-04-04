package rpg.currency;

import rpg.common.entities.Entity;

public class Currency extends Entity {
    
    private int value;
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
}
