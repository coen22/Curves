/*
 * Place to draw the curves
 */
package curves;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author Kareem
 */
public class Canvas extends JPanel {
    
    private double offSetX = 0;
    private double offSetY = 0;
    private double zoom = 0;
    private double gridSpacing = 0;
    private ArrayList<List<Point2D>> curves = new ArrayList<>();
    private boolean allPoints = false;
    private int currentLine = 0;
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.repaint();
        for (List<Point2D> curve : curves) {
            boolean first = true;
            Path2D.Double tmp = new Path2D.Double(Path2D.WIND_NON_ZERO, 1);
            g2.setColor(colorPicker());
            for (Point2D point : curve) {
                if (!first) {
                    first = false;
                    tmp.moveTo(point.getX(), point.getY());
                } else {
                    tmp.lineTo(point.getX(), point.getY());
                }
            }
        }
    }
    
    public Color colorPicker() {
        Random r = new Random();
        return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
    }
    
    public Canvas(double zoom, double gridSpace) {
        setBackground(Color.gray);
        this.zoom = zoom;
        this.gridSpacing = gridSpace;
        this.setSize(400, 500);
    }
    
    public double getGridSpacing() {
        return gridSpacing;
    }
    
    public void setGridSpacing(double gridSpacing) {
        this.gridSpacing = gridSpacing;
    }
    
    public boolean isAllPoints() {
        return allPoints;
    }
    
    public void setAllPoints(boolean allPoints) {
        this.allPoints = allPoints;
    }
    
    public int getCurrentLine() {
        return currentLine;
    }
    
    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }

    /**
     * Get the value of curves
     *
     * @return the value of curves
     */
    public ArrayList<Curve> getCurves() {
        return curves;
    }

    /**
     * Set the value of curves
     *
     * @param curves new value of curves
     */
    public void setCurves(ArrayList<List<Point2D>> curves) {
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
