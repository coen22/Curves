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
    
    public List<Point2D> getCurvePlot(int index){
        return curves.get(index).getPlot();
    }

    public void createCurve(int TYPE, double x, double y, String name) {
        //created the constructor for polyline
        if(TYPE == 1){
            curves.add(new PolyLine(new Point2D.Double(x, y),name));
        }
    }

    public boolean closeCurve(int CURVE_ID) {
        return false;
    }

    public boolean openCurve(int CURVE_ID) {
        return false;
    }

    public int getCurveID(Curve curve) {
        return 0;
    }

    public void addLastPoint(double x, double y, int CURVE_ID) {

    }

    public void addPoint(double x, double y, int CURVE_ID, int index) {

    }

    public void translate(double deltaX, double deltaY, int CURVE_ID, int POINT_ID) {

    }

}
