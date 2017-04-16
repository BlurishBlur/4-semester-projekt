package org.netbeans.modules.autoupdate.silentupdate.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import org.netbeans.api.autoupdate.UpdateElement;
import org.netbeans.modules.autoupdate.silentupdate.UpdateHandler;

/**
 * @author Niels
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private AnchorPane elementPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int y = 0;
        //for(UpdateElement ue : UpdateHandler.getLocallyInstalled()) {
        for (int i = 0; i < 50; i++) {
            Label label = new Label("hi");
            label.setLayoutY(y);
            y += 10;
            
            elementPane.getChildren().add(label);
        }
        //}
    }
}
