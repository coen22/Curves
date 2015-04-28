package ui.Events;

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
