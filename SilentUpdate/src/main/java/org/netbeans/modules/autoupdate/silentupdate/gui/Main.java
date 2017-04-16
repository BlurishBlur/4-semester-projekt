package org.netbeans.modules.autoupdate.silentupdate.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Niels
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("org/netbeans/modules/autoupdate/silentupdate/resources/FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("TITEL");
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
