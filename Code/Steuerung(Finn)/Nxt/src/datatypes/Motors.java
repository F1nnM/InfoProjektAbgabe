package datatypes;

import java.util.Timer;
import java.util.TimerTask;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import util.Connection;

/**
 * a class for motors
 */
public class Motors {

	/**
	 * this method stops a motor
	 * 
	 * @param motor
	 *            the name of the motor to stop
	 */
	public static void stop(int motor) {
		getMotor(motor).stop();
	}

	/**
	 * send the command forward to a motor
	 * 
	 * @param motor
	 *            the name of the motor
	 */
	public static void forward(int motor) {
		getMotor(motor).forward();
	}

	/**
	 * this method returns the TachoCount of a motor
	 * 
	 * @param motor
	 *            the name of the motor
	 */
	public static void getTachoCount(int motor) {
		Connection.send(NXTMessage.getTachoCount(motor, getMotor(motor).getTachoCount()));
	}

	/**
	 * this method resets the TachoCount of a motor
	 * 
	 * @param motor
	 *            the name of the motor
	 */
	public static void resetTachoCount(int motor) {
		getMotor(motor).resetTachoCount();
	}

	/**
	 * this method sets the speed of a motor
	 * 
	 * @param speed
	 *            the speed
	 * @param motor
	 *            the name of the motor
	 */
	public static void setSpeed(int speed, int motor) {
		getMotor(motor).setSpeed(speed);
	}

	/**
	 * send a message every travelled one centimetre
	 * 
	 * @param motor
	 *            the motor to listen for
	 */
	public static void oneCentimentreTravelled(int motor) {
		NXTRegulatedMotor m = getMotor(motor);
		Double[] dist = { 0.0 };

		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				if (m.getRotationSpeed() != 0) {
					dist[0] += (m.getRotationSpeed() * 1.88495559215) * 0.1;
					if (dist[0] >= 1.0) {
						dist[0] -= 1.0;
						Connection.send(NXTMessage.oneCentimetreTravelled(motor));
					}
				}

				if (!Connection.connected) {
					t.cancel();
				}
			}
		}, 0, 100);
	}

	/**
	 * this method rotates a motor
	 * 
	 * @param angle
	 *            the angle to rotate to
	 * @param motor
	 *            the name of the motor
	 */
	public static void rotate(int angle, int motor) {
		getMotor(motor).rotate(angle, true);
	}

	/**
	 * this method sets a motor into float mode
	 * 
	 * @param motor
	 *            the name of the motor
	 */
	public static void flt(int motor) {
		getMotor(motor).flt();
	}

	/**
	 * rotates the motor to a specific angle
	 * 
	 * @param angle
	 *            the angle to rotate to
	 * @param motor
	 *            the motor to rotate
	 */
	public static void rotateTo(int angle, int motor) {
		getMotor(motor).rotateTo(angle);
	}

	/**
	 * this method returns, if the motor is moving
	 * 
	 * @param motor
	 *            the name of the motor
	 */
	public static void isMoving(int motor) {
		Connection.send(NXTMessage.isMoving(motor, getMotor(motor).isMoving()));
	}

	/**
	 * this method converts a name of a motor to a motor
	 * 
	 * @param motorName
	 *            the name of the motor
	 * @return the motor
	 */
	private static NXTRegulatedMotor getMotor(int motorName) {
		switch (motorName) {
		case 1:
			return Motor.A;
		case 2:
			return Motor.B;
		case 3:
			return Motor.C;
		default:
			return null;
		}
	}
}
