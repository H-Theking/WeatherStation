/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagers;

import entities.Sensor;
import entities.Location;
import constants.StatusType.Status;
import constants.StatusType.Type;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author harvey
 */
public class SensorManager {

    private final EntityManager manager;

    public SensorManager(EntityManager manager) {
        this.manager = manager;
    }

    public List<Sensor> getSensors(){
        return manager.createNamedQuery("Sensor.findAll").getResultList();
    }

    public Sensor createSensor(String name, String type, String status, float lon, float lat) {
        manager.getTransaction().begin();
        Sensor sensor = new Sensor(name, type, status);
        manager.persist(sensor);
        manager.flush();
        Location location = new Location(sensor.getId(), lon, lat);
        sensor.setLocation(location);
        manager.persist(location);
        manager.getTransaction().commit();
        return sensor;
    }
    
    public Sensor createSensor(Sensor sensor){
        return createSensor(sensor.getName(), sensor.getType(), sensor.getStatus(),
                sensor.getLocation().getLongitude(), sensor.getLocation().getLatitude());
    }

    public Sensor updateSensor(Sensor sensor) {
        manager.getTransaction().begin();
        
//        sensor.setName(sensor.getName());
        manager.merge(sensor);
//        manager.flush();
        manager.getTransaction().commit();
        return sensor;
    }

    public void changeSensorLocation(int sensorId, float lon, float lat) {
        manager.getTransaction().begin();
        Location location = manager.find(Location.class, sensorId);
        location.setLatitude(lat);
        location.setLongitude(lon);
        manager.merge(location);
        manager.flush();
        manager.getTransaction().commit();
    }

    public void switchSensorStatus(Sensor sensor) {
//        manager.getTransaction().begin();
        if (sensor.getStatus().equals(Status.ON.toString())) {
            sensor.setStatus(Status.OFF.toString());
        } else {
            sensor.setStatus(Status.ON.toString());
        }
//        manager.merge(sensor);

        manager.persist(sensor);
//        manager.flush();
//        manager.getTransaction().commit();
    }

    public void removeSensor(String name) {
        manager.getTransaction().begin();
        Sensor sensor = findByName(name);
//        manager.refresh(sensor);
        manager.remove(sensor);
        manager.getTransaction().commit();
    }
    
    public List<Sensor> findByType(Type type){
        return manager.createNamedQuery("Sensor.findByType").
                setParameter("type", type.toString()).getResultList();
    }

    public Sensor findByName(String name) {
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
