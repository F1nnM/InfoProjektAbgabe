package datatype;

import listener.OneCentimetreTravelledListener;
import nxt.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for the motors of the NXT. For further information on how the motors
 * works, visit the leJos NXJ documentation.
 */
public class NXTMotor {

    private int number;
    private Connection connection;
    private ArrayList<OneCentimetreTravelledListener> listeners;

    /**
     * the Constructor of NXTMotor
     *
     * @param number     the number of the motor; e.g. "A", "B" or "C"
     * @param connection an Instance of the Object Connection
     */
    protected NXTMotor(int number, Connection connection) {
        this.connection = connection;
        this.number = number;
        listeners = new ArrayList<>();
    }

    /**
     * add a listener to listen for every travelled centimetre
     *
     * @param listener the listener to add
     */
    public void addOneCentimetreTravelledListener(OneCentimetreTravelledListener listener) {
        if (listeners.isEmpty()) {
            connection.enqueue(NXTMessage.oneCentimetreTravelled(this));
        }
        listeners.add(listener);
    }

    /**
     * rotate the motor to a specific angle
     *
     * @param angle the angle to rotate to
     */
    public void rotateTo(int angle) {
        connection.enqueue(NXTMessage.rotateTo(angle, this));
    }

    /**
     * notify all listeners
     */
    public void oneCentimetreTravelled() {
        for (OneCentimetreTravelledListener l : listeners) {
            l.travelledOneCentimetre();
        }
    }

    /**
     * @return the number of the motor
     */
    public int getNumber() {
        return number;
    }

    /**
     * tell the motor to start moving
     */
    public void forward() {
        connection.enqueue(NXTMessage.forward(this));
    }

    /**
     * this method sets the speed of the motor
     *
     * @param speed the speed of the motor
     */
    public void setSpeed(int speed) {
        connection.enqueue(NXTMessage.setSpeed(speed, this));
    }

    /**
     * this method stops the motor
     */
    public void stop() {
        connection.enqueue(NXTMessage.stop(this));
    }

    /**
     * this method returns the TachoCount of the motor
     *
     * @return the TachoCount
     */
    public int getTachoCount() {
        final List<Integer> answer = new ArrayList<>();
        connection.addRequest(NXTMessage.getTachoCount(this), answer::add);

        while (answer.isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return answer.get(0);
    }

    /**
     * this method resets the TachoCount of the motor
     */
    public void resetTachoCount() {
        connection.enqueue(NXTMessage.resetTachoCount(this));
    }

    /**
     * this method rotates the motor to a specific angle
     *
     * @param angle the angle to rotate to
     */
    public void rotate(int angle) {
        connection.enqueue(NXTMessage.rotate(angle, this));
    }

    /**
     * this method runs 'flt' on the NXT for the motor
     */
    public void flt() {
        connection.enqueue(NXTMessage.flt(this));
    }

    /**
     * this method checks, if the motor is moving
     *
     * @return a boolean indicating if the motor is moving
     */
    public boolean isMoving() {
        final List<Boolean> answer = new ArrayList<>();
        connection.addRequest(NXTMessage.isMoving(this), reply -> {
            if (reply == 1) {
                answer.add(true);
            } else if (reply == 0) {
                answer.add(false);
            }
        });

        while (answer.isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return answer.get(0);
    }
}
