/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagers;

/**
 *
 * @author harvey
 */
public class Constants {

    public enum STATUS {
        ON, OFF, REMOVED
    }
    
    public enum TYPE{
        HUMIDITY,
        TEMPERATURE,
        PRESSURE,
        WIND_SPEED
    }
    
    public enum UNIT{
        GRAM_PER_CUBIC_METRE,
        CELSIUS,
        PASCALS,
        METRES_PER_SECOND,
    }
}
