/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfacecontrollers;

import constants.SensorUnits.HumidityUnits;
import constants.SensorUnits.PressureUnits;
import constants.SensorUnits.TemperatureUnits;
import constants.SensorUnits.WindSpeedUnits;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author harvey
 */
public class SettingsController implements Initializable {

    @FXML
    private ComboBox<Integer> hour;
    @FXML
    private ComboBox<Integer> min;
    @FXML
    private ComboBox<Integer> seconds;
    @FXML
    private ComboBox<String> humidity;
    @FXML
    private ComboBox<String> pressure;
    @FXML
    private ComboBox<String> temp;
    @FXML
    private ComboBox<String> velocity;

    private Stage dialogStage;
    private boolean okClicked;
    private HashMap<String, String> settings;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        okClicked = false;
        populateBoxes();
    }

    public void settingsController(HashMap<String, String> settings, Stage stage) {
        this.settings = settings;
        this.dialogStage = stage;
    }
    
    

    @FXML
    private void done() {
        settings.put("Hour", hour.getSelectionModel().getSelectedItem().toString());
        settings.put("Minute", min.getSelectionModel().getSelectedItem().toString());
        settings.put("Second", seconds.getSelectionModel().getSelectedItem().toString());
        velocity.getSelectionModel().getSelectedItem();
        settings.put("speed", velocity.getSelectionModel().getSelectedItem());
        temp.getSelectionModel().getSelectedItem();
        settings.put("temp", temp.getSelectionModel().getSelectedItem());
        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void cancel() {
        okClicked = false;
        dialogStage.close();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void populateBoxes() {
        for (HumidityUnits units : HumidityUnits.values()) {
            humidity.getItems().add(units.toString());
        }
        for (TemperatureUnits units : TemperatureUnits.values()) {
            temp.getItems().add(units.toString());
        }
        for (PressureUnits units : PressureUnits.values()) {
            pressure.getItems().add(units.toString());
        }
        for (WindSpeedUnits units : WindSpeedUnits.values()) {
            velocity.getItems().add(units.toString());
        }
        humidity.getSelectionModel().select(0);
        temp.getSelectionModel().select(0);
        pressure.getSelectionModel().select(0);
        velocity.getSelectionModel().select(0);
    }
}
