package ui;

import java.awt.Component;
import java.awt.GridLayout;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
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
    private JCheckBox checkBox;
    private boolean visiblity;
    private ArrayList<List<Point2D>> controlPoints;
    private boolean updating;
    private final boolean DEBUG = true;
    private final List<GuiEventListner> LISTENER = new ArrayList<GuiEventListner>();
    JPanel info;

    /**
     * The constructor of the side bar
     */
    public SideBar() {
        controlPoints = new ArrayList<>();
        init1();
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

    private void init1() {
        JPanel controls = new JPanel(new GridLayout(10, 0));
        controls.setName("Controls");
        this.addTab(controls.getName(), controls);
        checkBox = new JCheckBox("Set Invisible");
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fireEvent(new GuiEventsVisibility(this, visiblity));
                visiblity = !visiblity;
            }
        });
        checkBox.setToolTipText("Sets all other line to not be visible");
        controls.add(checkBox);

        JButton button = new JButton("Refresh");
        button.setToolTipText("Refresh all the data");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fireEvent(new GuiEventsRefresh(this));
            }
        });
        controls.add(button);
        button = new JButton("Help");
        button.setToolTipText("Shows a help diaglog");
        button.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(controls, "Crtl Click to Add New Points" + "\n" + "Panning with shift click");
        });
        controls.add(button);

        info = new JPanel();
        info.setToolTipText("Information about the line");
        info.add(new JLabel("Name"));
        info.add(new JLabel("Area"));
        info.add(new JLabel("Length"));
        info.add(new JLabel("Number Of control points"));
        info.add(new JLabel("Zoom Level"));

        controls.add(info);
        init2();

    }

    private void init2() {
        JPanel dataViewer = new JPanel();
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        dataViewer.add(scrollPane);
        dataViewer.setName("Data Inputter");
        this.addTab(dataViewer.getName(), dataViewer);
    }

    protected void updateInfo(String[] info) {
        Component[] com = this.info.getComponents();
        int j = 0;
        for (int i = 0; i < com.length; i++) {
            if (com[i] instanceof JLabel) {
                j++;
                ((JLabel) (com[i])).setText(info[j]);
            }
        }

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
            double fir = mod.getValueAt(row, 1);
            double sec = mod.getValueAt(row, 2);
            if (DEBUG) {
                System.out.println("Table has been changed");
            }
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

}
