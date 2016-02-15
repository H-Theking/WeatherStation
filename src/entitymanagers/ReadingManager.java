/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagers;

import entities.Sensor;
import entities.Reading;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author harvey
 */
public class ReadingManager{
    private final EntityManager manager;

    public ReadingManager(EntityManager manager) {
        this.manager = manager;
    }

    
    public List<Reading> get7DaysSensorReading(Sensor sensor, Date start, Date end){
        return manager.createNamedQuery("Reading.get7DaysReading").setParameter("sensor", sensor).
                setParameter("start", start).setParameter("end", end).getResultList();
    }
    
    public int saveReading(int sensorId, float value, Date date){
        manager.getTransaction().begin();
        Sensor sensor = manager.find(Sensor.class, sensorId);
        Reading reading = new Reading(value, date);
        reading.setSensor(sensor);
//        sensor.getReadingList().add(reading);
        manager.persist(reading);
        manager.flush();
        manager.getTransaction().commit();
        return reading.getId();
    }

    public EntityManager getManager() {
        return manager;
    }
    
}
