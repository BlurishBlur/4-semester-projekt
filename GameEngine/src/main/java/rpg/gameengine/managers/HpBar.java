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
    private float visibleValue;
    private final int fadeTime;
    private float fadeTimeCounter;

    public HpBar(Entity owner) {
        this.owner = owner;
        hpBarWidth = (int) (owner.getWidth() + owner.getWidth() / 3);
        hpBarHeight = (int) (owner.getHeight() / 6);
        hpBarEmpty = new Sprite(new Texture("rpg/gameengine/hpbarempty.png"));
        hpBarEmpty.setSize(hpBarWidth, hpBarHeight);
        hpBarFull = new Sprite(new Texture("rpg/gameengine/hpbarfull.png"));
        hpBarFull.setSize(hpBarWidth, hpBarHeight);
        maxValue = owner.getMaxHealth();
        scaleFactor = maxValue / hpBarFull.getWidth();
        visibleValue = hpBarWidth;
        fadeTime = 5;
        fadeTimeCounter = fadeTime;
    }

    public void draw(SpriteBatch batch, GameData gameData) {
        update(gameData);
        hpBarEmpty.draw(batch);
        hpBarFull.draw(batch);
    }

    private void update(GameData gameData) {
        updatePosition(gameData);
        updateValue(gameData);
        hpBarFull.setSize(visibleValue, hpBarHeight);
    }

    private void updatePosition(GameData gameData) {
        hpBarEmpty.setPosition(owner.getRoomPosition().getX() - hpBarWidth / 2, owner.getRoomPosition().getY() + owner.getHeight() / 2);
        if (hpBarEmpty.getY() + hpBarHeight > gameData.getDisplayHeight()) {
            hpBarEmpty.setY(hpBarEmpty.getY() - owner.getHeight());
        }
        hpBarFull.setPosition(hpBarEmpty.getX(), hpBarEmpty.getY());
    }

    private void updateValue(GameData gameData) {
        float currentValue = owner.getCurrentHealth() / scaleFactor;
        if (currentValue < visibleValue) {
            visibleValue -= (hpBarWidth * 2 / scaleFactor) * gameData.getDeltaTime();
            hpBarEmpty.setAlpha(1);
            hpBarFull.setAlpha(hpBarEmpty.getColor().a);
            fadeTimeCounter = fadeTime;
        }
        else if (fadeTimeCounter < 0) {
            if (hpBarEmpty.getColor().a > 0.70f) {
                hpBarEmpty.setAlpha(hpBarEmpty.getColor().a - 0.01f * gameData.getDeltaTime());
                hpBarFull.setAlpha(hpBarEmpty.getColor().a);
            }
        }
        else {
            fadeTimeCounter -= gameData.getDeltaTime();
        }
    }
    
}
