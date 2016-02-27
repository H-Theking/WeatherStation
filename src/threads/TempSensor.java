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
public class TempSensor extends SensorThread{

    public TempSensor(Sensor sensor) {
        super(sensor);
    }
    

    @Override
    public void run() {
        while(super.getIsAlive()){
            try {
                super.setSensorValue(18 + super.getRandomNumbers().nextInt(10) +
                        super.getRandomNumbers().nextDouble());
                System.out.println(super.getSensorValue());
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Temp sensor stopped");
    }
}
