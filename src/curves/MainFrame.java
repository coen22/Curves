/*
 * Main JFrame
 */
package curves;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Kareem
 */
public class MainFrame extends JFrame implements GUI_Event_Listner {

    private final Canvas CANVAS;
    private final SideBar SIDE_BAR;
    private final Controller CONTROLLER;

    public MainFrame() {
        setTitle("Dem Curves");
        CONTROLLER = new Controller();
        CANVAS = new Canvas(1, 100);
        CANVAS.addEventListener(this);
        SIDE_BAR = new SideBar();
        add(CANVAS);
        add(SIDE_BAR, BorderLayout.EAST);
        setSize(1500, 800);
        SIDE_BAR.addEventListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
        this.setVisible(true);
        //JOptionPane.showMessageDialog(this, "Testing Message box");
    }

    public static void main(String[] args) {
        UIManager.setInstalledLookAndFeels(UIManager.getInstalledLookAndFeels());
        new MainFrame();
    }

    private void update() {
        int amount = CONTROLLER.amountOfCurves();
        ArrayList tmpList = new ArrayList();
        for (int i = 0; i < amount; i++) {
            tmpList.add(CONTROLLER.getCurvePlot(i, 10));
        }
        CANVAS.setCurves(tmpList);
        SIDE_BAR.setCurves(tmpList);
        System.out.println("Updating");
    }

    public void addPoint(double x, double y,int curveID) {
        CONTROLLER.addLastPoint(x, y, curveID);
    }

    public void removePoint(double x, double y) {
    }

    public void changePos(double deltaX, double deltaY, int curveId, int pointId) {
    }

    public void newCurve(int curveType, double x, double y, String Name) {
        CONTROLLER.createCurve(curveType, x, y, Name);
    }

    public void setName(String name, int curveId) {
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String[] command = e.getActionCommand().split(",");
//        if (command[0].equals("create")) {
//            newCurve(Integer.valueOf(command[1]), Double.valueOf(command[2]), Double.valueOf(command[3]), command[4]);
//        }
//    }
    @Override
    public void handleGUI_Events(GUI_Events e) {
        System.out.println("Handling");
        Object[] tmp = e.getInfo();
        int type = e.getCommand();
        int curveID = e.getCurveID();
        switch (type) {
            case 0://create new line
                System.out.println("I should add a new line");
                newCurve((int) tmp[2], (double) tmp[0], (double) tmp[1], (String) tmp[3]);
                break;
            case 1://create new point
                System.out.println("I should add a new point");
                addPoint(Double.valueOf((String)tmp[0]), Double.valueOf((String)tmp[1]), curveID);
                break;
            case 2://modify point
                System.out.println("I should move a point");
                break;
            case 3://delete point
                System.out.println("I should remove a point");
                break;
            case 4://close line
                System.out.println("I should close a line");
                break;
            case 5://open line
                System.out.println("I should open a line");
                break;
            case 6://Change state of visiblity
                System.out.println("Set visiablity to " + e.getInfo()[0]);
                break;
            default:
                System.out.println("Unsupported action");
        }
        update();
    }
}

//                if (!(tmp.length == 2 || tmp.length == 3)) {
//                    System.out.println("Wrong Event given");
//                    break;
//                }
//                try {
//                    double x = Double.valueOf((String) tmp[0]);
//                    double y = Double.valueOf((String) tmp[1]);
//                    if (tmp.length == 3) {
//                        int index = Integer.valueOf((String) tmp[2]);
//                        //controller.addPoint(x, y, curveID, index);
//                        System.out.println("add at point");
//                        break;
//                    }
//                    System.out.println("Add last");
//                    //controller.addLastPoint(x, y, curveID);
//                } catch (NumberFormatException e1) {
//                    System.out.println("Wrong info given");
//                    System.out.println(e1);
//                }
