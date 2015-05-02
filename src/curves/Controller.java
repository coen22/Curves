package curves;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;

public class Controller {

    public static final int POLYLINE = 1;
    public static final int CUBIC_N = 21;
    public static final int BEZIERCURVE = 31;
    public static final int BEZIERSPLINE = 32;
    public static final int BEZIERSPLINECOLINEAR = 33;
    private ArrayList<Curve> curves = new ArrayList<Curve>();

    public Controller() {
        curves = new ArrayList<Curve>();
    }

    public int amountOfCurves() {
        return curves.size();
    }

    public String getCurveName(int index) {
        return curves.get(index).getName();
    }

    public String curveName(int index) {
        return curves.get(index).getName();
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
        if (TYPE == POLYLINE) {
            curves.add(new PolyLine(new Point2D.Double(x, y), name));
            System.out.println("Polyline Created");
        } else if (TYPE == CUBIC_N) {
            curves.add(new CubicSpline(new Point2D.Double(x, y), name, CubicSpline.NATURAL_SPLINE));
            System.out.println("CubicLine Created");
        } else if (TYPE == BEZIERCURVE) {
            curves.add(new BezierCurve(new Point2D.Double(x, y), name));
            System.out.println("Bezier Curve Created");
        } else if (TYPE == BEZIERSPLINE) {
            curves.add(new BezierSpline(name));
            curves.get(curves.size() - 1).add(x, y);
            System.out.println("Bezier Spline Created");
        } else if (TYPE == BEZIERSPLINECOLINEAR) {
            curves.add(new BezierSplineColinear(name));
            curves.get(curves.size() - 1).add(x, y);
            System.out.println("Bezier Spline Colinear Created");
        }
    }

    public Point2D removePoint(int CurveID, int PointID) {
        return curves.get(CurveID).removePoint(PointID);
    }

    public void createCurve(int TYPE, double x, double y, String name, int cubicType) {
        //created the constructor for polyline
        if (TYPE == POLYLINE) {
            curves.add(new PolyLine(new Point2D.Double(x, y), name));
            System.out.println("Polyline Created");
        } else if (TYPE == CUBIC_N) {
            curves.add(new CubicSpline(new Point2D.Double(x, y), name, cubicType));
            System.out.println("CubicLine Created");
        } else if (TYPE == BEZIERCURVE) {
            curves.add(new BezierCurve(new Point2D.Double(x, y), name));
            System.out.println("Bezier Curve Created");
        } else if (TYPE == BEZIERSPLINE) {
            curves.add(new BezierSpline(name));
            curves.get(curves.size() - 1).add(x, y);
            System.out.println("Bezier Spline Created");
        } else if (TYPE == BEZIERSPLINECOLINEAR) {
            curves.add(new BezierSplineColinear(name));
            curves.get(curves.size() - 1).add(x, y);
            System.out.println("Bezier Spline Colinear Created");
        }
    }

    public boolean closeCurve(int curveIndex) {
        curves.get(curveIndex).setClosed(true);
        return curves.get(curveIndex).isClosed();
    }

    public boolean openCurve(int curveIndex) {
        curves.get(curveIndex).setClosed(false);
        return curves.get(curveIndex).isClosed();
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

    public void setPointLocation(double x, double y, int curveIndex, int pointIndex) {
        curves.get(curveIndex).setPoint(pointIndex, x, y);
    }

    public List<Point2D> getControlsPoints(int curveIndex) {
        return curves.get(curveIndex).getControlPoints();
    }

}
