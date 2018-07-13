package datatype;

/**
 * A class for sensor ports
 */
public class SensorPort {

    public static final SensorPort S1 = new SensorPort(1);
    public static final SensorPort S2 = new SensorPort(2);
    public static final SensorPort S3 = new SensorPort(3);
    public static final SensorPort S4 = new SensorPort(4);

    private int name;

    /**
     * the constructor of SensorPort
     *
     * @param name the name of the port
     */
    private SensorPort(int name) {
        this.name = name;
    }

    /**
     * returns the name of the port
     *
     * @return the name of the port
     */
    public int getName() {
        return name;
    }
}
