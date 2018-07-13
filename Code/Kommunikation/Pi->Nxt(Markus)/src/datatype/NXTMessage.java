package datatype;

import java.util.ArrayList;
import java.util.StringTokenizer;

/*
 * A class for messages
 */
public class NXTMessage {

	// Definition of all NXTMessages
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
	public static final NXTMessage ultraSonicSensorData = new NXTMessage(19);

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
		String[] arr = String.valueOf(in).split("0");
		int[] ints = new int[arr.length - 2];

		int zero = Integer.parseInt(arr[arr.length - 1]);

		for (int i = 0; i < arr.length - 2; i++) {
			ints[i] = Integer.parseInt(arr[i + 1].replaceAll(String.valueOf(zero), "0"));
		}
		if (negative)
			ints[0] = -ints[0];
		return new NXTMessage(Integer.parseInt(arr[0]), ints);
	}

	/**
	 * sends a NXTMessage to rotate a motor to a specific angle
	 *
	 * @param angle
	 *            the angle to rotate to
	 * @param motor
	 *            the motor to rotate
	 * @return the NXTMessage
	 */
	public static NXTMessage rotateTo(int angle, NXTMotor motor) {
		return new NXTMessage(17, angle, motor.getNumber());
	}

	public static NXTMessage forward(NXTMotor motor) {
		return new NXTMessage(18, motor.getNumber());
	}

	/**
	 * requests to send a message every travelled centimetre
	 *
	 * @param motor
	 *            the motor to listen
	 * @return the NXTMessage
	 */
	public static NXTMessage oneCentimetreTravelled(NXTMotor motor) {
		return new NXTMessage(15, motor.getNumber());
	}

	/**
	 * this method splits a String by a delimiter it works pretty much like the
	 * String.split() in native Java.
	 *
	 * @param toSplit
	 *            the String
	 * @param delimiter
	 *            the delimiter
	 * @return an Array, containing the split String
	 */
	public static String[] split(String toSplit, String delimiter) {
		ArrayList<String> elements = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(toSplit, delimiter);
		if (stringTokenizer.countTokens() > 0) {
			while (stringTokenizer.hasMoreElements()) {
				elements.add(stringTokenizer.nextToken());
			}
		}
		return elements.toArray(new String[0]);
	}

	/**
	 * this method returns a NXTMessage to run a synchronised Task
	 *
	 * @param time
	 *            the time to run the Task at
	 * @return the NXTMessage
	 */
	public static NXTMessage runSyncTask(long time) {
		return new NXTMessage(13, Math.toIntExact(time));
	}

	/**
	 * set a Ultrasonic Sensor in continuous mode
	 *
	 * @param sensor
	 *            the sensor
	 * @return the NXTMessage
	 */
	public static NXTMessage continuous(UltrasonicSensor sensor) {
		return new NXTMessage(14, sensor.getSensorPort().getName());
	}

	/**
	 * this method returns a NXTMessage to listen for Button press
	 *
	 * @param button
	 *            the Button to listen for
	 * @return the NXTMessage
	 */
	public static NXTMessage buttonPressed(NXTButton button) {
		return new NXTMessage(6, button.getName());
	}

	/**
	 * this method returns a NXTMessage to get the TachoCount of a motor
	 *
	 * @param motor
	 *            the motor to get the TachoCount from
	 * @return the NXTMessage
	 */
	public static NXTMessage getTachoCount(NXTMotor motor) {
		return new NXTMessage(7, motor.getNumber());
	}

	/**
	 * this method returns a NXTMessage to reset the TachoCount of a motor
	 *
	 * @param motor
	 *            the motor to reset the TachoCount for
	 * @return the NXTMessage
	 */
	public static NXTMessage resetTachoCount(NXTMotor motor) {
		return new NXTMessage(8, motor.getNumber());
	}

	/**
	 * this method returns a NXTMessage to set the speed of a motor
	 *
	 * @param speed
	 *            the speed to set
	 * @param motor
	 *            the motor to set the speed for
	 * @return the NXTMessage
	 */
	public static NXTMessage setSpeed(int speed, NXTMotor motor) {
		return new NXTMessage(19, speed, motor.getNumber());
	}

	/**
	 * this method returns a NXTMessage to rotate a motor to a specific angle
	 *
	 * @param angle
	 *            the angle to rotate to
	 * @param motor
	 *            the motor to rotate
	 * @return the NXTMessage
	 */
	public static NXTMessage rotate(int angle, NXTMotor motor) {
		return new NXTMessage(11, angle, motor.getNumber());
	}

	/**
	 * this method returns a NXTMessage to set a motor into float mode
	 *
	 * @param motor
	 *            the motor to set into float mode
	 * @return the NXTMessage
	 */
	public static NXTMessage flt(NXTMotor motor) {
		return new NXTMessage(4, motor.getNumber());
	}

	/**
	 * this method returns a NXTMessage to check, if a motor is Moving
	 *
	 * @param motor
	 *            the motor to check
	 * @return the NXTMessage
	 */
	public static NXTMessage isMoving(NXTMotor motor) {
		return new NXTMessage(5, motor.getNumber());
	}

	public static NXTMessage stop(NXTMotor motor) {
		return new NXTMessage(16, motor.getNumber());
	}

	/**
	 * @return the message of the NXTMessage
	 */
	public int getMessage() {
		return message;
	}

	/**
	 * @return the values of the message; returns null if no value is given
	 */
	public int[] getValues() {
		return values;
	}

	/**
	 * converts the NXTMessage to a String
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
				builder.append(String.valueOf(Math.abs(s)).replaceAll("0", String.valueOf(zero))).append(0);
			}
			builder.append(zero);
			return Long.parseLong(((negative) ? "-" : "") + builder.toString());
		}
		return (long) message;
	}

	private int getZero(int startZero, int... values) {
		for (int i : values) {
			if (String.valueOf(i).contains(String.valueOf(startZero))) {
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
