package org.netbeans.modules.autoupdate.silentupdate.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.netbeans.api.autoupdate.UpdateUnit;
import org.netbeans.modules.autoupdate.silentupdate.UpdateHandler;

/**
 * @author Niels
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label lblAmountLoaded;
    @FXML
    private VBox moduleContainer;

    private List<UpdateUnit> allModules; // de custom moduler der kan installeres
    //private Collection<UpdateElement> installedModules; // de custom moduler der er installeret pt
    @FXML
    private TextField txtSearch;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allModules = UpdateHandler.getSilentUpdateProvider().getUpdateUnits();
        updateAmountLabel();
        createModuleCheckBoxes("");
    }

    private void createModuleCheckBoxes(String searchTerm) {
        moduleContainer.getChildren().clear();
        List<CheckBox> checkBoxes = new ArrayList<>();
        String name;
        for (UpdateUnit unit : allModules) {
            name = unit.getCodeName().replace("rpg.", "");
            if (name.toLowerCase().contains(searchTerm.toLowerCase())) {
                CheckBox checkBox = new CheckBox(name);
                checkBox.setSelected(unit.getInstalled() != null);
                if (name.equals("GameEngine") || name.startsWith("Common")) {
                    checkBox.setDisable(true);
                }
                else {
                    checkBox.setOnAction((e) -> {
                        new Thread(() -> {
                            update(unit.getCodeName(), checkBox.isSelected());
                        }).start();
                    });
                }
                checkBoxes.add(checkBox);
            }
        }
        checkBoxes.sort(new CheckBoxComparator());
        moduleContainer.getChildren().addAll(checkBoxes);
    }

    private void update(String codeName, boolean load) {
        if (load) {
            UpdateHandler.load(codeName);
        }
        else {
            UpdateHandler.unload(codeName);
        }
        //UpdateHandler.checkAndHandleUpdates();
        updateAmountLabel();
    }

    private void updateAmountLabel() {
        Platform.runLater(() -> {
            int amountOfModules = UpdateHandler.getLocallyInstalled().size();
            lblAmountLoaded.setText(amountOfModules + (amountOfModules == 1 ? " module" : " modules") + " currently loaded");
        });
    }

    @FXML
    private void searchModules(KeyEvent event) {
        createModuleCheckBoxes(txtSearch.getText());
    }
    
    private class CheckBoxComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            return Boolean.valueOf(((CheckBox) o1).isDisabled()).compareTo(((CheckBox) o2).isDisabled());
        }
        
    }

}
