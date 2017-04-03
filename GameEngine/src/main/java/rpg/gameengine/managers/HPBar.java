/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.gameengine.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;

/**
 *
 * @author niclasmolby
 */
public class HPBar {
    ProgressBar progressBar;
    ProgressBarStyle progressBarStyle;
    
    public HPBar() {
        progressBarStyle = new ProgressBarStyle();
        progressBar = new ProgressBar(0, 100, 0.1f, false, progressBarStyle);
        progressBar.setValue(100);
        progressBar.setPosition(100, 100);
    }
    
    public void draw(SpriteBatch batch) {
        progressBar.draw(batch, 0);
    }
}
