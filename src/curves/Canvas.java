package curves;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

/**
 * The canvas in which to draw various elements of the UI, mostly focusing on
 * the graphical side of things
 *
 * @author Kareem Horstink
 */
public class Canvas extends JPanel {

    private double offSetX = 0;
    private double offSetY = 0;
    private double zoom = 15;
    private double gridSpacing = 50;
    private ArrayList<List<Point2D>> curves = new ArrayList<>();
    private boolean allPoints = false;
    private int currentLine = 0;

    private double x(double x) {
        return zoom * (x) + offSetX + getVisibleRect().width / 2;
    }

    private double x(int x) {
        return zoom * (x) + offSetX + getVisibleRect().width / 2;
    }
    
    private double y(double y) {
        return (zoom * getVisibleRect().height / 2 - zoom * y) - ((zoom - 1) * getVisibleRect().height / 2) - offSetY;
    }
    
    private double y(int y) {
        return (zoom * getVisibleRect().height / 2 - zoom * y) - ((zoom - 1) * getVisibleRect().height / 2) - offSetY;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paint(g);
        drawGrid(g2);
        drawLines(g2);
        
    }

    private void drawGrid(Graphics2D g) {
        for (int i = -500; i < 500; i++) {
            g.setColor(Color.lightGray);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
            g.draw(new Line2D.Double(x(-Double.MAX_VALUE), y(gridSpacing * i), x(Double.MAX_VALUE), y(gridSpacing * i)));
            g.draw(new Line2D.Double(x(gridSpacing * i), y(-Double.MAX_VALUE), x(gridSpacing * i), y(Double.MAX_VALUE)));
            g.drawString(Double.toString(i * gridSpacing), (int) x(i * gridSpacing + 5), (int) y(0));
            g.drawString(Double.toString(i * gridSpacing), (int) x(5), (int) y(i * gridSpacing));
        }
    }

    private void drawLines(Graphics2D g) {
        for (List<Point2D> curve : curves) {
            boolean first = true;
            Path2D.Double tmp = new Path2D.Double(Path2D.WIND_NON_ZERO, 1);
            g.setStroke(new BasicStroke(3f));
            g.setColor(colorPicker());
            for (Point2D point : curve) {
                if (first) {
                    first = false;
                    tmp.moveTo(x(point.getX()), y(point.getY()));
                } else {
                    tmp.lineTo(x(point.getX()), y(point.getY()));
                }
            }
            g.draw(tmp);
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
        BezierCurve test = new BezierCurve(new Point2D.Double(100, 100), "bob");
        test.add(0,100);
        test.add(50,50);
        test.add(25, 40);
        
        curves.add(test.calcPlotingCoordinates(0.001));
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
    public ArrayList<List<Point2D>> getCurves() {
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
