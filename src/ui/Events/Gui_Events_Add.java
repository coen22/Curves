package ui.Events;

/**
 * Event to handle the creation of a new line
 *
 * @author Kareem Horstink
 */
public class Gui_Events_Add extends Gui_Events {

    private double[] info;
    private int curveID;

    public Gui_Events_Add(Object source, double[] info, int curveID) {
        super(source);
        this.info = info;
        this.curveID = curveID;
    }

    public double[] getInfo() {
        return info;
    }

    public int getCurveID() {
        return curveID;
    }

}
