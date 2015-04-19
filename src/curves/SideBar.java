package curves;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
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
 * @author Kareem
 */
public class SideBar extends JTabbedPane implements TableModelListener {
    private int counter;
    private JTable table;
    private List<GUI_Event_Listner> listeners = new ArrayList<GUI_Event_Listner>();
    
    public SideBar() {
        counter = 0;
        init();
    }
    
    public synchronized void addEventListener(GUI_Event_Listner list){
        listeners.add(list);
    }

    private void init() {
        JPanel controls = new JPanel();
        controls.setName("Controls");
        controls.setBackground(Color.red);
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
        TableModel tableModel = new DefaultTableModel(header, 50);
        table = new JTable(tableModel);
        table.getModel().addTableModelListener(this);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        GUI_Events event = new GUI_Events(this, 1);
        listeners.get(0).handleGUI_Events(event);
        System.out.println(e.getFirstRow());
    }
}
