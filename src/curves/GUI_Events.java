package curves;

/**
 *
 * @author Kareem Horstink
 */
public class GUI_Events extends java.util.EventObject {

    private int command = -1;
    //0 = create new line
    //1 = create new point
    //2 = modify point
    //3 = delete point
    //4 = close line
    //5 = open line

    private int curveID;

    public int getCurveID() {
        return curveID;
    }
    private Object[] info;

    public GUI_Events(Object source) {
        super(source);
    }

    public GUI_Events(Object source, int curveID, int command) {
        super(source);
        this.command = command;
        this.curveID = curveID;
    }

    public GUI_Events(Object source, int curveID, int command, Object[] info) {
        super(source);
        this.command = command;
        this.info = info;
        this.curveID = curveID;
    }

    public int getCommand() {
        return command;
    }

    public Object[] getInfo() {
        return info;
    }

}
