/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import entities.Sensor;
import entitymanagers.Constants.TYPE;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author harvey
 */
public class SensorFactory {
    private final List<SensorThread> threads;

    public SensorFactory() {
        threads = new ArrayList<>();
    }

    /**
     * Creates a thread associated with this {@code sensor}.
     * @param sensor
     * @return the created thread
     */
    public SensorThread createSensorThread(Sensor sensor) {
        TYPE type = TYPE.valueOf(sensor.getType());
        SensorThread thread;
        switch (type) {
            case WIND_SPEED:
                thread = new WindSpeedSensor(sensor);
                break;
            case TEMPERATURE:
                thread = new TempSensor(sensor);
                break;
            case PRESSURE:
                thread = new PressSensor(sensor);
                break;
            default:
                thread = new HumSensor(sensor);
        }
        threads.add(thread);
        return thread;
    }
    
    /**
     * Stops the thread associated with this {@code sensor}.
     * @param sensor
     */
    public void interruptThread(Sensor sensor){
        for (SensorThread next : threads) {
            if (next.getSensor().getName().equals(sensor.getName())){ 
                threads.remove(next);
               next.setIsAlive(false);
               return;
            }
        }
    }

    public List<SensorThread> getThreads() {
        return threads;
    }
    
}
