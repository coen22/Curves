/*
 * Main JFrame
 */
package curves;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Kareem
 */
public class MainFrame extends JFrame implements ActionListener {

    private final Canvas canvas;
    private final SideBar sideBar;
    private Controller controller;

    public MainFrame() {
        controller = new Controller();
        canvas = new Canvas(1, 100);
        sideBar = new SideBar();
        sideBar.setBackground(Color.red);
        add(canvas);
        add(sideBar, BorderLayout.EAST);
        setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        //JOptionPane.showMessageDialog(this, "Testing Message box");
    }

    public static void main(String[] args) {
        new MainFrame();
    }

    public void addPoint(double x, double y) {
    }

    public void removePoint(double x, double y) {
    }

    public void changePos(double deltaX, double deltaY, int curveId, int pointId) {
    }

    public void newCurve(int curveType, double x, double y, String Name) {
        controller.createCurve(curveType, x, y, Name);
    }

    public void setName(String name, int curveId) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] command = e.getActionCommand().split(",");
        if (command[0].equals("create")) {
            newCurve(Integer.valueOf(command[1]), Double.valueOf(command[2]), Double.valueOf(command[3]), command[4]);
        }
    }
}
