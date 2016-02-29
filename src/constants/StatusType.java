/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

/**
 *
 * @author harvey
 */
public class StatusType {

    public enum Status {
        ON, OFF, REMOVED
    }
    
    public enum Type{
        HUMIDITY,
        TEMPERATURE,
        PRESSURE,
        WIND_SPEED
    }
}
