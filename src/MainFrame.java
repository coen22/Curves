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

    public void addPoint(double x, double y) {
    }

    public void removePoint(double x, double y) {
    }

    public void changePos(double deltaX, double deltaY, int curveId, int pointId) {
    }

    public void newCurve(int curveType, double x, double y, String Name) {
    }

    public void setName(String name, int curveId) {
    }

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
