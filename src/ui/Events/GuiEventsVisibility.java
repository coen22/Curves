package ui.events;

/**
 * Event to handle the change in if the line is visible
 *
 * @author Kareem Horstink
 */
public class GuiEventsVisibility extends GuiEvents {

    public GuiEventsVisibility(Object source, boolean visiblity) {
        super(source);
        this.visiblity = visiblity;
    }

    private boolean visiblity;

    public boolean getVisiablity() {
        return visiblity;
    }

}
