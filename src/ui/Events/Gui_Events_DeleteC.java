package ui.Events;

/**
 * Event to handle the creation of a new line
 *
 * @author Kareem Horstink
 */
public class Gui_Events_DeleteC extends Gui_Events {

    private int curveID;

    public Gui_Events_DeleteC(Object source, int curveID) {
        super(source);
        this.curveID = curveID;
    }


    public int getCurveID() {
        return curveID;
    }

}
