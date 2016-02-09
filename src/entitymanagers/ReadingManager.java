/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagers;

import entities.IdGenerator;
import entities.Reading;
import entities.ReadingPK;
import entities.Sensor;
import java.sql.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author harvey
 */
public class ReadingManager{

    private final EntityManagerFactory emf;
    private final EntityManager manager;

    public ReadingManager() {
        this.emf = Persistence.createEntityManagerFactory("WeatherStationPU");
        this.manager = emf.createEntityManager();
    }
    public List<Reading> get7DaysSensorReading(int sensorId, Date start, Date end){
        return manager.createNamedQuery("Reading.get7DaysReading").setParameter("sensorId", sensorId).
                setParameter("start", start).setParameter("end", end).getResultList();
    }
    
    public void saveReading(int sensorId, float value, Date date){
        manager.getTransaction().begin();
        IdGenerator generator = new IdGenerator();
        manager.persist(generator);
        manager.refresh(generator);
        Reading reading = new Reading(new ReadingPK(sensorId, generator.getId()), 
                value, date.toString());
        Sensor sensor = manager.find(Sensor.class, sensorId);
        sensor.getReadingList().add(reading);
        manager.persist(reading);
        manager.getTransaction().commit();
    }
}
