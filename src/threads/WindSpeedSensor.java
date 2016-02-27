/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import entities.Sensor;

/**
 *
 * @author harvey
 */
public class WindSpeedSensor extends SensorThread {

    public WindSpeedSensor(Sensor sensor) {
        super(sensor);
    }

    @Override
    public void run() {
        while (super.getIsAlive()) {
            try {
                //                min speed     I chose 5m/s as max wind speed
                super.setSensorValue(0.1 + super.getRandomNumbers().nextInt(4)
                        + super.getRandomNumbers().nextDouble());
                System.out.println(super.getSensorValue());
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Wind sensor stopped");
    }

}
