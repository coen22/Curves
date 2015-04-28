package ui.Events;

/**
 * Event to handle the creation of a new line
 *
 * @author Kareem Horstink
 */
public class GuiEventsDeleteC extends GuiEvents {

    private int curveID;

    public GuiEventsDeleteC(Object source, int curveID) {
        super(source);
        this.curveID = curveID;
    }


    public int getCurveID() {
        return curveID;
    }

}
