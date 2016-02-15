/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.dialogs;

import entities.Location;
import entities.Sensor;
import entitymanagers.Constants.STATUS;
import entitymanagers.Constants.TYPE;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author harvey
 */
public class AddSensorController implements Initializable {

    @FXML
    private TextField sensorName;
    @FXML
    private ComboBox<TYPE> type;
    @FXML
    private TextField latitude;
    @FXML
    private TextField longitude;
    @FXML
    private RadioButton sensorOn;
    @FXML
    private RadioButton sensorOff;

    private Stage dialogStage;
    private Sensor sensor;
    private ToggleGroup group;
    private boolean okClicked = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        group = new ToggleGroup();
        sensorOn.setToggleGroup(group);
        sensorOn.setSelected(true);
        sensorOff.setToggleGroup(group);
    }

    public void initSensorController(Stage dialogStage, Sensor sensor) {
        this.dialogStage = dialogStage;
        this.sensor = sensor;
    }

    @FXML
    private void cancel(ActionEvent event) {
        dialogStage.close();
    }
    
    private void setSersorUI(){
        sensorName.setText(sensor.getName());
        type.getSelectionModel().select(TYPE.valueOf(sensor.getType()));
        longitude.setText(String.valueOf(sensor.getLocation().getLongitude()));
        latitude.setText(String.valueOf(sensor.getLocation().getLatitude()));
        group.selectToggle(sensor.getStatus().equalsIgnoreCase(STATUS.OFF.toString())? sensorOff:sensorOn);
    }

    @FXML
    private void setData(ActionEvent event) {
         if (isInputValid()) {
             sensor.setName(sensorName.getText());
             sensor.setType(type.getSelectionModel().getSelectedItem().name().toUpperCase());
             sensor.setLocation(new Location());
             sensor.getLocation().setLatitude(Float.parseFloat(latitude.getText()));
             sensor.getLocation().setLongitude(Float.parseFloat(longitude.getText()));
             sensor.setStatus(group.getSelectedToggle().getUserData().toString());
             okClicked = true;
             dialogStage.close();
         }
    }
    
    public boolean okClicked(){
        return okClicked;
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (sensorName.getText() == null || sensorName.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }
        if (latitude.getText() == null || latitude.getText().length() == 0) {
            errorMessage += "No valid latitude!\n";
        } else {
            try {
                Double.parseDouble(latitude.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Latitude ust be a floating point number!\n";
            }
        }
        if (longitude.getText() == null || longitude.getText().length() == 0) {
            errorMessage += "No valid latitude!\n";
        } else {
            try {
                Double.parseDouble(longitude.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Latitude ust be a floating point number!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
