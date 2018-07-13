package datatype;

import nxt.NXT;

public class Buttons {

    // Definition of the Buttons
    public final NXTButton ENTER;
    public final NXTButton LEFT;
    public final NXTButton RIGHT;
    public final NXTButton ESCAPE;

    /**
     * the Constructor of Buttons, which creates all Buttons
     */
    public Buttons(NXT nxt) {
        ENTER = new NXTButton(1, nxt);
        LEFT = new NXTButton(2, nxt);
        RIGHT = new NXTButton(3, nxt);
        ESCAPE = new NXTButton(4, nxt);
    }

    /**
     * this method converts a name of a button to a button
     *
     * @param name the name
     * @return the Button
     */
    public NXTButton getButton(int name) {
        switch (name) {
            case 1:
                return ENTER;
            case 2:
                return LEFT;
            case 3:
                return RIGHT;
            case 4:
                return ESCAPE;
            default:
                return null;
        }
    }
}
