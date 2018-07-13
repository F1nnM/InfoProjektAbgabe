package datatypes;

import util.Utilities;

/**
 * A class for messages
 */
public class NXTMessage {

	public static final NXTMessage ok = new NXTMessage(1);
	public static final NXTMessage done = new NXTMessage(2);
	public static final NXTMessage clearLCD = new NXTMessage(3);
	public static final NXTMessage flt = new NXTMessage(4);
	public static final NXTMessage isMoving = new NXTMessage(5);
	public static final NXTMessage buttonPressed = new NXTMessage(6);
	public static final NXTMessage getTachoCount = new NXTMessage(7);
	public static final NXTMessage resetTachoCount = new NXTMessage(8);
	public static final NXTMessage drawLCD = new NXTMessage(9);
	public static final NXTMessage setSpeed = new NXTMessage(19);
	public static final NXTMessage rotate = new NXTMessage(11);
	public static final NXTMessage waitTillFinished = new NXTMessage(12);
	public static final NXTMessage runSyncTask = new NXTMessage(13);
	public static final NXTMessage continuous = new NXTMessage(14);
	public static final NXTMessage oneCentimetreTravelled = new NXTMessage(15);
	public static final NXTMessage stop = new NXTMessage(16);
	public static final NXTMessage rotateTo = new NXTMessage(17);
	public static final NXTMessage forward = new NXTMessage(18);

	private int message;
	private int[] values;

	/**
	 * the Constructor of NXTMessage
	 *
	 * @param message
	 *            the containing message
	 */
	private NXTMessage(int message) {
		this.message = message;
		this.values = null;
	}

	/**
	 * the Constructor of NXTMessage
	 * 
	 * @param message
	 *            the containing message
	 * @param values
	 *            the value of the message
	 */
	private NXTMessage(int message, int... values) {
		this.message = message;
		this.values = values;
	}

	public static NXTMessage oneCentimetreTravelled(int motor) {
		return new NXTMessage(15, motor);
	}

	/**
	 * converts a Integer to a NXTMessage
	 *
	 * @param in
	 *            the String to convert
	 * @return the NXTMessage
	 */
	public static NXTMessage toNxtMessage(Long in) {
		Boolean negative = false;
        if (in < 0)
            negative = true;
        in = Math.abs(in);
        String[] arr = Utilities.split(String.valueOf(in), "0");
        int[] ints = new int[arr.length - ((arr.length > 2)? 2: 1)];

        int zero = Integer.parseInt(arr[arr.length - 1]);

        for (int i = 0; i < arr.length - 2; i++) {
            ints[i] = Integer.parseInt(Utilities.replaceAll(arr[i + 1], String.valueOf(zero), "0"));
        }

        if (negative)
            ints[0] = -ints[0];
        return new NXTMessage(Integer.parseInt(arr[0]), ints);
	}

	/**
	 * this method says, if a button is pressed
	 * 
	 * @param button
	 *            the name of the pressed button
	 * @return the NXTMessage
	 */
	public static NXTMessage buttonPressed(int button) {
		return new NXTMessage(6, button);
	}

	/**
	 * this method sends a distance of a UltrasonicSensor
	 * 
	 * @param distance
	 *            the distance to an object
	 * @param sensorPort
	 *            the port of the sensor
	 * @return the NXTMessage
	 */
	public static NXTMessage ultraSonicSensorData(int distance, int sensorPort) {
		return new NXTMessage(19, distance, sensorPort);
	}

	/**
	 * this method returns a NXTMessage to send the TachoCount
	 * 
	 * @param motor
	 *            the motor
	 * @param TachoCount
	 *            the TachoCount of the motor
	 * @return a NXTMessage
	 */
	public static NXTMessage getTachoCount(int motor, int TachoCount) {
		return new NXTMessage(7, motor, TachoCount);
	}

	/**
	 * this method returns a NXTMessage to send, if a motor is moving
	 * 
	 * @param motor
	 *            the motor
	 * @param isMoving
	 *            a Boolean, if the motor is moving
	 * @return a NXTMessage
	 */
	public static NXTMessage isMoving(int motor, Boolean isMoving) {
		return new NXTMessage(5, motor, ((isMoving) ? 1 : 0));
	}

	/**
	 * @return the message of the NXTMessage
	 */
	public int getMessage() {
		return message;
	}

	/**
	 * @return the value of the message; returns -1 if no value is given
	 */
	public int[] getValues() {
		return values;
	}

	/**
	 * converts the NXTMessage to a Integer
	 *
	 * @return the resulting String
	 */
	public Long toLong() {
		if (values != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(message).append(0);
			Boolean negative = false;
			int zero = getZero(22, values);
			for (int s : values) {
				if (s < 0)
					negative = true;
				builder.append(Utilities.replaceAll(String.valueOf(Math.abs(s)), "0", String.valueOf(zero))).append(0);
			}
			builder.append(zero);
			return Long.parseLong(((negative) ? "-" : "") + builder.toString());
		}
		return (long) message;
	}

	private int getZero(int startZero, int... values) {
		for (int i : values) {
			if (Utilities.contains(String.valueOf(i), String.valueOf(startZero))) {
				return getZero(++startZero, values);
			}
		}
		return startZero;
	}

	/**
	 * checks if a NXTMessage equals another
	 * 
	 * @param nxtMessage
	 *            the NXTMessage to compare to
	 * @return a boolean
	 */
	public boolean equals(NXTMessage nxtMessage) {
		return this.message == nxtMessage.getMessage();
	}
}
