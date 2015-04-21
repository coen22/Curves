package ui;

import ui.Events.Gui_Events_Add;
import ui.Events.GUI_Event_Listner;
import ui.Events.Gui_Events_Close;
import ui.Events.Gui_Events;
import ui.Events.Gui_Events_Current;
import ui.Events.Gui_Events_Create;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
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
import ui.Events.Gui_Events_Open;
import ui.Events.Gui_Events_Refresh;

/**
 * The canvas in which to draw various elements of the UI, mostly focusing on
 * the graphical side of things
 *
 * @author Kareem Horstink
 */
public class Canvas extends JPanel implements ActionListener {

    private final List<GUI_Event_Listner> listeners = new ArrayList<>();
    private double offSetX = 0;
    private double offSetY = 0;
    private double zoom = 1;
    private double gridSpacing = 50;
    private ArrayList<List<Point2D>> curves = new ArrayList<>();
    private final ArrayList<Color> COLORS = new ArrayList<>();
    private boolean Visiblity = true;
    private int curveID = -1;
    private boolean first = false;
    private JPopupMenu popup1;
    private JPopupMenu popup2;
    private Point2D.Double point = new Point2D.Double();

    public Canvas(double zoom, double gridSpace) {
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

        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.isShiftDown()) {
                    if (curveID != -1) {
                        fireEvent(new Gui_Events_Add(this, new double[]{xm(e.getX()), ym(e.getY())}, curveID));
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    point.setLocation(xm(e.getX()), ym(e.getY()));
                    popup1.show(e.getComponent(), e.getX(), e.getY());
                    repaint();
                } else if (SwingUtilities.isLeftMouseButton(e) && e.isAltDown()) {
                    fireEvent(new Gui_Events_Refresh(this));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                first = false;
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
                }
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    offSetX -= preX - e.getX();
                    offSetY += preY - e.getY();
                    repaint();
                    preX = e.getX();
                    preY = e.getY();
                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (Math.signum(e.getPreciseWheelRotation()) == 1) {
                    setZoom(Math.abs(getZoom() / (e.getPreciseWheelRotation() * 1.05)));
                } else {
                    setZoom(Math.abs(e.getPreciseWheelRotation() * 1.05 * getZoom()));
                }
            }
        });

    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paint(g);
        drawGrid(g2);
        drawLines(g2);
    }

    public void setVisiblity(boolean visiblity) {
        this.Visiblity = visiblity;
        repaint();
    }

    private void drawGrid(Graphics2D g) {
        for (int i = -1000; i < 1000; i++) {
            g.setColor(Color.lightGray);
            g.setStroke(new BasicStroke(0));
            g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
            g.draw(new Line2D.Double(x(-Double.MAX_VALUE), y(gridSpacing * i), x(Double.MAX_VALUE), y(gridSpacing * i)));
            g.draw(new Line2D.Double(x(gridSpacing * i), y(-Double.MAX_VALUE), x(gridSpacing * i), y(Double.MAX_VALUE)));
            g.drawString(Double.toString(i * gridSpacing), (int) x(i * gridSpacing + 5), (int) y(0));
            g.drawString(Double.toString(i * gridSpacing), (int) x(5), (int) y(i * gridSpacing));
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

    public void colorPicker() {
        Random r = new Random();
        while (COLORS.size() != curves.size()) {
            COLORS.add(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        }
    }

    public void setCurrentLine(int currentLine) {
        this.curveID = currentLine;
    }

    /**
     * Set the value of curves
     *
     * @param curves new value of curves
     */
    public void setCurves(ArrayList<List<Point2D>> curves) {
        this.curves = curves;
        repaint();
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
        repaint();
        this.zoom = zoom;
    }

    private void fireEvent(Gui_Events event) {
        Iterator<GUI_Event_Listner> i = listeners.iterator();
        while (i.hasNext()) {
            i.next().actionPerformed(event);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("New Line")) {
            String optionSelected = (String) JOptionPane.showInputDialog(this, "Please Select a Line Type", "Line Type", JOptionPane.QUESTION_MESSAGE, null, new String[]{"PolyLine", "Cubic Line", "B-Spline"}, null);
            if (optionSelected != null) {
                String name = (String) JOptionPane.showInputDialog(this, "Please Give the Line a name", "Line Name", JOptionPane.QUESTION_MESSAGE);
                if (name != null) {
                    curveID++;
                    fireEvent(new Gui_Events_Current(this, curveID));
                    switch (optionSelected) {
                        case "PolyLine":
                            fireEvent(new Gui_Events_Create(this, new double[]{point.x, point.y, 1}, name));
                            break;
                        case "Cubic Line":
                            fireEvent(new Gui_Events_Create(this, new double[]{point.x, point.y, 2}, name));
                            break;
                        case "B-Spline":
                            fireEvent(new Gui_Events_Create(this, new double[]{point.x, point.y, 13}, name));
                            break;
                        default:
                            System.out.println("Option Panel is incorrect");
                    }
                }
            }
        } else if (e.getActionCommand().equals("Close Curve")) {
            fireEvent(new Gui_Events_Close(this, curveID));
        }else if(e.getActionCommand().equals("Open Curve")){
            fireEvent(new Gui_Events_Open(this, curveID));
        }
        repaint();
    }

    public synchronized void addEventListener(GUI_Event_Listner list) {
        listeners.add(list);
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

    private double y(double y) {
        return (zoom * getVisibleRect().height / 2 - zoom * y) - ((zoom - 1) * getVisibleRect().height / 2) - offSetY;
    }

    private double y(int y) {
        return (zoom * getVisibleRect().height / 2 - zoom * y) - ((zoom - 1) * getVisibleRect().height / 2) - offSetY;
    }
}
