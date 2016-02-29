/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagers;

import entities.Sensor;
import entities.Reading;
import constants.StatusType.Type;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 *
 * @author harvey
 */
public class ReadingManagerTest {

    static private EntityManagerFactory emf;
    static private EntityManager manager;
    static ReadingManager instance;
    static SensorManager sManager;
    static String type = Type.PRESSURE.toString();
    static String status = "OFF";

    @BeforeClass
    public static void init() {
        ReadingManagerTest.emf = Persistence.createEntityManagerFactory("WeatherStationPU");
        ReadingManagerTest.manager = emf.createEntityManager();
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
        Sensor sensor = sManager.findByName(name);
        for (int i = 1; i < 10; i++) {
            float value = (float) i / 10 + 9.54F;
            Date date = Timestamp.valueOf(String.format("2015-02-%d 00:00:00", i));
            instance.saveReading(sensor.getId(), value, date);
        }

        List<Reading> expResult = null;
        List<Reading> result = instance.get7DaysSensorReading(sensor,
                Timestamp.valueOf("2015-02-2 00:00:00"), Timestamp.valueOf("2015-02-8 00:00:00"));
        assertEquals(result.size(), 7);
//        assertEquals(expResult, result);
    }

    @Test
    public void testGetTodaysReadings() {
        System.out.println("getTodaysReadings");
        String name = "PressureSensor3";
        sManager.createSensor(name, type, status, 1, 2);
        
        Sensor sensor = sManager.findByName(name);
        for (int i = 1; i < 10; i++) {
            float value = (float) i / 10 + 9.54F;
            Date date = Timestamp.valueOf(LocalDateTime.of(LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth(),
                    i, LocalDateTime.now().getMinute()));
            instance.saveReading(sensor.getId(), value, date);
        }
        List<Reading> result = instance.getTodaysReadings(sensor);
        assertEquals(result.size(), 9);
        for (int i = 0; i < 9; i++) {
            Instant instant = Instant.ofEpochMilli(result.get(i).getRegDate().getTime());
            LocalDateTime datetime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            assertEquals("Days of the month differ", datetime.getDayOfMonth(), LocalDate.now().getDayOfMonth());

            assertEquals("Month values differ", datetime.getMonth(), LocalDate.now().getMonth());
            assertEquals("Hours not the same", datetime.getHour(), i + 1);
        }
    }

    /**
     * Test of saveReading method, of class ReadingManager.
     */
    @Test
    @Ignore
    public void testSaveReading() {
        System.out.println("saveReading");
        String name = "PressureSensor1";
        sManager.createSensor(name, type, status, 1, 2);
        Sensor sensor = sManager.findByName(name);
        float value = 5.3F;
        Date date = new Date(new Date().getTime());
        Reading saveReading = instance.saveReading(sensor.getId(), value, date);
//        instance.getManager().f
        Reading reading = instance.getManager().find(Reading.class, saveReading.getId());
        assertNotNull(reading);
//        Timestamp.
    }
}
