/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagers;

import entities.Sensor;
import entities.Reading;
import java.time.LocalDate;
import java.util.Calendar;
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
    
    /**
     * Get the sensor's readings for the current day since 00h 00min 00s.
     * @param sensor
     * @param date
     * @return the sensor readings since midnight
     */
    public List<Reading> getTodaysReadings(Sensor sensor){
        return manager.createNamedQuery("Reading.findBySensorAndRegDate").setParameter("sensor", sensor)
                .setParameter("day", LocalDate.now().getDayOfMonth())
                .setParameter("month", LocalDate.now().getMonthValue())
                .getResultList();
    }
    
    public synchronized Reading saveReading(int sensorId, float value, Date date){
        manager.getTransaction().begin();
        Sensor sensor = manager.find(Sensor.class, sensorId);
        Reading reading = new Reading(value, date, sensor);
//        reading.setSensor(sensor);
        sensor.getReadingList().add(reading);
        manager.persist(reading);
        manager.flush();
        manager.getTransaction().commit();
        return reading;
    }

    public EntityManager getManager() {
        return manager;
    }
    
}
