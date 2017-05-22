package rpg.commonenemy;

import rpg.common.entities.Entity;
import rpg.common.util.Vector;

public class Enemy extends Entity {
    
    private Vector nextStep;

    public void setNextStep(Vector nextStep) {
        this.nextStep = nextStep;
    }

    public Vector getNextStep() {
        return nextStep;
    }
    
    
}
