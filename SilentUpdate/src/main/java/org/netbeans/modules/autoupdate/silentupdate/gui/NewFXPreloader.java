package org.netbeans.modules.autoupdate.silentupdate.gui;

import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author Niels
 */
public class NewFXPreloader extends Preloader {
    
    private ProgressBar bar;
    private Stage stage;
    
    private Scene createPreloaderScene() {
        bar = new ProgressBar();
        AnchorPane pane = new AnchorPane();
        Label label = new Label("Loading modules...");
        
        HBox hbox = new HBox();
        hbox.setPrefWidth(300);
        hbox.setPrefHeight(150);
        hbox.setAlignment(Pos.CENTER);
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(bar, label);
        
        hbox.getChildren().add(vbox);
        
        pane.getChildren().add(hbox);
        return new Scene(pane, 300, 150);        
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(createPreloaderScene());        
        stage.show();
    }
    
    @Override
    public void handleStateChangeNotification(StateChangeNotification scn) {
        if (scn.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }
    
    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        bar.setProgress(pn.getProgress());
    }
    
    public void setProgress(double d) {
        bar.setProgress(d);
    }
    
    public DoubleProperty getProgress() {
        return bar.progressProperty();
    }
    
}
