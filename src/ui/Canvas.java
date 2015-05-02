package ui;

import curves.Controller;
import ui.Events.GuiEventsAdd;
import ui.Events.GuiEventListner;
import ui.Events.GuiEventsClose;
import ui.Events.GuiEvents;
import ui.Events.GuiEventsCurrent;
import ui.Events.GuiEventsCreate;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import ui.Events.GuiEventsDeleteP;
import ui.Events.GuiEventsMove;
import ui.Events.GuiEventsOpen;
import ui.Events.GuiEventsRefresh;

/**
 * The canvas in which to draw various elements of the UI, mostly focusing on
 * the graphical side of things as well intuitive controls
 *
 * @author Kareem Horstink
 * @version 0.8
 */
public class Canvas extends JPanel implements ActionListener {

    private final List<GuiEventListner> LISTENERS = new ArrayList<>();
    private double offSetX = 0;
    private double offSetY = 0;
    private double zoom = 1;
    private double gridSpacing = 50;
    private ArrayList<List<Point2D>> curves = new ArrayList<>();
    private List<Point2D> controls = new ArrayList<>();
    private final ArrayList<Color> COLORS = new ArrayList<>();
    private boolean Visiblity = true;
    private int curveID = -1;
    private boolean first = false;
    private boolean select = false;
    private JPopupMenu popup1;
    private JPopupMenu popup2;
    private Point2D.Double point = new Point2D.Double();
    private final boolean DEBUG = true;
    private ArrayList<Ellipse2D> controlPoints;
    private int selectPoint;
    private boolean moveSelected;

    /**
     * Creates a new canvas and sets the default zoom level as well the grid
     * spacing
     *
     * @param zoom
     * @param gridSpace
     */
    protected Canvas(double zoom, double gridSpace) {
        setBackground(Color.gray);
        init();
        this.zoom = zoom;
        this.gridSpacing = gridSpace;
        this.setSize(400, 500);
    }

    private void init() {
        popup1 = new JPopupMenu();

        JMenuItem menuItem = new JMenuItem("New Line");
        menuItem.addActionListener(this);
        popup1.add(menuItem);
        menuItem = new JMenuItem("Close Curve");
        menuItem.addActionListener(this);
        popup1.add(menuItem);
        menuItem = new JMenuItem("Open Curve");
        menuItem.addActionListener(this);
        popup1.add(menuItem);

        popup2 = new JPopupMenu();
        menuItem = new JMenuItem("Move point");
        menuItem.addActionListener(this);
        popup2.add(menuItem);
        menuItem = new JMenuItem("Delete point");
        menuItem.addActionListener(this);
        popup2.add(menuItem);

        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.isControlDown()) {
                    if (curveID != -1) {
                        fireEvent(new GuiEventsAdd(this, new double[]{xm(e.getX()), ym(e.getY())}, curveID));
                    }
                } else if (SwingUtilities.isRightMouseButton(e) && e.isControlDown()) {
                    point.setLocation(xm(e.getX()), ym(e.getY()));
                    openNewPopUpCoordiante();
                    repaint();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    point.setLocation(xm(e.getX()), ym(e.getY()));
                    popup1.show(e.getComponent(), e.getX(), e.getY());
                    repaint();
                } else if (SwingUtilities.isRightMouseButton(e) && e.isShiftDown()) {
                    fireEvent(new GuiEventsRefresh(this));
                }
                if (controlPoints != null) {
                    for (int i = 0; i < controlPoints.size(); i++) {
                        if (controlPoints.get(i).contains(e.getX(), e.getY())) {
                            selectPoint = i;
                            moveSelected = true;
                            if (SwingUtilities.isRightMouseButton(e)) {
                                popup2.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                first = false;
                moveSelected = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            int preX = 0;
            int preY = 0;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!first) {
                    first = true;
                    preX = e.getX();
                    preY = e.getY();
                } else if (e.isShiftDown()) {
                    offSetX -= preX - e.getX();
                    offSetY += preY - e.getY();
                    updateControls();
                    repaint();
                    preX = e.getX();
                    preY = e.getY();
                } else if (moveSelected) {
                    fireEvent(new GuiEventsMove(this, new double[]{xm(e.getX()), ym(e.getY())}, selectPoint, curveID));
                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                repaint();
            }
        });

        addMouseWheelListener((MouseWheelEvent e) -> {
            if (Math.signum(e.getPreciseWheelRotation()) == 1) {
                setZoom(Math.abs(getZoom() / (e.getPreciseWheelRotation() * 1.05)));
            } else {
                setZoom(Math.abs(e.getPreciseWheelRotation() * 1.05 * getZoom()));
            }
        });

    }

    private void openNewPopUpCoordiante() {
        String tmp = JOptionPane.showInputDialog(this, "Enter Coordiante", "0.0, 0.0");
        try {
            tmp = tmp.replace(" ", "");
            String split[] = tmp.split(",");
            double x = Double.valueOf(split[0]);
            double y = Double.valueOf(split[1]);
            fireEvent(new GuiEventsAdd(this, new double[]{x, y}, curveID));
        } catch (Exception e) {
            System.out.println("Please enter a proper location " + e);
        }
    }

    private void openNewPopUpCoordianteMove() {
        String tmp = JOptionPane.showInputDialog(this, "Enter Coordiante", "0.0, 0.0");
        try {
            tmp = tmp.replace(" ", "");
            String split[] = tmp.split(",");
            double x = Double.valueOf(split[0]);
            double y = Double.valueOf(split[1]);
            System.out.println("x " + x + ", y " + y);
            System.out.println("point " + selectPoint);
            System.out.println("curve " + curveID);

            fireEvent(new GuiEventsMove(this, new double[]{x, y}, selectPoint, curveID));
        } catch (Exception e) {
            System.out.println("Please enter a proper location " + e);
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paint(g);
        drawGrid(g2);
        drawLines(g2);
        if (DEBUG) {
            drawControls(g2);
        }
    }

    protected void setVisiblity(boolean visiblity) {
        this.Visiblity = visiblity;
        repaint();
    }

    private void drawControls(Graphics2D g) {
        if (curveID != -1) {
            g.setColor(COLORS.get(curveID));
            for (Ellipse2D control : controlPoints) {
                g.draw(control);
            }
        }
    }

    private void drawGrid(Graphics2D g) {
        for (int i = -500; i < 500; i++) {
            g.setColor(Color.lightGray);
            g.drawLine(Integer.MAX_VALUE * -1, (int) y(gridSpacing * i), Integer.MAX_VALUE, (int) y(gridSpacing * i));
            g.drawLine((int) x(gridSpacing * i), Integer.MAX_VALUE * -1, (int) x(gridSpacing * i), Integer.MAX_VALUE);
            g.drawString(Double.toString(i * gridSpacing), (int) x(i * gridSpacing + 5), (int) y(0) - 2);
            g.drawString(Double.toString(i * gridSpacing), (int) x(5), (int) y(i * gridSpacing) - 2);
        }
    }

    private void drawLines(Graphics2D g) {
        int counter = 0;
        for (List<Point2D> curve : curves) {
            if (Visiblity || counter == curveID) {
                boolean fir = true;
                Path2D.Double tmp = new Path2D.Double(Path2D.WIND_NON_ZERO, 1);
                g.setStroke(new BasicStroke(3f));
                colorPicker();
                g.setColor(COLORS.get(counter));
                for (Point2D poin : curve) {
                    if (fir) {
                        fir = false;
                        tmp.moveTo(x(poin.getX()), y(poin.getY()));
                    } else {
                        tmp.lineTo(x(poin.getX()), y(poin.getY()));
                    }
                }
                g.draw(tmp);
            }
            counter++;
        }
    }

    private void colorPicker() {
        Random r = new Random();
        while (COLORS.size() != curves.size()) {
            COLORS.add(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        }
    }

    protected void setCurrentLine(int currentLine) {
        this.curveID = currentLine;
    }

    /**
     * Set the value of curves
     *
     * @param curves new value of curves
     */
    protected void setCurves(ArrayList<List<Point2D>> curves) {
        this.curves = curves;
        repaint();
    }

    /**
     * Get the value of zoom
     *
     * @return the value of zoom
     */
    protected double getZoom() {
        return zoom;
    }

    /**
     * Set the value of zoom
     *
     * @param zoom new value of zoom
     */
    protected void setZoom(double zoom) {
        this.zoom = zoom;
        updateControls();
        repaint();
    }

    private void fireEvent(GuiEvents event) {
        Iterator<GuiEventListner> i = LISTENERS.iterator();
        while (i.hasNext()) {
            i.next().actionPerformed(event);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().isInstance(new JMenuItem())) {
            JMenuItem tmp = (JMenuItem) e.getSource();

            if (tmp.getParent().equals(popup1)) {
                if (e.getActionCommand().equals("New Line")) {
                    String optionSelected = (String) JOptionPane.showInputDialog(this, "Please Select a Line Type", "Line Type", JOptionPane.QUESTION_MESSAGE, null, new String[]{
                        "PolyLine",
                        "Cubic Line Natural",
                        "Bezier Curve",
                        "Bezier Spline",
                        "Bezier Spline Colinear"
                    }, null);
                    if (optionSelected != null) {
                        String name = (String) JOptionPane.showInputDialog(this, "Please Give the Line a name", "Line Name", JOptionPane.QUESTION_MESSAGE);
                        if (name != null) {
                            curveID++;
                            switch (optionSelected) {
                                case "PolyLine":
                                    fireEvent(new GuiEventsCreate(this, new double[]{point.x, point.y, Controller.POLYLINE}, name));
                                    break;
                                case "Cubic Line Natural":
                                    fireEvent(new GuiEventsCreate(this, new double[]{point.x, point.y, Controller.CUBIC_N}, name));
                                    break;
                                case "Bezier Curve":
                                    fireEvent(new GuiEventsCreate(this, new double[]{point.x, point.y, Controller.BEZIERCURVE}, name));
                                    break;
                                case "Bezier Spline":
                                    fireEvent(new GuiEventsCreate(this, new double[]{point.x, point.y, Controller.BEZIERSPLINE}, name));
                                    break;
                                case "Bezier Spline Colinear":
                                    fireEvent(new GuiEventsCreate(this, new double[]{point.x, point.y, Controller.BEZIERSPLINECOLINEAR}, name));
                                    break;
                                default:
                                    System.out.println("Option Panel is incorrect");
                            }
                        }
                    }
                } else if (e.getActionCommand().equals("Close Curve")) {
                    fireEvent(new GuiEventsClose(this, curveID));
                } else if (e.getActionCommand().equals("Open Curve")) {
                    fireEvent(new GuiEventsOpen(this, curveID));
                }
            } else {
                if (e.getActionCommand().equals("Move point")) {
                    openNewPopUpCoordianteMove();
                } else if (e.getActionCommand().equals("Delete point")) {
                    fireEvent(new GuiEventsDeleteP(curves, selectPoint, curveID));
                }
            }
        }
        repaint();
    }

    protected synchronized void addEventListener(GuiEventListner list) {
        LISTENERS.add(list);
    }

    private double xm(double x) {
        return (x - getVisibleRect().width / 2 - offSetX) / zoom;
    }

    private double ym(double y) {
        return (getVisibleRect().height / 2 - y - offSetY) / zoom;
    }

    private double x(double x) {
        return zoom * (x) + offSetX + getVisibleRect().width / 2;
    }

    private double x(int x) {
        return zoom * (x) + offSetX + getVisibleRect().width / 2;
    }

    /**
     * Converts the mouse or the panel y into a graphs y
     *
     * @param y The panels y value
     * @return The graphical y value
     */
    private double y(double y) {
        return (zoom * getVisibleRect().height / 2 - zoom * y) - ((zoom - 1) * getVisibleRect().height / 2) - offSetY;
    }

    /**
     * Converts the mouse or the panel y into a graphs y
     *
     * @param y The panels y value
     * @return The graphical y value
     */
    private double y(int y) {
        return (zoom * getVisibleRect().height / 2 - zoom * y) - ((zoom - 1) * getVisibleRect().height / 2) - offSetY;
    }

    /**
     * Sets the controls points
     *
     * @param controls The controls to be changed
     */
    protected void setControls(ArrayList<List<Point2D>> controls) {
        if (curveID != -1) {
            this.controls = controls.get(curveID);
            updateControls();
        }

    }

    private void updateControls() {
        controlPoints = new ArrayList<>();
        double size = 15;
        for (Point2D control : controls) {
            controlPoints.add(new Ellipse2D.Double(x(control.getX()) - size / 2, y(control.getY()) - size / 2, size, size));
        }
    }
}
