package curves;

/**
 *
 * @author Kareem Horstink
 */
public class GUI_Events extends java.util.EventObject {

    private int command;
    private int[] info;

    public GUI_Events(Object source) {
        super(source);
    }

    public GUI_Events(Object source, int command) {
        super(source);
        this.command = command;
    }

    public GUI_Events(Object source, int command, int[] info) {
        super(source);
        this.command = command;
        this.info = info;
    }

    public int getCommand() {
        return command;
    }

    public int[] getInfo() {
        return info;
    }

}
