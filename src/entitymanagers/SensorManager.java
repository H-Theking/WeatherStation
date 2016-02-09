/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagers;

import entities.Location;
import entities.Sensor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 *
 * @author harvey
 */
public class SensorManager {

    private final EntityManagerFactory emf;
    private final EntityManager manager;

    public SensorManager() {
        this.emf = Persistence.createEntityManagerFactory("WeatherStationPU");
        this.manager = emf.createEntityManager();
    }

    public void createSensor(String name, String type, String status, float lon, float lat) {
        manager.getTransaction().begin();
        Sensor sensor = new Sensor(name, type, status);
        manager.persist(sensor);
        manager.flush();
        Location location = new Location(sensor.getId(), lon, lat);
        sensor.setLocation(location);
        manager.persist(location);
        manager.getTransaction().commit();
    }

    public void editSensorName(String name, String newName) {
        manager.getTransaction().begin();
        Sensor sensor = findSensorByName(name);
        sensor.setName(newName);
        manager.merge(sensor);
        manager.getTransaction().commit();
    }

    public void changeSensorLocation(int sensorId, float lon, float lat) {
        manager.getTransaction().begin();
        Location location = manager.find(Location.class, sensorId);
        location.setLatitude(lat);
        location.setLongitude(lon);
        manager.merge(location);
        manager.getTransaction().commit();
    }

    public void switchSensorStatus(String name) {
        manager.getTransaction().begin();
        Sensor sensor = findSensorByName(name);
        if (sensor.getStatus().equals("ON")) {
            sensor.setStatus("OFF");
        } else {
            sensor.setStatus("ON");
        }
        manager.merge(sensor);
        manager.getTransaction().commit();
    }

    public void removeSensor(String name) {
        manager.getTransaction().begin();
        Sensor sensor = findSensorByName(name);
//        manager.refresh(sensor);
        manager.remove(sensor);
        manager.getTransaction().commit();
    }

    public Sensor findSensorByName(String name) {
        try {
            return (Sensor) manager.createNamedQuery("Sensor.findByName").setParameter("name", name).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public EntityManager getManager() {
        return manager;
    }
}
