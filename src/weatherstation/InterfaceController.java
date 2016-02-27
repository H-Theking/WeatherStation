/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherstation;

import entities.Reading;
import entities.Sensor;
import entitymanagers.Constants.STATUS;
import entitymanagers.Constants.TYPE;
import entitymanagers.ReadingManager;
import entitymanagers.SensorManager;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import resources.dialogs.AddSensorController;
import threads.SensorThread;
import threads.SensorFactory;
import threads.UpdateThread;

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
    public LineChart<String, Number> lineChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private ToggleButton toggleStateButton;

    private final EntityManagerFactory emf;
    private final EntityManager manager;
    private final SensorManager sensorManager;
    public static ReadingManager readingManager;

    private final String dialogPath = "/resources/dialogs/addSensor.fxml";
    private Stage primaryStage;
    public static final SensorFactory sensorFactory = new SensorFactory();
    private final ExecutorService executorService;
    private ListView<Sensor> currentSelectedList, previousList;
    private final List<UpdateThread> regUpdateSensors;
    public static final Series<String, Number> series = new Series<>();;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        primaryStage = WeatherStation.getPrimaryStage();
        primaryStage.setOnCloseRequest((WindowEvent event1) -> {
            executorService.shutdownNow();
        });
        ListUpdateListener allListListener = new ListUpdateListener();
        //Register listview listeners
        speedListView.getSelectionModel().selectedItemProperty().addListener(allListListener);
        tempListView.getSelectionModel().selectedItemProperty().addListener(allListListener);
        pressListView.getSelectionModel().selectedItemProperty().addListener(allListListener);
        humListview.getSelectionModel().selectedItemProperty().addListener(allListListener);

        xAxis.setLabel("Time");
        lineChart.getData().add(series);
    }

    public InterfaceController() {
        this.emf = Persistence.createEntityManagerFactory("WeatherStationPU");
        executorService = Executors.newCachedThreadPool();

        this.manager = emf.createEntityManager();
        InterfaceController.readingManager = new ReadingManager(manager);
        this.sensorManager = new SensorManager(manager);
        this.regUpdateSensors = new ArrayList<>();
        
        this.previousList = null;
    }

    @FXML
    private void addSensor() {
        try {
            Sensor sensor = new Sensor();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WeatherStation.class.getResource(dialogPath));

            Stage dialogStage = createDialogStage("Create New Sensor", loader);
            AddSensorController controller = loader.getController();
            controller.initSensorController(dialogStage, sensor);
            dialogStage.showAndWait();

            if (controller.okClicked()) {
                //Write to database
                sensor = sensorManager.createSensor(sensor);
                addSensorToList(sensor);
//                if (sensor.getStatus().equals(STATUS.ON.toString())) {
//                    sensor.setStatus(STATUS.OFF.toString());
//                    //can only be null at application start, which means currentselectedList is null too
//                    if (previousList == null) {
//                        setCurrentList(sensor);
//                        currentSelectedList.getSelectionModel().clearAndSelect(0);
//                    }
//                    else{//Is not the first launch, start the thread anyways but dont display
//                        
//                    }
//                }
                writeInfo("A new sensor " + sensor.getName() + " was created successfully!");
            }
        } catch (IOException ex) {
            catchException(ex);
        }
    }

    private void editSensor(Sensor sensor) {
        try {

            String oldName = sensor.getName();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WeatherStation.class.getResource(dialogPath));
            Stage dialogStage = createDialogStage("Edit Sensor", loader);
            AddSensorController controller = loader.getController();
            controller.initSensorController(dialogStage, sensor);
            controller.populateSersorUI();
            dialogStage.showAndWait();

            if (controller.okClicked()) {
                //Write to database
                sensorManager.editSensorName(oldName, sensor);
                writeInfo("Sensor name changed successfully edited!");
            }
        } catch (IOException ex) {
            catchException(ex);
        }
    }

    @FXML
    private void exitApp() {
        Platform.exit();
    }

    @FXML
    private void launchSettings(Event event) {
    }

    @FXML
    private void about(ActionEvent event) {
    }

    @FXML
    private void toggleSensorStatus() {
        Sensor selectedItem = currentSelectedList.getSelectionModel().getSelectedItem();

        if (selectedItem.getStatus().equals(STATUS.OFF.toString())) {
            //update sensor status
            sensorManager.switchSensorStatus(selectedItem);
//            selectedItem.setStatus(STATUS.ON.toString());

            SensorThread sensorThread = sensorFactory.createSensorThread(selectedItem);
            executorService.execute(sensorThread);
            //start updateThread for this sensor if not already started
            if (regUpdateSensors.isEmpty()) {
                UpdateThread updateThread = new UpdateThread(selectedItem, 10000);//value to change
                updateThread.setIsVisibleOnChart(true);
                regUpdateSensors.add(updateThread);
                executorService.execute(updateThread);
            } else {
                boolean isIn = false;
                for (UpdateThread thread : regUpdateSensors) {
                    if (thread.getSensor().equals(selectedItem)) {
                        isIn = true;
                        break;
                    }
                }
                if (!isIn) {
                    UpdateThread updateThread = new UpdateThread(selectedItem, 10000);//value to change
                    updateThread.setIsVisibleOnChart(true);
                    regUpdateSensors.add(updateThread);
                    executorService.execute(updateThread);
                }
            }
        } else if (selectedItem.getStatus().equals(STATUS.ON.toString())) {
            //update sensor status
            sensorManager.switchSensorStatus(selectedItem);
//            selectedItem.setStatus(STATUS.OFF.toString());
            //stop reading weather value
            sensorFactory.interruptThread(selectedItem);

            for (UpdateThread thread : regUpdateSensors) {
                if (thread.getSensor().equals(selectedItem)) {
                    //stop updating chart and database
                    thread.setIsAlive(false);
                    thread.stop();
                    this.regUpdateSensors.remove(thread);
                    break;
                }
            }
        }
    }

    private void startSensorThread(Sensor sensor) {

    }

    @FXML
    private void getDataFromDate() {
    }

    @FXML
    private void setCurrentValue() {
    }

    private Stage createDialogStage(String title, FXMLLoader loader) throws IOException {

        final Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(primaryStage);
        AnchorPane pane = (AnchorPane) loader.load();
        Scene scene = new Scene(pane);
        dialogStage.setScene(scene);
        dialogStage.resizableProperty().set(false);
        return dialogStage;
    }

    private void catchException(IOException ex) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Unexpected Error");
        String s = "An error occured while trying to open the add dialog!";
        alert.setContentText(s);
        alert.showAndWait();

        Logger.getLogger(InterfaceController.class.getName()).log(Level.SEVERE, null, ex);
    }

    private void writeInfo(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Request Completed!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addSensorToList(Sensor sensor) {
        switch (sensor.getType()) {
            case "HUMIDITY":
                humListview.getItems().add(sensor);
                break;
            case "PRESSURE":
                pressListView.getItems().add(sensor);
                break;
            case "TEMPERATURE":
                tempListView.getItems().add(sensor);
                break;
            case "WIND_SPEED":
                speedListView.getItems().add(sensor);
        }
    }

    private void setCurrentList(Sensor sensor) {
        switch (TYPE.valueOf(sensor.getType())) {
            case HUMIDITY:
                currentSelectedList = humListview;
                yAxis.setLabel("Humidity(%)");
                break;
            case PRESSURE:
                currentSelectedList = pressListView;
                yAxis.setLabel("Pressure(mb)");
                break;
            case TEMPERATURE:
                currentSelectedList = tempListView;
                yAxis.setLabel("Temperature(°c)");
                break;
            default:
                currentSelectedList = speedListView;
                yAxis.setLabel("Wind(mph)");
        }
    }

    @FXML
    private void showContextMenu(ContextMenuEvent event) {
        final List<Sensor> selectedItems = ((ListView<Sensor>) event.getSource()).getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            return;
        }
        MenuItem edit = new MenuItem("Edit sensor");
        MenuItem remove = new MenuItem("Delete");
        ContextMenu contextMenu = new ContextMenu();
        if (selectedItems.size() == 1) {
            contextMenu.getItems().add(edit);
        }
        contextMenu.getItems().add(remove);
        contextMenu.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());

        edit.setOnAction((ActionEvent event1) -> {
            ObservableList<Sensor> items = ((ListView<Sensor>) event.getSource()).getItems();
            int indexOfItem = items.indexOf(selectedItems.get(0));
            editSensor(selectedItems.get(0));
            Sensor sensor = sensorManager.findByName(selectedItems.get(0).getName());
            items.set(indexOfItem, sensor);
        });
        remove.setOnAction((ActionEvent e) -> {
            selectedItems.stream().forEach(((ListView<Sensor>) event.getSource()).getItems()::remove);
        });
    }

    public void populateGraph(Sensor sensor, LocalDateTime datetime, List<Reading> readings) {
        //defining the XAxis
        readings.stream().forEach((read) -> {
            System.out.print("[" + read.getId() + ":" + read.getReadValue() + "], ");
        });
        System.out.println("");

        lineChart.setTitle(sensor.getName() + " Readings of " + String.format("%d/%d/%d",
                datetime.getDayOfMonth(), datetime.getMonthValue(), datetime.getYear()));

        series.getData().clear();
        readings.stream().forEach((reading) -> {
            Instant ins = Instant.ofEpochMilli(reading.getRegDate().getTime());
            LocalDateTime datet = LocalDateTime.ofInstant(ins, ZoneId.systemDefault());
            series.getData().add(new XYChart.Data(String.format("%2d:%2d:%2d", datet.getHour(),
                    datet.getMinute(), datet.getSecond()), reading.getReadValue()));
        });
    }

    public class ListUpdateListener implements ChangeListener<Sensor> {

        @Override
        public void changed(ObservableValue<? extends Sensor> observable, Sensor oldValue, Sensor newValue) {
            if (toggleStateButton.isDisabled()) {
                toggleStateButton.setDisable(false);
            }

            //if same values in same list, exit
            if (oldValue == newValue || newValue == null) {
                return;
            }
            if (oldValue != null) {
                System.out.print("old: " + oldValue.getName());

            }
            System.out.println(" new: " + newValue.getName());
            toggleStateButton.setSelected(newValue.getStatus().equals(STATUS.ON.toString()));
            switch (TYPE.valueOf(newValue.getType())) {
                case HUMIDITY:
                    currentSelectedList = humListview;
                    yAxis.setLabel("Humidity(%)");
                    break;
                case PRESSURE:
                    currentSelectedList = pressListView;
                    yAxis.setLabel("Pressure(mb)");
                    break;
                case TEMPERATURE:
                    currentSelectedList = tempListView;
                    yAxis.setLabel("Temperature(°c)");
                    break;
                default:
                    currentSelectedList = speedListView;
                    yAxis.setLabel("Wind(mph)");
            }

            if (!currentSelectedList.equals(previousList)) {
                if (previousList != null) {
                    regUpdateSensors.stream().forEach((thread) -> {
                        if (thread.getSensor().getName().equals(
                                previousList.getSelectionModel().getSelectedItem().getName())) {
                            thread.setIsVisibleOnChart(false);
                        } else if (thread.getSensor().getName().equals(newValue.getName())) {
                            thread.setIsVisibleOnChart(true);
                        }
                    });
                    previousList.getSelectionModel().clearSelection();
                }

            } else {
                regUpdateSensors.stream().forEach((thread) -> {
                    if (thread.getSensor().equals(oldValue)) {
                        thread.setIsAlive(false);
                    } else if (thread.getSensor().getName().equals(newValue.getName())) {
                        thread.setIsVisibleOnChart(true);
                    }
                });
            }
            previousList = currentSelectedList;

            /*
             Read the current sensor's data for today from the database
             Display it in the graph
             Display it under the tabPane approprate table and make that tab selected
             */
//            read current sensor value(s)for today from database
            synchronized (InterfaceController.class){
                List<Reading> todaysReadings = readingManager.getTodaysReadings(newValue);
                populateGraph(newValue, LocalDateTime.now(), todaysReadings);
            }
        }
    }
}
