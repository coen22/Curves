package ui.events;

/**
 *
 * @author Kareem
 */
public class GuiEventZoom extends GuiEvents{
    private double zoom;
    public GuiEventZoom(Object source, double zoom) {
        super(source);
        this.zoom = zoom;
    }
    
}
