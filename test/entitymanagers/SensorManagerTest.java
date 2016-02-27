/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagers;

import entities.Sensor;
import entities.Location;
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
public class SensorManagerTest {
    private static EntityManagerFactory emf;
    private static  EntityManager manager;
    static SensorManager instance;
    String type = Constants.TYPE.PRESSURE.toString();
    String status = "OFF";

    @BeforeClass
    public static void init() {
        SensorManagerTest.emf = Persistence.createEntityManagerFactory("WeatherStationPU");
        SensorManagerTest.manager = emf.createEntityManager();
        instance = new SensorManager(manager);
    }


    /**
     * Test of createSensor method, of class SensorManager.
     */
    @Test
    public void testCreateSensor() {
        System.out.println("createSensor");
        String name = "TempSensor";
        float lon = 0.0F;
        float lat = 0.0F;
        instance.createSensor(name, type, status, lon, lat);
        Sensor sensor = instance.findByName(name);
        assertNotNull(sensor);
    }

    /**
     * Test of editSensorName method, of class SensorManager.
     */
    @Test
    public void testEditSensorName() {
        String name = "TempSensor2";
        String newName = "TempSensor3";
        instance.createSensor(name, type, status, 1, 2);
        
        instance.editSensorName(name, newName);
        Sensor sensor = instance.findByName(newName);
        Location location = instance.getManager().find(Location.class, sensor.getId());
        assertNotNull(sensor);
        assertNotNull(location);
        assertEquals(sensor.getName(), newName);
    }

    /**
     * Test of changeSensorLocation method, of class SensorManager.
     */
    @Test
    public void testChangeSensorLocation() {
        System.out.println("changeSensorLocation");
         String name = "TempSensor6";
        instance.createSensor(name, type, status, 8, 5);
        float lon = 5;
        float lat = (float)7.2;
        Sensor sensor = instance.findByName(name);
        instance.changeSensorLocation(sensor.getId(), lon, lat);
        Location location = instance.getManager().find(Location.class, sensor.getId());
        assertNotNull(location);
        assertEquals(location.getLatitude(), lat, 0.05);
        assertEquals(location.getLongitude(), lon, 0.05);
    }

    /**
     * Test of switchSensorStatus method, of class SensorManager.
     */
    @Test
    public void testSwitchSensorStatus() {
        System.out.println("switchSensorStatus");
        String name = "TempSensor4";
        instance.createSensor(name, type, status, 2, 1);
        Sensor sensor = instance.findByName(name);
        Location l = new Location(sensor.getId(), 1, 2);
        String status = "ON";
        instance.switchSensorStatus(sensor);
        assertEquals(sensor.getStatus(), status);
    }

    /**
     * Test of removeSensor method, of class SensorManager.
     */
    @Test
    public void testRemoveSensor() {
        System.out.println("removeSensor");
         String name = "TempSensor5";
        instance.createSensor(name, type, status, 2, 2);
        instance.removeSensor(name);
        Sensor sensor = instance.findByName(name);
        assertNull(sensor);
    }

    /**
     * Test of findByName method, of class SensorManager.
     */
//    @Test
    public void testFindSensorByName() {
        System.out.println("findSensorByName");
        String name = "TempSensor7";
        instance.createSensor(name, type, status, 8, 5);
        Sensor result = instance.findByName(name);
        assertNotNull(result);
    }
}
