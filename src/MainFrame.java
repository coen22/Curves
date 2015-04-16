/*
 * Main JFrame
 */
package curves;

import javax.swing.JFrame;

/**
 *
 * @author Kareem
 */
public class MainFrame extends JFrame {
    private double zoom = 1;
    private double offSetX = 0;
    private double offSetY = 0;
    private double gridSpacing = 100;

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getOffSetX() {
        return offSetX;
    }

    public void setOffSetX(double offSetX) {
        this.offSetX = offSetX;
    }

    public double getOffSetY() {
        return offSetY;
    }

    public void setOffSetY(double offSetY) {
        this.offSetY = offSetY;
    }

    public double getGridSpacing() {
        return gridSpacing;
    }

    public void setGridSpacing(double gridSpacing) {
        this.gridSpacing = gridSpacing;
    }
    
    
}
