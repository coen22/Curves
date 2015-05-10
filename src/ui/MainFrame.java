package ui;

import curves.Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import ui.events.GuiEventAreaChange;
import ui.events.GuiEventLengthChange;
import ui.events.GuiEventListner;
import ui.events.GuiEvents;
import ui.events.GuiEventsAdd;
import ui.events.GuiEventsClose;
import ui.events.GuiEventsCreate;
import ui.events.GuiEventsCurrent;
import ui.events.GuiEventsDeleteC;
import ui.events.GuiEventsDeleteP;
import ui.events.GuiEventsMove;
import ui.events.GuiEventsOpen;
import ui.events.GuiEventsRefresh;
import ui.events.GuiEventsVisibility;

/**
 * The main frame to put everything on
 *
 * @author Kareem Horstink
 */
public class MainFrame extends JFrame implements GuiEventListner {

    /**
     * To test the rest of the program
     *
     * @param args This will be ignored
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            System.out.println(ex);
        }
        new MainFrame();
    }

    /**
     * The canvas
     */
    private final Canvas CANVAS;

    /**
     * The side bar
     */
    private final SideBar SIDE_BAR;

    /**
     * The controller that changes the line
     */
    private final Controller CONTROLLER;

    /**
     * The current selected line
     */
    private int selected = -1;

    private final boolean DEBUG = false;

    /**
     * Default constructor
     */
    public MainFrame() {
        setTitle("Dem Curves");
        CONTROLLER = new Controller();
        CANVAS = new Canvas(1, 1);
        CANVAS.setGridspacing(100);
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
    }

    /**
     * Fully update the canvas and side bar
     */
    private void update() {
        int amount = CONTROLLER.amountOfCurves();
        ArrayList tmpList = new ArrayList();
        for (int i = 0; i < amount; i++) {
            tmpList.add(CONTROLLER.getCurvePlot(i, (int) (10 * CANVAS.getZoom())));
        }

        if (amount > 0) {
            CANVAS.setCurves(tmpList);
            tmpList = new ArrayList();
            for (int i = 0; i < amount; i++) {
                tmpList.add(CONTROLLER.getControlsPoints(i));
            }
            SIDE_BAR.setCurveID(selected);
            SIDE_BAR.setCurves(tmpList);
            SIDE_BAR.setNumberOfCurve(amount);
            SIDE_BAR.updateInfo(new String[]{CONTROLLER.getCurveName(selected), Double.toString(CONTROLLER.curveArea(selected)), Double.toString(CONTROLLER.curveLength(selected)), Integer.toString(CONTROLLER.getControlsPoints(selected).size()), Double.toString(CANVAS.getZoom())});
            CANVAS.setControls(tmpList);
            if (DEBUG) {
                System.out.println("Updating");
            }
        } else {
            System.out.println("Insufficient amount of curves");
        }
    }

    /**
     * Only updates the graphical plot in the canvas
     */
    private void updateG() {
        int amount = CONTROLLER.amountOfCurves();
        ArrayList tmpList = new ArrayList();
        if (DEBUG) {
            System.out.println(10 * CANVAS.getZoom());
        }
        for (int i = 0; i < amount; i++) {
            tmpList.add(CONTROLLER.getCurvePlot(i, (int) (10 * CANVAS.getZoom())));
        }
        if (amount >= 0) {
            SIDE_BAR.setCurveID(selected);
            SIDE_BAR.updateInfo(new String[]{CONTROLLER.getCurveName(selected), Double.toString(CONTROLLER.curveArea(selected)), Double.toString(CONTROLLER.curveLength(selected)), Integer.toString(CONTROLLER.getControlsPoints(selected).size()), Double.toString(CANVAS.getZoom())});
            SIDE_BAR.setNumberOfCurve(amount);
            CANVAS.setCurves(tmpList);
            if (DEBUG) {
                System.out.println("UpdatingG");
            }
        } else {
            System.out.println("Insufficient amount of curves");
        }
    }

    @Override
    public void handleCreate(GuiEventsCreate e) {
        if (DEBUG) {
            System.out.println("Creating");
        }
        selected++;
        if (e.getInfo().length == 3) {
            CONTROLLER.createCurve((int) e.getInfo()[2], e.getInfo()[0], e.getInfo()[1], e.getName());
            update();
        } else if (e.getInfo().length == 4) {
            CONTROLLER.createBowl(e.getInfo()[2], e.getName());
            update();
        } else if (e.getInfo().length == 7) {
            CONTROLLER.createEllipse(e.getInfo()[3], e.getInfo()[4], e.getInfo()[5], e.getInfo()[6], e.getName());
            update();
        } else {
            System.out.println("Error");
        }
    }

    @Override
    public void handleAdd(GuiEventsAdd e) {
        if (DEBUG) {
            System.out.println("Adding a new point");
        }
        CONTROLLER.addLastPoint(e.getInfo()[0], e.getInfo()[1], e.getCurveID());
        update();
    }

    @Override
    public void handleMove(GuiEventsMove e) {
        if (DEBUG) {
            System.out.println("Moving a point");
        }
        if (e.getSource().equals(CANVAS)) {
            CONTROLLER.setPointLocation(e.getInfo()[0], e.getInfo()[1], e.getCurveID(), e.getPointID());
        } else {
            CONTROLLER.setPointLocation(e.getInfo()[0], e.getInfo()[1], e.getCurveID(), e.getPointID());
        }
        update();
    }

    @Override
    public void handleDeleteP(GuiEventsDeleteP e) {
        if (DEBUG) {
            System.out.println("Deleting a point");
        }
        CONTROLLER.removePoint(e.getCurveID(), e.getPointID());
        update();
    }

    @Override
    public void handleDeleteC(GuiEventsDeleteC e) {
        if (DEBUG) {
            System.out.println("Deleting a curve");
        }
        System.out.println("Not supported yet.");
        update();
    }

    @Override
    public void handleClose(GuiEventsClose e) {
        if (DEBUG) {
            System.out.println("Closing Curve");
        }
        CONTROLLER.closeCurve(e.getCurveID());
        updateG();
    }

    @Override
    public void handleOpen(GuiEventsOpen e) {
        if (DEBUG) {
            System.out.println("Closing Curve");
        }
        CONTROLLER.openCurve(e.getCurveID());
        updateG();
    }

    @Override
    public void handleVisibility(GuiEventsVisibility e) {
        if (DEBUG) {
            System.out.println("Change Visiblity");
        }
        CANVAS.setVisiblity(e.getVisiablity());
    }

    @Override
    public void handleCurrent(GuiEventsCurrent e) {
        if (DEBUG) {
            System.out.println("Changing Current Line");
        }
        selected = e.getCurveID();
        if (!e.getSource().getClass().equals(CANVAS.getClass())) {
            CANVAS.setCurrentLine(e.getCurveID());
        } else {
            SIDE_BAR.setCurveID(e.getCurveID());
        }
        update();
    }

    @Override
    public void handleRefresh(GuiEventsRefresh e) {
        if (DEBUG) {
            System.out.println("Refreshing");
        }
        update();
    }

    @Override
    public void actionPerformed(GuiEvents e) {
        if (GuiEventsAdd.class.equals(e.getClass())) {
            handleAdd((GuiEventsAdd) e);
        } else if (GuiEventsClose.class.equals(e.getClass())) {
            handleClose((GuiEventsClose) e);
        } else if (GuiEventsMove.class.equals(e.getClass())) {
            handleMove((GuiEventsMove) e);
        } else if (GuiEventsCreate.class.equals(e.getClass())) {
            handleCreate((GuiEventsCreate) e);
        } else if (GuiEventsCurrent.class.equals(e.getClass())) {
            handleCurrent((GuiEventsCurrent) e);
        } else if (GuiEventsDeleteC.class.equals(e.getClass())) {
            handleDeleteC((GuiEventsDeleteC) e);
        } else if (GuiEventsDeleteP.class.equals(e.getClass())) {
            handleDeleteP((GuiEventsDeleteP) e);
        } else if (GuiEventsOpen.class.equals(e.getClass())) {
            handleOpen((GuiEventsOpen) e);
        } else if (GuiEventsVisibility.class.equals(e.getClass())) {
            handleVisibility((GuiEventsVisibility) e);
        } else if (GuiEventsRefresh.class.equals(e.getClass())) {
            handleRefresh((GuiEventsRefresh) e);
        } else {
            System.out.println("Not handled");
        }
    }

    @Override
    public void handleAreaChange(GuiEventAreaChange e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleLengthChange(GuiEventLengthChange e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
