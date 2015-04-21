package ui;

import ui.Events.Gui_Events_Add;
import ui.Events.Gui_Events_Open;
import ui.Events.GUI_Event_Listner;
import ui.Events.Gui_Events;
import ui.Events.Gui_Events_Close;
import ui.Events.Gui_Events_DeleteC;
import ui.Events.Gui_Events_DeleteP;
import ui.Events.Gui_Events_Move;
import ui.Events.Gui_Events_Vis;
import ui.Events.Gui_Events_Current;
import ui.Events.Gui_Events_Create;
import curves.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * The main frame to put everything on
 *
 * @author Kareem Horstink
 */
public class MainFrame extends JFrame implements GUI_Event_Listner {

    private final Canvas CANVAS;
    private final SideBar SIDE_BAR;
    private final Controller CONTROLLER;
    private final boolean DEBUG = false;

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
        JOptionPane.showMessageDialog(this, "Shift Click to Add New Points");
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
        if (amount >= 0) {
            CANVAS.setCurves(tmpList);
            SIDE_BAR.setCurves(tmpList);
            System.out.println("Updating");
        } else {
            System.out.println("Insufficient amount of curves");
        }
    }

    @Override
    public void handleCreate(Gui_Events_Create e) {
        if (DEBUG) {
            System.out.println("Creating");
        }
        if (e.getInfo().length == 4) {
            CONTROLLER.createCurve((int) e.getInfo()[2], e.getInfo()[0], e.getInfo()[1], e.getName(), (int) e.getInfo()[3]);
        } else if (e.getInfo().length == 3) {
            CONTROLLER.createCurve((int) e.getInfo()[2], e.getInfo()[0], e.getInfo()[1], e.getName());
        } else {
            System.out.println("Error");
        }
        update();
    }

    @Override
    public void handleAdd(Gui_Events_Add e) {
        if (DEBUG) {
            System.out.println("Adding a new point");
        }
        CONTROLLER.addLastPoint(e.getInfo()[0], e.getInfo()[1], e.getCurveID());
        update();
    }

    @Override
    public void handleMove(Gui_Events_Move e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleDeleteP(Gui_Events_DeleteP e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleDeleteC(Gui_Events_DeleteC e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleClose(Gui_Events_Close e) {
        if (DEBUG) {
            System.out.println("Closing Curve");
        }
        CONTROLLER.closeCurve(e.getCurveID());
        update();
    }

    @Override
    public void handleOpen(Gui_Events_Open e) {
        if (DEBUG) {
            System.out.println("Closing Curve");
        }
        CONTROLLER.openCurve(e.getCurveID());
        update();
    }

    @Override
    public void handleVis(Gui_Events_Vis e) {
        CANVAS.setVisiblity(e.getVisiablity());
    }

    @Override
    public void handleCurrent(Gui_Events_Current e) {
        if (DEBUG) {
            System.out.println("Changing Current Line");
        }
        if (!e.getSource().getClass().equals(CANVAS.getClass())) {
            CANVAS.setCurrentLine(e.getCurveID());
        } else {
            SIDE_BAR.setCurveID(e.getCurveID());
        }
    }

    @Override
    public void actionPerformed(Gui_Events e) {
        if (Gui_Events_Add.class.equals(e.getClass())) {
            handleAdd((Gui_Events_Add) e);
        } else if (Gui_Events_Close.class.equals(e.getClass())) {
            handleClose((Gui_Events_Close) e);
        } else if (Gui_Events_Move.class.equals(e.getClass())) {
            handleMove((Gui_Events_Move) e);
        } else if (Gui_Events_Create.class.equals(e.getClass())) {
            handleCreate((Gui_Events_Create) e);
        } else if (Gui_Events_Current.class.equals(e.getClass())) {
            handleCurrent((Gui_Events_Current) e);
        } else if (Gui_Events_DeleteC.class.equals(e.getClass())) {
            handleDeleteC((Gui_Events_DeleteC) e);
        } else if (Gui_Events_DeleteP.class.equals(e.getClass())) {
            handleDeleteP((Gui_Events_DeleteP) e);
        } else if (Gui_Events_Open.class.equals(e.getClass())) {
            handleOpen((Gui_Events_Open) e);
        } else if (Gui_Events_Vis.class.equals(e.getClass())) {
            handleVis((Gui_Events_Vis) e);
        } else {
            System.out.println("Not handled");
        }
    }
}
