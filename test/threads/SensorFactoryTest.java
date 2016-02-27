/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import entities.Sensor;
import entitymanagers.Constants.STATUS;
import entitymanagers.Constants.TYPE;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author harvey
 */
public class SensorFactoryTest {
    private static SensorFactory instance;
    private static Sensor sensor;
    private static boolean expResult;
    private static ExecutorService executorService;
    
    
    @BeforeClass
    public static void setUpClass() {
        instance = new SensorFactory();
        sensor = new Sensor("PressSensor", TYPE.PRESSURE.toString(), STATUS.ON.toString());
        expResult = true;
        executorService = Executors.newCachedThreadPool();
    }
    
    @AfterClass
    public static void tearDownClass() {
        executorService.shutdown();
    }

    /**
     * Test of createSensorThread method, of class SensorFactory.
     * @throws java.lang.InterruptedException
     */
    @Test@Ignore
    public void testCreateSensorThread() throws InterruptedException {
        System.out.println("createSensorThread");
        SensorThread result = instance.createSensorThread(sensor);
        executorService.execute(result);
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        assertNotNull(result);
    }

    /**
     * Test of interruptThread method, of class SensorFactory.
     */
    @Test
    public void testInterruptThread() throws InterruptedException {
        System.out.println("interruptThread");
        SensorThread sensorThread = instance.createSensorThread(sensor);
        executorService.execute(sensorThread);
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        boolean result = instance.interruptThread(sensor);
        assertEquals(expResult, result);
    }   
}
