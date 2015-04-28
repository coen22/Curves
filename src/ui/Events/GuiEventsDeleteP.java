package ui.Events;

/**
 * Event to handle the creation of a new line
 *
 * @author Kareem Horstink
 */
public class GuiEventsDeleteP extends GuiEvents{

    private int pointID;
    private int curveID;

    public GuiEventsDeleteP(Object source, int pointID, int curveID) {
        super(source);
        this.curveID = curveID;
        this.pointID = pointID;
    }

    public int getPointID() {
        return pointID;
    }

    public int getCurveID() {
        return curveID;
    }

}
