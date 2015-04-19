package curves;

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
import javax.swing.table.TableModel;

/**
 *
 * @author Kareem Horstink
 */
public class SideBar extends JTabbedPane implements TableModelListener {

    private JTable table;
    private final List<GUI_Event_Listner> LISTENER = new ArrayList<GUI_Event_Listner>();
    private TableModel mod;
    private int curveID;
    private JCheckBox tobi;
    boolean vis;

    private ArrayList<List<Point2D>> curves;

    public void setCurves(ArrayList<List<Point2D>> curve) {
        this.curves = curve;
    }

    public SideBar() {
        curves = new ArrayList<>();
        init();
    }

    public synchronized void addEventListener(GUI_Event_Listner list) {
        LISTENER.add(list);
    }

    private void init() {
        tobi = new JCheckBox("Set Invisible");
        tobi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Tobi should do something");
                fireEvent(6, new Object[]{vis});
                vis = !vis;
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
        mod = new DefaultTableModel(header, 50);
        table = new JTable(mod);
        mod.addTableModelListener(this);
    }

    public void setData(int row, int x, int y) {
        mod.setValueAt(x, row, 1);
        mod.setValueAt(y, row, 2);
        if (mod.getValueAt(row, 0) == null) {
            mod.setValueAt(row, row, 0);
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        double rowC = row;
        Object fir = mod.getValueAt(row, 1);
        Object sec = mod.getValueAt(row, 2);

    }

    private void fireEvent(int command, Object[] info) {
        GUI_Events event = new GUI_Events(this, curveID, command, info);
        Iterator<GUI_Event_Listner> i = LISTENER.iterator();
        while (i.hasNext()) {
            i.next().handleGUI_Events(event);
        }
    }
}
