package rpg.gameengine.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rpg.common.data.GameData;
import rpg.common.entities.Entity;

public class HpBar {
    
    private final Sprite hpBarEmpty;
    private Sprite hpBarFull;
    private final int hpBarWidth;
    private final int hpBarHeight;
    private final Entity owner;
    private final int maxValue;
    private final float scaleFactor;
    
    public HpBar(Entity owner) {
        this.owner = owner;
        hpBarWidth = (int) (owner.getWidth() + owner.getWidth() / 3);
        hpBarHeight = (int) (owner.getHeight() / 5);
        hpBarEmpty = new Sprite(new Texture("rpg/gameengine/hpbarempty.png"));
        hpBarEmpty.setSize(hpBarWidth, hpBarHeight);
        hpBarFull = new Sprite(new Texture("rpg/gameengine/hpbarfull.png"));
        maxValue = owner.getMaxHealth();
        scaleFactor = maxValue / hpBarFull.getWidth();
    }
    
    public void draw(SpriteBatch batch, GameData gameData) {
        update(gameData);
        hpBarEmpty.draw(batch);
        hpBarFull.draw(batch);
    }
    
    private void update(GameData gameData) {
        hpBarEmpty.setPosition(owner.getRoomPosition().getX() - hpBarWidth / 2, owner.getRoomPosition().getY() + owner.getHeight() / 2);
        if(hpBarEmpty.getY() + hpBarHeight > gameData.getDisplayHeight()) {
            hpBarEmpty.setY(hpBarEmpty.getY() - owner.getHeight());
        }
        hpBarFull.setPosition(hpBarEmpty.getX(), hpBarEmpty.getY());
        hpBarFull.setSize(owner.getCurrentHealth() / scaleFactor, hpBarHeight);
    }
}
