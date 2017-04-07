package rpg.commonplayer;

import rpg.common.entities.Entity;

public class Player extends Entity {
    
    private int level;
    private int experiencePoints;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }
}
