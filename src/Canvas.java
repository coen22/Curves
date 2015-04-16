/*
 * Place to draw the curves
 */
package curves;

import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Kareem
 */
public class Canvas extends JPanel {

    private double offSetX = 0;
    private double offSetY = 0;
    private double zoom = 0;
    private ArrayList<Curves> curves = new ArrayList<>();

    /**
     * Get the value of curves
     *
     * @return the value of curves
     */
    public ArrayList<Curves> getCurves() {
        return curves;
    }

    /**
     * Set the value of curves
     *
     * @param curves new value of curves
     */
    public void setCurves(ArrayList<Curves> curves) {
        this.curves = curves;
    }

    /**
     * Get the value of offSetX
     *
     * @return the value of offSetX
     */
    public double getOffSetX() {
        return offSetX;
    }

    /**
     * Set the value of offSetX
     *
     * @param offSetX new value of offSetX
     */
    public void setOffSetX(double offSetX) {
        this.offSetX = offSetX;
    }

    /**
     * Get the value of offSetY
     *
     * @return the value of offSetY
     */
    public double getOffSetY() {
        return offSetY;
    }

    /**
     * Set the value of offSetY
     *
     * @param offSetY new value of offSetY
     */
    public void setOffSetY(double offSetY) {
        this.offSetY = offSetY;
    }

    /**
     * Get the value of zoom
     *
     * @return the value of zoom
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Set the value of zoom
     *
     * @param zoom new value of zoom
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

}
