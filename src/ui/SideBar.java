package ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import ui.events.GuiEventListner;
import ui.events.GuiEvents;
import ui.events.GuiEventsCurrent;
import ui.events.GuiEventsMove;
import ui.events.GuiEventsRefresh;
import ui.events.GuiEventsVisibility;

/**
 * The sidebar which shows the data input grid and other controls
 *
 * @author Kareem Horstink
 * @version 0.5
 */
public class SideBar extends JTabbedPane implements TableModelListener {

    /**
     * JTable to represent the table of info about points
     */
    private JTable table;

    /**
     * The table to hold info
     */
    private TableModel mod;

    /**
     * Current selected line
     */
    private int curveID;

    /**
     * Check box to set the line to be visible or not
     */
    private JCheckBox visibleBox;

    /**
     * Boolean to represent if the line is visible or not
     */
    private boolean visiblity;

    /**
     * List of the control points
     */
    private ArrayList<List<Point2D>> controlPoints;

    /**
     * Checks if the table is currently updating
     */
    private boolean updating1 = false;

    /**
     * Checks if the table is currently updating
     */
    private boolean updating2 = true;

    /**
     * List of the Gui Event Listener
     */
    private final List<GuiEventListner> LISTENER = new ArrayList<GuiEventListner>();

    /**
     * The panel to hold info about the line
     */
    private JPanel info;

    /**
     * An array of string to hold the curve info: Name; Area; Length; Number Of control points; Zoom
     * Level
     *
     */
    private String[] curveInfo;

    /**
     * Array of JLabels containing curveInfo[]
     */
    private JLabel[] infoText;

    /**
     * The amount of curve
     */
    private int numberOfCurve;

    /**
     * Combo box to select the current line
     */
    private JComboBox<String> comboSelected;

    /**
     * Combo box to select the algorithm 
     */
    private JComboBox<String> comboLenth;
    
    /**
     * Combo box to select the algorithm
     */
    private JComboBox<String> comboArea;
    
    private final boolean DEBUG = false;

    /**
     * The constructor of the side bar
     */
    public SideBar() {
        controlPoints = new ArrayList<>();
        init1();
    }

    /**
     * Sets the number of lines in the system
     *
     * @param numberOfCurve The number of curves
     */
    public void setNumberOfCurve(int numberOfCurve) {
        this.numberOfCurve = numberOfCurve;
    }

    /**
     * Sets the control points
     *
     * @param controlPoints The control points to be passed
     */
    protected void setCurves(ArrayList<List<Point2D>> controlPoints) {
        if (curveID == -1) {
        } else {
            this.controlPoints = controlPoints;
            while (this.controlPoints.get(curveID).size() >= mod.getRowCount()) {
                mod.addRow(new Object[]{0d, 0d, 0d});
            }
            updating1 = true;
            updateTableFull();
            updating1 = false;
        }
    }

    /**
     * Sets the current curve
     *
     * @param curveID The index of the current curve
     */
    protected void setCurveID(int curveID) {
        this.curveID = curveID;
        updating1 = true;
        updateTableFull();
        updating1 = false;
    }

    /**
     * Updates the table with the current info
     */
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

    /**
     * Initialize the buttons and info boxes
     */
    private void init1() {
        JPanel controls = new JPanel(new GridLayout(10, 0));
        controls.setName("Controls");
        this.addTab(controls.getName(), controls);
        visibleBox = new JCheckBox("Set Invisible");
        visibleBox.setHorizontalAlignment(SwingConstants.CENTER);
        visibleBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEvent(new GuiEventsVisibility(this, visiblity));
                visiblity = !visiblity;
            }
        });
        visibleBox.setToolTipText("Sets all other line to not be visible");
        controls.add(visibleBox);
        comboSelected = new JComboBox<>(new String[]{""});
        comboSelected.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (DEBUG) {
                    System.out.println(e.getActionCommand());
                }
                if (e.getSource() == comboSelected && comboSelected.getItemCount() != 0 && !updating2) {
                    fireEvent(new GuiEventsCurrent(this, comboSelected.getSelectedIndex()));
                    curveID = comboSelected.getSelectedIndex();
                }
            }
        });
        controls.add(comboSelected);
        
        

        curveInfo = new String[5];

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
            JOptionPane.showMessageDialog(controls, "Crtl Click to Add New Points"
                    + "\n" + "Panning with shift click");
        });
        controls.add(button);

        infoText = new JLabel[]{
            new JLabel("Name"),
            new JLabel("Area"),
            new JLabel("Length"),
            new JLabel("Number Of control points"),
            new JLabel("Zoom Level")};
        info = new JPanel(new GridLayout(6, 0));
        info.setToolTipText("Information about the line");
        for (JLabel infoText1 : infoText) {
            infoText1.setHorizontalAlignment(SwingConstants.CENTER);
            info.add(infoText1);
        }
        controls.add(info);
        init2();

    }

    /**
     * Initialize the table
     */
    private void init2() {
        JPanel dataViewer = new JPanel();
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        dataViewer.add(scrollPane);
        dataViewer.setName("Data Inputter");
        this.addTab(dataViewer.getName(), dataViewer);
    }

    /**
     * Update the info for the curve
     *
     * @param info The info from the curve
     */
    protected void updateInfo(String[] info) {
        updating2 = true;
        if (info.length == 5) {
            curveInfo = info;
        }
        updateInfoPanel();
        comboSelected.removeAllItems();
        for (int i = 0; i < numberOfCurve; i++) {
            comboSelected.addItem(Integer.toString(i + 1));
        }
        if (DEBUG) {
            System.out.println(curveID);
        }
        comboSelected.setSelectedIndex(curveID);
        updating2 = false;
    }

    /**
     * Updates the jLabel
     */
    private void updateInfoPanel() {
        infoText[0].setText("Name " + curveInfo[0]);
        infoText[1].setText("Area " + curveInfo[1]);
        infoText[2].setText("Length " + curveInfo[2]);
        infoText[3].setText("Number Of control points " + curveInfo[3]);
        infoText[4].setText("Zoom Level " + curveInfo[4]);

    }

    /**
     * Creates the JTable and the table model
     */
    private void createTable() {
        String[] header = {"Point ID", "X-coordinates", "Y-coordinates"};
        mod = new TableModel(header);
        table = new JTable(mod);
        mod.addTableModelListener(this);
    }

    /**
     * A listener to check if the table has been changed
     *
     * @param e
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        if (!updating1) {
            int row = e.getFirstRow();
            if (DEBUG) {
                System.out.println(row);
            }
            double fir = mod.getValueAt(row, 1);
            double sec = mod.getValueAt(row, 2);
            if (DEBUG) {
                System.out.println("Table has been changed");
            }
            fireEvent(new GuiEventsMove(this, new double[]{fir, sec}, row, curveID));
        }
    }

    /**
     * Handles the logic of the pop up
     *
     * @param e The event
     */
    private void fireEvent(GuiEvents event) {
        Iterator<GuiEventListner> i = LISTENER.iterator();
        while (i.hasNext()) {
            i.next().actionPerformed(event);
        }
    }

    /**
     * Attaches a eventListner to the object
     *
     * @param list The GuiEventListener
     */
    protected synchronized void addEventListener(GuiEventListner list) {
        LISTENER.add(list);
    }

}
