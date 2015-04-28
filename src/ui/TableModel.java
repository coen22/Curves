package ui;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * A two dimensional data structure used to store double instances,
 * usually for display in a JTable component.
 *
 * @author Kareem Horstink
 * @version 1.0
 */
public class TableModel extends AbstractTableModel implements Serializable {

    static final long serialVersionUID = 6680042567037222321L;
    private ArrayList<ArrayList<Double>> data;
    private ArrayList<String> header;

    /**
     * 
     * @param header 
     */
    protected TableModel(ArrayList<String> header) {
        data = new ArrayList<>();
        this.header = header;
    }

    /**
     * 
     * @param header 
     */
    protected TableModel(String[] header) {
        ArrayList<String> tmp = new ArrayList();
        for (String tmp1 : header) {
            tmp.add(tmp1);
        }
        this.header = tmp;
        data = new ArrayList<>();
        System.out.println(data.size());
        ArrayList tmp1 = new ArrayList();
        tmp1.add(0d);
        tmp1.add(0d);
        tmp1.add(0d);
        data.add(tmp1);

        System.out.println(data);

    }

    /**
     * 
     * @param data
     * @param header 
     */
    protected TableModel(ArrayList<ArrayList<Double>> data, String[] header) {
        this.data = data;
        ArrayList<String> tmp = new ArrayList();
        for (String tmp1 : tmp) {
            tmp.add(tmp1);
        }
        this.header = tmp;
    }

    /**
     * 
     * @param rowData 
     */
    protected void addRow(Object[] rowData) {
        ArrayList tmp = new ArrayList();
        for (Object tmp1 : rowData) {
            tmp.add(tmp1);
        }
        data.add(tmp);
    }

    /**
     * 
     * @param data
     * @param header 
     */
    protected TableModel(ArrayList<ArrayList<Double>> data, ArrayList<String> header) {
        this.data = data;
        this.header = header;
    }

    /**
     * 
     * @return 
     */
    protected ArrayList getData() {
        return data;
    }

    /**
     * 
     */
    protected void removeLast() {
        data.remove(data.size() - 1);
        fireTableRowsDeleted(data.size()-1, data.size()-1);

    }

    /**
     * 
     * @param row 
     */
    protected void removeRow(int row) {
        data.remove(row);
        fireTableRowsDeleted(row, row);
    }

    /**
     * 
     * @return 
     */
    @Override
    public int getRowCount() {
        return data.size() - 1;
    }

    /**
     * 
     * @param row
     * @param column
     * @return 
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @return 
     */
    @Override
    public int getColumnCount() {
        if (data.isEmpty()) {
            return 0;
        } else {
            return data.get(0).size();
        }
    }

    /**
     * 
     * @param column
     * @return 
     */
    @Override
    public String getColumnName(int column) {
        return header.get(column);
    }

    /**
     * 
     * @param rowIndex
     * @param columnIndex
     * @return 
     */
    @Override
    public Double getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).get(columnIndex);
    }

    /**
     * 
     * @param value
     * @param rowIndex
     * @param columnIndex 
     */
    protected void setValueAt(double value, int rowIndex, int columnIndex) {
        data.get(rowIndex).set(columnIndex, value);
        fireTableCellUpdated(rowIndex, columnIndex);

    }

    /**
     * 
     * @param value
     * @param row
     * @param column 
     */
    @Override
    public void setValueAt(Object value, int row, int column) {
        if (value != null) {
            if (isNumeric((String) (value))) {
                data.get(row).set(column, Double.valueOf((String) value));
                fireTableCellUpdated(row, column);
            } else {
                System.out.println("Error /n Please enter a valid number");
            }
        }
    }

    /**
     * 
     * @param str
     * @return 
     */
    protected static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
