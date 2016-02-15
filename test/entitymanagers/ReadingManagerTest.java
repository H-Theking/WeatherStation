/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagers;

import entities.Sensor;
import entities.Reading;
import entitymanagers.Constants.TYPE;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author harvey
 */
public class ReadingManagerTest {

    private EntityManagerFactory emf;
    private EntityManager manager;
    ReadingManager instance;
    SensorManager sManager;
    String type = TYPE.PRESSURE.toString();
    String status = "OFF";

    @BeforeClass
    public void init() {
        this.emf = Persistence.createEntityManagerFactory("WeatherStationPU");
        this.manager = emf.createEntityManager();
        instance = new ReadingManager(manager);
        sManager = new SensorManager(manager);
    }

    /**
     * Test of get7DaysSensorReading method, of class ReadingManager.
     */
    @Test
    public void testGet7DaysSensorReading() {
        System.out.println("get7DaysSensorReading");
        String name = "PressureSensor2";
        sManager.createSensor(name, type, status, 1, 2);
        Sensor sensor = sManager.findSensorByName(name);
        for (int i = 1; i < 10; i++) {
            float value = (float) i / 10 + 9.54F;
            Date date = new Date(Timestamp.valueOf(String.format("2015-02-%d 00:00:00", i)).getTime());
            int id = instance.saveReading(sensor.getId(), value, date);
        }

        List<Reading> expResult = null;
        List<Reading> result = instance.get7DaysSensorReading(sensor,
                Timestamp.valueOf("2015-02-2 00:00:00"), Timestamp.valueOf("2015-02-8 00:00:00"));
        assertEquals(result.size(), 7);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of saveReading method, of class ReadingManager.
     */
    @Test
    public void testSaveReading() {
        System.out.println("saveReading");
        String name = "PressureSensor1";
        sManager.createSensor(name, type, status, 1, 2);
        Sensor sensor = sManager.findSensorByName(name);
        float value = 5.3F;
        Date date = new Date(new Date().getTime());
        int id = instance.saveReading(sensor.getId(), value, date);
//        instance.getManager().f
        Reading reading = instance.getManager().find(Reading.class, id);
        assertNotNull(reading);
//        Timestamp.
    }
}
