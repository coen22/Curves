package ui;

import ui.Events.GuiEventListner;
import ui.Events.GuiEvents;
import ui.Events.GuiEventsVisibility;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import ui.Events.GuiEventsMove;
import ui.Events.GuiEventsRefresh;

/**
 * The sidebar which shows the data input grid and other controls
 *
 * @author Kareem Horstink
 * @version 0.5
 */
public class SideBar extends JTabbedPane implements TableModelListener {

    private JTable table;
    private TableModel mod;
    private int curveID;
    private JCheckBox tobi;
    private boolean visiblity;
    private ArrayList<List<Point2D>> controlPoints;
    private boolean updating;

    /**
     * The constructor of the side bar
     */
    public SideBar() {
        controlPoints = new ArrayList<>();

        init();
    }

    /**
     * Sets the control poi nts
     *
     * @param controlPoints The control points to be passed
     */
    protected void setCurves(ArrayList<List<Point2D>> controlPoints) {
        curveID = controlPoints.size() - 1;
        this.controlPoints = controlPoints;
        while (this.controlPoints.get(curveID).size() >= mod.getRowCount()) {
            mod.addRow(new Object[]{0d, 0d, 0d});
        }
        updating = true;
        updateTableFull();
        updating = false;
    }

    protected void setCurveID(int curveID) {
        this.curveID = curveID;
        updateTableFull();
    }

    protected void updateTable() {
        if (curveID < controlPoints.size()) {
            List<Point2D> curve = controlPoints.get(curveID);
            int counter = 0;
            for (Point2D point : curve) {
                mod.setValueAt(point.getX(), counter, 1);
                mod.setValueAt(point.getY(), counter, 2);
                mod.setValueAt(counter, counter, 0);
                counter++;
            }
        }
    }

    protected void updateTableFull() {
        if (curveID < controlPoints.size()) {
            List<Point2D> curve = controlPoints.get(curveID);
            int counter = 0;
            int rows = mod.getRowCount();
            for (int i = 0; i < rows; i++) {
                if (i < curve.size()) {
                    Point2D point = curve.get(i);
                    mod.setValueAt(point.getX(), counter, 1);
                    mod.setValueAt(point.getY(), counter, 2);
                    mod.setValueAt(counter, counter, 0);
                } else {
                    mod.removeLast();
                }
                counter++;
            }
        }
    }

    private void init() {
        tobi = new JCheckBox("Set Invisible");
        tobi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fireEvent(new GuiEventsVisibility(this, visiblity));
                visiblity = !visiblity;
            }
        });
        JPanel controls = new JPanel();
        controls.add(tobi);
        controls.setName("Controls");
        this.addTab(controls.getName(), controls);
        JPanel dataViewer = new JPanel();
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        dataViewer.add(scrollPane);
        dataViewer.setName("Data Inputter");
        this.addTab(dataViewer.getName(), dataViewer);
        JButton t = new JButton("refresh");
        t.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fireEvent(new GuiEventsRefresh(this));
            }
        });
        controls.add(t);
        t = new JButton("Help");
        t.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(controls, "Crtl Click to Add New Points" + "\n" + "Panning with shift click");
            }
        });
       controls.add(t);
    }

    private void createTable() {
        String[] header = {"Point ID", "X-coordinates", "Y-coordinates"};
        mod = new TableModel(header);
        table = new JTable(mod);
        mod.addTableModelListener(this);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (!updating) {
            int row = e.getFirstRow();
            System.out.println(row);
            double rowC = row;
            double fir = mod.getValueAt(row, 1);
            double sec = mod.getValueAt(row, 2);
            System.out.println("Table has been changed");
            fireEvent(new GuiEventsMove(this, new double[]{fir, sec}, row, curveID));
        }
    }

    private void fireEvent(GuiEvents event) {
        Iterator<GuiEventListner> i = LISTENER.iterator();
        while (i.hasNext()) {
            i.next().actionPerformed(event);
        }
    }

    protected synchronized void addEventListener(GuiEventListner list) {
        LISTENER.add(list);
    }

    private final List<GuiEventListner> LISTENER = new ArrayList<GuiEventListner>();

}
