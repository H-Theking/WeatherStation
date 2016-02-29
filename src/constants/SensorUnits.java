/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

import constants.StatusType.Type;
import entities.Sensor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author harvey
 */
public class SensorUnits {
//	1 m/s = 3,6 km/h = 1,944 knots = 2,237 miles per hour

    public enum HumidityUnits {

        PRECENTAGE
    }

    public enum TemperatureUnits {

        CELSIUS,
        FAHRENHEIT
    }

    public enum PressureUnits {

        MILIBARS
    }

    public enum WindSpeedUnits {

        METRES_PER_SECOND,
        MILES_PER_HOUR,
        KILOMETRES_PER_HOUR,

    }

    public List<String> getUnit(Sensor sensor) {
        List<String> units = new ArrayList<>();
        switch (Type.valueOf(sensor.getType())) {
            case HUMIDITY:
                for (HumidityUnits value : HumidityUnits.values()) {
                    units.add(value.toString());
                }
            case PRESSURE:
                for (PressureUnits value : PressureUnits.values()) {
                    units.add(value.toString());
                }
                break;
            case TEMPERATURE:
                for (TemperatureUnits value : TemperatureUnits.values()) {
                    units.add(value.toString());
                }
                break;
            default:
                for (WindSpeedUnits value : WindSpeedUnits.values()) {
                    units.add(value.toString());
                }
                break;
        }
        return units;
    }

    public float convertTo(String unit, float value) {
        switch (unit) {
            case "FAHRENHEIT":
                return celciusToFah(value);
            case "MILES_PER_HOUR":
                return mpsToMph(value);
            default:
                return mpsToKh(value);
        }
    }

    private float celciusToFah(float value) {
        return value * 9 / 5 + 32;
    }

    private float mpsToMph(float value) {
        return value * (float) 2.237;
    }

    private float mpsToKh(float value) {
        return value * (float) 3.6;
    }
}
