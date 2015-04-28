package ui.Events;

/**
 * The general GUI Event
 *
 * @author Kareem Horstink
 * @version 1.0
 */
public abstract class GuiEvents extends java.util.EventObject {

    public GuiEvents(Object source) {
        super(source);
    }   
}
