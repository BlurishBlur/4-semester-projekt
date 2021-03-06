package org.netbeans.modules.autoupdate.silentupdate.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.netbeans.modules.autoupdate.silentupdate.UpdateHandler;
import org.openide.util.Exceptions;

/**
 * @author Niels
 */
public class GUILauncher extends Application {

    private Stage loadStage;
    private Stage mainStage;
    private boolean doneLoading = false;

    @Override
    public void start(Stage stage) throws Exception {
        NewFXPreloader p = new NewFXPreloader();
        loadStage = stage;
        p.start(loadStage);
        p.handleProgressNotification(new ProgressNotification(-1));

        p.getProgress().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.doubleValue() == 1) {
                try {
                    p.handleStateChangeNotification(new StateChangeNotification(StateChangeNotification.Type.BEFORE_START));
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("org/netbeans/modules/autoupdate/silentupdate/resources/FXMLDocument.fxml"));
                    Scene scene = new Scene(root);
                    mainStage = new Stage();
                    mainStage.setScene(scene);
                    mainStage.setTitle("Module manager");
                    mainStage.show();
                }
                catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
        loadStage.show();

        new Thread(() -> {
            while (!doneLoading) {
                if (!UpdateHandler.getSilentUpdateProvider().getUpdateUnits().isEmpty() && UpdateHandler.getLocallyInstalled().size() == UpdateHandler.getSilentUpdateProvider().getUpdateUnits().size()) {
                    Platform.runLater(() -> {
                        p.setProgress(1.0);
                        doneLoading = true;
                    });
                }
            }
        }).start();
    }

    public static void initiate(String[] args) {
        launch(args);
    }
}
