package curves;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;

public class Controller {
    public static final int POLYLINE = 1;
    private ArrayList<Curve> curves = new ArrayList<Curve>();

    public Controller() {
        curves = new ArrayList<Curve>();
    }
    
    /**
     * Method to retrieve the plotting coordinates of each curve. NOTE THE DEFINITION OF SUB-POINTS!
     * @param curveIndex index of which curve to retrieve
     * @param subPoints this number signifies the number of plotting points in-between each pair of control-points. The larger the number, the more fine-grained the curve plot will be.
     * @return
     */
    public List<Point2D> getCurvePlot(int curveIndex, int subPoints) {
        return curves.get(curveIndex).getPlot(subPoints);
    }

    public void createCurve(int TYPE, double x, double y, String name) {
        //created the constructor for polyline
        if(TYPE == 1){
            curves.add(new PolyLine(new Point2D.Double(x, y),name));
        }
    }

    public boolean closeCurve(int curveIndex) {
        return false;
    }

    public boolean openCurve(int curveIndex) {
        return false;
    }

    public int getCurveID(Curve curve) {
        return 0;
    }

    public void addLastPoint(double x, double y, int curveIndex) {

    }

    public void addPoint(double x, double y, int curveIndex, int index) {

    }

    public void translate(double deltaX, double deltaY, int CURVE_ID, int POINT_ID) {

    }

}
