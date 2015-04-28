package curves;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;

public class Controller {

    public static final int POLYLINE = 1;
    public static final int CUBIC_N = 2;
    public static final int BEZIERSPLINE = 31;

    private ArrayList<Curve> curves = new ArrayList<Curve>();

    public Controller() {
        curves = new ArrayList<Curve>();
    }

    public int amountOfCurves() {
        return curves.size();
    }

    /**
     * Method to retrieve the plotting coordinates of each curve. NOTE THE
     * DEFINITION OF SUB-POINTS!
     *
     * @param curveIndex index of which curve to retrieve
     * @param subPoints this number signifies the number of plotting points
     * in-between each pair of control-points. The larger the number, the more
     * fine-grained the curve plot will be.
     * @return
     */
    public ArrayList<Point2D> getCurvePlot(int curveIndex, int subPoints) {
        return curves.get(curveIndex).getPlot(subPoints);
    }

    public void createCurve(int TYPE, double x, double y, String name) {
        //created the constructor for polyline
        if (TYPE == 1) {
            curves.add(new PolyLine(new Point2D.Double(x, y), name));
            System.out.println("sdklgsjdsl");
        } else if (TYPE == 2) {
            curves.add(new CubicSpline(new Point2D.Double(x, y), name, CubicSpline.NATURAL_SPLINE));

        }
    }

    public Point2D removePoint(int CurveID, int PointID) {
        //TO DO
        return null;
    }

    public void createCurve(int TYPE, double x, double y, String name, int cubicType) {
        //created the constructor for polyline
        if (TYPE == POLYLINE) {
            curves.add(new PolyLine(new Point2D.Double(x, y), name));
        } else if (TYPE == CUBIC_N) {
            curves.add(new CubicSpline(new Point2D.Double(x, y), name, cubicType));
        } else if (TYPE == BEZIERSPLINE) {
            curves.add(new BezierSpline(name));
            curves.get(curves.size()-1).add(x, y);

        }
    }

    public boolean closeCurve(int curveIndex) {
        curves.get(curveIndex).setClosed(true);
        return curves.get(curveIndex).isClosed();
    }

    public boolean openCurve(int curveIndex) {
        return false;
    }

    public int getCurveID(Curve curve) {
        return 0;
    }

    public void addLastPoint(double x, double y, int curveIndex) {
        System.out.println("Adding last point");
        curves.get(curveIndex).add(x, y);
    }

    public void addPoint(double x, double y, int curveIndex, int index) {

    }

    public void translate(double deltaX, double deltaY, int curveIndex, int pointIndex) {
        //remove
    }

    public void setPointLocation(double x, double y, int curveIndex, int pointIndex) {
        curves.get(curveIndex).setPoint(pointIndex, x, y);
    }

    public List<Point2D> getControlsPoints(int curveIndex) {
        return curves.get(curveIndex).getControlPoints();
    }

}
