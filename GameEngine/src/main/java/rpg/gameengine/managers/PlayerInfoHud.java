package rpg.gameengine.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rpg.common.world.World;
import rpg.commonplayer.Player;

public class PlayerInfoHud {

    private Camera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    public PlayerInfoHud(Camera camera) {
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
    }

    public void draw(World world) {
        batch.setProjectionMatrix(camera.getProjection());
        batch.begin();
        font.draw(batch, "Health: " + world.getPlayer().getCurrentHealth(), 50, world.getCurrentRoom().getHeight() - 40);
        font.draw(batch, "Currency: " + world.getPlayer().getCurrency(), 600, world.getCurrentRoom().getHeight() - 40);
        font.draw(batch, "Level: " + ((Player) world.getPlayer()).getLevel(), 1150, world.getCurrentRoom().getHeight() - 25);
        font.draw(batch, ((Player) world.getPlayer()).getExperience() + "/100", 1155, world.getCurrentRoom().getHeight() - 65);
        batch.end();
    }

}
