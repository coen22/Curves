package ui.Events;

/**
 * Event to handle the creation of a new line
 *
 * @author Kareem Horstink
 */
public class Gui_Events_Create extends Gui_Events {

    private double[] info;
    private String name;

    public Gui_Events_Create(Object source, double[] info, String name) {
        super(source);
        this.info = info;
        this.name = name;
    }

    public double[] getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

}
