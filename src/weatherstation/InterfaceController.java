/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherstation;

import entities.Sensor;
import entitymanagers.ReadingManager;
import entitymanagers.SensorManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import resources.dialogs.AddSensorController;

/**
 * FXML Controller class
 *
 * @author harvey
 */
public class InterfaceController implements Initializable {

    @FXML
    private ListView<Sensor> humListview;
    @FXML
    private ListView<Sensor> pressListView;
    @FXML
    private ListView<Sensor> tempListView;
    @FXML
    private ListView<Sensor> speedListView;
    @FXML
    private AnchorPane mondayPane;
    @FXML
    private AnchorPane tuesdayPane;
    @FXML
    private AnchorPane wedPane;
    @FXML
    private AnchorPane thursPane;
    @FXML
    private AnchorPane friPane;
    @FXML
    private AnchorPane satPane;
    @FXML
    private AnchorPane sunPane;
    @FXML
    private LineChart<Double, ?> lineChart;

    private final EntityManagerFactory emf;
    private final EntityManager manager;
    private final SensorManager sensorManager;
    private final ReadingManager readingManager;

    private final String dialogPath = "/resources/dialogs/addSensor.fxml";
    private Stage primaryStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        primaryStage = WeatherStation.getPrimaryStage();
    }

    public InterfaceController() {
        this.emf = Persistence.createEntityManagerFactory("WeatherStationPU");
        this.manager = emf.createEntityManager();
        this.readingManager = new ReadingManager(manager);
        this.sensorManager = new SensorManager(manager);
    }

    @FXML
    private void addSensor(ActionEvent event) {
        try {
            Sensor sensor = new Sensor();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WeatherStation.class.getResource(dialogPath));

            final Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            AnchorPane pane = (AnchorPane) loader.load();
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);
            AddSensorController controller = loader.getController();
            controller.initSensorController(dialogStage, sensor);
            dialogStage.resizableProperty().set(false);
            dialogStage.showAndWait();

            if (controller.okClicked()) {
                //Write to database
                sensorManager.createSensor(sensor);
            }
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Unexpected Error");
            String s = "An error occured while trying to open the add dialog!";
            alert.setContentText(s);
            alert.showAndWait();

            Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void exitApp(ActionEvent event) {
    }

    @FXML
    private void editSensor(ActionEvent event) {
    }

    @FXML
    private void removeSensor(ActionEvent event) {
    }

    @FXML
    private void launchSettings(Event event) {
    }

    @FXML
    private void about(ActionEvent event) {
    }

    @FXML
    private void toggleSensorStatus(ActionEvent event) {
    }

    @FXML
    private void getDataFromDate(ActionEvent event) {
    }

    @FXML
    private void setCurrentValue(ActionEvent event) {
    }

}
