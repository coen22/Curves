package ui;

import ui.Events.GUI_Event_Listner;
import ui.Events.Gui_Events;
import ui.Events.Gui_Events_Vis;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * The sidebar which shows the data input grid and other controls
 *
 * @author Kareem Horstink
 */
public class SideBar extends JTabbedPane implements TableModelListener {

    private JTable table;
    private TableModel mod;
    private int curveID;
    private JCheckBox tobi;
    private boolean visiblity;
    private ArrayList<List<Point2D>> controlPoints;

    public SideBar() {
        controlPoints = new ArrayList<>();
        init();
    }

    public void setCurves(ArrayList<List<Point2D>> curve) {
        curveID = curve.size() - 1;
        this.controlPoints = curve;
        while (this.controlPoints.get(curveID).size() >= mod.getRowCount()) {
            mod.addRow(new Object[]{0d, 0d, 0d});
        }
        updateTableFull();
    }

    public void setCurveID(int curveID) {
        this.curveID = curveID;
        updateTableFull();
    }

    public void updateTable() {
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

    public void updateTableFull() {
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
                fireEvent(new Gui_Events_Vis(this, visiblity));
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
    }

    private void createTable() {
        String[] header = {"Point ID", "X-coordinates", "Y-coordinates"};
        mod = new TableModel(header);
        table = new JTable(mod);
        mod.addTableModelListener(this);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        double rowC = row;
        Object fir = mod.getValueAt(row, 1);
        Object sec = mod.getValueAt(row, 2);
        System.out.println("Table has been changed");
    }

    private void fireEvent(Gui_Events event) {
        Iterator<GUI_Event_Listner> i = LISTENER.iterator();
        while (i.hasNext()) {
            i.next().actionPerformed(event);
        }
    }

    public synchronized void addEventListener(GUI_Event_Listner list) {
        LISTENER.add(list);
    }

    private final List<GUI_Event_Listner> LISTENER = new ArrayList<GUI_Event_Listner>();

}
