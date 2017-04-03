package rpg.gameengine.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rpg.common.entities.Entity;

public class HPBar {
    
    private Sprite hpBarFull;
    private final Sprite hpBarEmpty;
    private final Entity owner;
    private final int maxValue;
    private final float factor;
    
    public HPBar(Entity owner) {
        this.owner = owner;
        hpBarEmpty = new Sprite(new Texture("rpg/gameengine/hpbarempty.png"));
        hpBarEmpty.setSize(40, 7);
        hpBarFull = new Sprite(new Texture("rpg/gameengine/hpbarfull.png"));
        hpBarFull.setSize(40, 7);
        maxValue = owner.getMaxHealth();
        factor = maxValue / hpBarFull.getWidth();
    }
    
    public void draw(SpriteBatch batch) {
        update();
        hpBarEmpty.draw(batch);
        hpBarFull.draw(batch);
    }
    
    private void update() {
        hpBarEmpty.setPosition(owner.getRoomPosition().getX() - hpBarEmpty.getWidth() / 2, owner.getRoomPosition().getY() + owner.getHeight() / 2);
        hpBarFull.setPosition(owner.getRoomPosition().getX() - hpBarEmpty.getWidth() / 2, owner.getRoomPosition().getY() + owner.getHeight() / 2);
        
        float currentValue = owner.getCurrentHealth() / factor;
        hpBarFull.setSize(currentValue, hpBarFull.getHeight());
    }
}
