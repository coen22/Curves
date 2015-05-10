package ui.events;

/**
 * An event to show that the algorithm to calculate the Area should be changed 
 *
 * @author Kareem Horstink
 * @version 1.0
 */
public class GuiEventsAreaChange extends GuiEvents {
    private int algorithm;

    public GuiEventsAreaChange(Object source, int algorithm) {
        super(source);
        this.algorithm = algorithm;
    }

    public int getAlgorithm() {
        return algorithm;
    }
}
