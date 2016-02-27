/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import entities.Sensor;
import java.security.SecureRandom;

/**
 *
 * @author harvey
 */
public abstract class SensorThread implements Runnable{
    private Sensor sensor;
    private final SecureRandom randomNumbers;
    private double sensorValue;
    private boolean isAlive;

    public SensorThread(Sensor sensor) {
        this.sensor = sensor;
        this.randomNumbers = new SecureRandom();
        isAlive = true;
    }

    
    @Override
    abstract public void run();
    
    public void stop(){
        isAlive = false;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(double sensorValue) {
        this.sensorValue = sensorValue;
    }

    public SecureRandom getRandomNumbers() {
        return randomNumbers;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    
}
