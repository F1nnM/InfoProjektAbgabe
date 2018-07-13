package datatype;

import nxt.NXT;

/**
 * A class for Ultrasonic Sensors
 */
public class UltrasonicSensors {

    private UltrasonicSensor s1 = null;
    private UltrasonicSensor s2 = null;
    private UltrasonicSensor s3 = null;
    private UltrasonicSensor s4 = null;

    private NXT nxt;

    /**
     * the constructor of UltrasonicSensors
     *
     * @param nxt a instance of the class NXT
     */
    public UltrasonicSensors(NXT nxt) {
        this.nxt = nxt;
    }

    /**
     * this method returns an Array containing all UltrasonicSensors; Not
     * initialised sensors might be null.
     *
     * @return the Array
     */
    public UltrasonicSensor[] toArray() {
        UltrasonicSensor[] sensors = {s1, s2, s3, s4};
        return sensors;
    }

    /**
     * this method initialises the Sensor
     *
     * @param sensorPort the Sensor Port of the Sensor
     */
    private void init(int sensorPort) {
        if (getUltrasonicSensor(sensorPort) == null) {
            switch (sensorPort) {
                case 1:
                    s1 = new UltrasonicSensor(SensorPort.S1, nxt);
                case 2:
                    s2 = new UltrasonicSensor(SensorPort.S2, nxt);
                case 3:
                    s3 = new UltrasonicSensor(SensorPort.S3, nxt);
                case 4:
                    s4 = new UltrasonicSensor(SensorPort.S4, nxt);
            }
        }
    }

    /**
     * this method returns an UltrasonicSensor with a SensorPort
     *
     * @param sensorPort the port of the sensor
     * @return the UltrasonicSensor
     */
    public UltrasonicSensor getUltrasonicSensor(SensorPort sensorPort) {
        init(sensorPort.getName());
        switch (sensorPort.getName()) {
            case 1:
                return s1;
            case 2:
                return s2;
            case 3:
                return s3;
            case 4:
                return s4;
            default:
                return null;
        }
    }

    /**
     * this method returns an UltrasonicSensor with a string containing the name of
     * the SensorPort
     *
     * @param sensorPort the name of the SensorPort
     * @return the UltrasonicSensor
     */
    public UltrasonicSensor getUltrasonicSensor(int sensorPort) {
        switch (sensorPort) {
            case 1:
                return s1;
            case 2:
                return s2;
            case 3:
                return s3;
            case 4:
                return s4;
            default:
                return null;
        }
    }
}
