import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BezierCurve extends Curve {
	
	private ArrayList<Double> Xcoefficients; 
	private ArrayList<Double> Ycoefficients; 
	
	public BezierCurve(Point2D point, String name){
		super(name);
		super.add(point.getX(), point.getY());
	}

	public double length() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double area() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Point2D> calcPlotingCoordinates(double tInterval) {
		return null;
	}
}
