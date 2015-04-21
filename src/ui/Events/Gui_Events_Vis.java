package ui.Events;

/**
 * Event to handle the change in if the line is visible
 *
 * @author Kareem Horstink
 */
public class Gui_Events_Vis extends Gui_Events {

    public Gui_Events_Vis(Object source, boolean visiblity) {
        super(source);
        this.visiblity = visiblity;
    }

    private boolean visiblity;

    public boolean getVisiablity() {
        return visiblity;
    }

}
