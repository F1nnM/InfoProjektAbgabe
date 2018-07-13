package datatypes;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import util.Clock;
import util.Connection;

/**
 * A class for Ultrasonic Sensors
 */
public class UltrasonicSensors {

	private static UltrasonicSensor s1 = null;
	private static UltrasonicSensor s2 = null;
	private static UltrasonicSensor s3 = null;
	private static UltrasonicSensor s4 = null;

	/**
	 * set the sensor in continuous mode
	 * 
	 * @param sensorPort
	 *            the port of the Sensor
	 */
	public static void continuous(int sensorPort) {
		UltrasonicSensor s = getUltraSonicSensor(sensorPort, true);
		s.continuous();

		Clock.addTimer(new TimedListener() {
			
			@Override
			public void TimeUp() {
				Connection.send(NXTMessage.ultraSonicSensorData(s.getDistance(), sensorPort));
			}
		});
	}

	/**
	 * Initialise the Sensor
	 * 
	 * @param sensorPort
	 *            the port of the Sensor
	 */
	private static void init(int sensorPort) {
		if (getUltraSonicSensor(sensorPort, false) == null) {
			switch (sensorPort) {
			case 1:
				s1 = new UltrasonicSensor(SensorPort.S1);
			case 2:
				s2 = new UltrasonicSensor(SensorPort.S2);
			case 3:
				s3 = new UltrasonicSensor(SensorPort.S3);
			case 4:
				s4 = new UltrasonicSensor(SensorPort.S4);
			}
		}
	}

	/**
	 * get the UltrasonicSensor by the name of the port
	 * 
	 * @param sensorPort
	 *            the port of the Sensor
	 * @param init
	 *            if the sensor should be initialised
	 * @return the UltrasonicSensor
	 */
	private static UltrasonicSensor getUltraSonicSensor(int sensorPort, Boolean init) {
		if (init) {
			init(sensorPort);
		}
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
