/*
 * Main JFrame
 */
package curves;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Kareem
 */
public class MainFrame extends JFrame implements GUI_Event_Listner {

    private final Canvas canvas;
    private final SideBar sideBar;
    private Controller controller;

    public MainFrame() {
        setTitle("Dem Curves");
        controller = new Controller();
        canvas = new Canvas(1, 100);
        canvas.addEventListener(this);
        sideBar = new SideBar();
        add(canvas);
        add(sideBar, BorderLayout.EAST);
        setSize(1500, 800);
        sideBar.addEventListener(this);
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
                break;
            case 1://create new point
                if (!(tmp.length == 2 || tmp.length == 3)) {
                    System.out.println("Wrong Event given");
                    break;
                }
                try {
                    double x = Double.valueOf((String) tmp[0]);
                    double y = Double.valueOf((String) tmp[1]);
                    if (tmp.length == 3) {
                        int index = Integer.valueOf((String) tmp[2]);
                        //controller.addPoint(x, y, curveID, index);
                        System.out.println("add at point");
                        break;
                    }
                    System.out.println("Add last");
                    //controller.addLastPoint(x, y, curveID);
                } catch (NumberFormatException e1) {
                    System.out.println("Wrong info given");
                    System.out.println(e1);
                }
                break;
            case 2://modify point
                break;
            case 3://delete point
                break;
            case 4://close line
                break;
            case 5://open line
                break;
            default:
                System.out.println("Unsupported action");
        }
    }
}
