package ui.events;

/**
 * Event to handle the creation of a new line
 *
 * @author Kareem Horstink
 * @version 1.0
 */
public class GuiEventsCreate extends GuiEvents {

    private double[] info;
    private String name;

    /**
     *
     * @param source
     * @param info x-coordinate; y-coordinate; line type; (if applicable) cubic type
     * @param name
     */
    public GuiEventsCreate(Object source, double[] info, String name) {
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
