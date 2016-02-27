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
public class PressSensor extends SensorThread {

    public PressSensor(Sensor sensor) {
        super(sensor);
    }

    @Override
    public void run() {
        while (super.getIsAlive()) {
            try {
                super.setSensorValue(1008 + super.getRandomNumbers().nextInt(7));
                Thread.sleep(1000);
                System.out.println(super.getSensorValue());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Pressure thread stopped");
    }

}
