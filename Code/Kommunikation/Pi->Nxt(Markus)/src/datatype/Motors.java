package datatype;

import nxt.Connection;
import nxt.NXT;

public class Motors {

    public final NXTMotor A;
    public final NXTMotor B;
    public final NXTMotor C;
    private Connection connection;

    /**
     * the constructor of Motors, which creates all single Motors
     *
     * @param nxt an Instance of the NXT class, which should be connected to a NXT
     *            Brick
     */
    public Motors(NXT nxt) {
        connection = nxt.getConnection();
        A = new NXTMotor(1, connection);
        B = new NXTMotor(2, connection);
        C = new NXTMotor(3, connection);
    }

    /**
     * get a NXTMotor from it's name
     *
     * @param name the name of the motor
     * @return the NXTMotor
     */
    public NXTMotor getMotor(int name) {
        switch (name) {
            case 1:
                return A;
            case 2:
                return B;
            case 3:
                return C;
            default:
                return null;
        }
    }

}
