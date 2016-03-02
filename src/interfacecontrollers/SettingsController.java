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
import constants.Settings;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        hour.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            List<Integer> time = new ArrayList<>(60);
            if (newValue.equals(0) && min.getSelectionModel().getSelectedItem().equals(0)) {

                for (int i = 5; i < 61; i++) {
                    time.add(i);
                }
            } else {
                for (int i = 1; i < 61; i++) {
                    time.add(i);
                }
            }
            seconds.getItems().setAll(time);
        });
        min.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
                    List<Integer> time = new ArrayList<>(61);
                    if (hour.getSelectionModel().getSelectedItem().equals(0) && newValue.equals(0)) {
                        for (int i = 5; i < 61; i++) {
                            time.add(i);
                        }
                    } else {
                        for (int i = 1; i < 61; i++) {
                            time.add(i);
                        }
                    }
                    seconds.getItems().setAll(time);
                });
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
        okClicked = true;
        dialogStage.close();
    }

    private void cancel() {
        okClicked = false;
        dialogStage.close();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void populateBoxes() {
        List<Integer> time = new ArrayList<>(61);
        for (int i = 0; i < 61; i++) {
            time.add(i);
        }
        min.getItems().addAll(time);
        time.clear();
        for (int i = 5; i < 61; i++) {
            time.add(i);
        }
        seconds.getItems().addAll(time);
        hour.getItems().addAll(0, 1, 2, 3);

        HashMap<String, String> readRecords = Settings.readRecords();
        if (!readRecords.isEmpty()) {
            int interval = Integer.parseInt(readRecords.get("interval"))/1000;
            Integer hours = interval / 3600;
            Integer minute = (interval % 3600) / 60;
            Integer second = (interval % 3600) % 60;

            seconds.getSelectionModel().select(second);
            min.getSelectionModel().select(minute);
            hour.getSelectionModel().select(hours);

            return;
        }

        seconds.getSelectionModel().select(0);
        min.getSelectionModel().select(0);
        hour.getSelectionModel().select(0);
    }

}
