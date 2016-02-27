/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import entities.Sensor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harvey
 */
public class HumSensor extends SensorThread {

    public HumSensor(Sensor sensor) {
        super(sensor);
    }

    @Override
    public void run() {
        while (super.getIsAlive()) {
            try {
                super.setSensorValue(30 + super.getRandomNumbers().nextInt(60));
                System.out.println(super.getSensorValue());
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Hum sensor stopped");
    }

}
