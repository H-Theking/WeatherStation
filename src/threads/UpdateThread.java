/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import entities.Reading;
import entities.Sensor;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import static weatherstation.InterfaceController.readingManager;
import static weatherstation.InterfaceController.sensorFactory;
import static weatherstation.InterfaceController.series;

/**
 *
 * @author harvey
 */
public class UpdateThread extends SensorThread {
    
    private final Sensor sensor;
    public static int updateInterval;
    private boolean isVisibleOnChart;
    
    public UpdateThread(Sensor sensor, int interrupt) {
        super(sensor);
        this.sensor = sensor;
        updateInterval = interrupt;
//        UpdateThread.series = series;
        isVisibleOnChart = false;
    }
    
    @Override
    public void run() {
        while (super.getIsAlive()) {
            try {
                Platform.runLater(() -> {
                    updateSensorValue(sensor);
                });
                Thread.currentThread().sleep(updateInterval);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void addChartData(Reading reading) {
        Instant instant = Instant.ofEpochMilli(reading.getRegDate().getTime());
        LocalDateTime datetime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        series.getData().add(new XYChart.Data(String.format("%2d:%2d:%2d", datetime.getHour(),
                datetime.getMinute(), datetime.getSecond()), reading.getReadValue()));
    }
    
    public void updateSensorValue(Sensor sensor) {
        for (SensorThread thread : sensorFactory.getThreads()) {
            if (thread.getSensor().equals(sensor)) {
                Reading newData = readingManager.saveReading(sensor.getId(), (float) thread.getSensorValue(),
                        Timestamp.valueOf(LocalDateTime.now()));
                //this sensor is currently selected
                if (isVisibleOnChart) {
                    synchronized (UpdateThread.class) {
                        addChartData(newData);
                    }
                }
                break;
            }
            
        }
    }
    
    public void setIsVisibleOnChart(boolean isVisibleOnChart) {
        this.isVisibleOnChart = isVisibleOnChart;
    }
    
}
