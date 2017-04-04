package rpg.commonplayer;

import rpg.common.entities.Entity;

public class Player extends Entity {
    private int playerLevel;
    private int playerExperiancePoints;

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int getPlayerExperiancePoints() {
        return playerExperiancePoints;
    }

    public void setPlayerExperiancePoints(int playerExperiancePoints) {
        this.playerExperiancePoints = playerExperiancePoints;
    }
}
