package ui.Events;

/**
 * Event to handle the creation of a new line
 *
 * @author Kareem Horstink
 */
public class Gui_Events_Move extends Gui_Events {

    private int pointID;
    private double[] info;
    private int curveID;

    public Gui_Events_Move(Object source, double[] info, int pointID, int curveID) {
        super(source);
        this.info = info;
        this.curveID = curveID;
        this.pointID = pointID;
    }

    public int getPointID() {
        return pointID;
    }

    public double[] getInfo() {
        return info;
    }

    public int getCurveID() {
        return curveID;
    }

}
