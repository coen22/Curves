package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/*
 * resource: http://pomax.github.io/bezierinfo/ 
 * 
 * B-Spline info:
 * 
 * intro: http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/spline/B-spline/bspline-basis.html
 * coefficients: http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/spline/B-spline/bspline-curve-coef.html
 * knot insertion: http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/spline/B-spline/single-insertion.html
 * multiple knot insertion: http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/spline/B-spline/multiple-time.html
 * DeBoors: http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/spline/B-spline/de-Boor.html
 * 
 */


public class BezierCurve extends Curve {
		
	private ArrayList<Double> Xcoefficients; 
	private ArrayList<Double> Ycoefficients; 
//	private ArrayList<Double> binomialCoefficients;
	
	public BezierCurve(Point2D point, String name){
		super(name);
//		binomialCoefficients = new ArrayList<Double>();
//		binomialCoefficients.add((double) 1);
		add(point.getX(), point.getY());
	}
	
	public void add(double x, double y){
		super.points.add(new Point2D.Double(x,y));
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
		ArrayList<Point2D> plottingPoints = new ArrayList<Point2D>();
		
		for (double d = 0; d < 1; d+=tInterval){
			plottingPoints.add(deCasteljauSAlgorithm(super.points, d));
		}
		plottingPoints.add(deCasteljauSAlgorithm(super.points, 1));
		
		return (List<Point2D>)plottingPoints;
	}
	
	/**
	 * Method which, without polynomial mathematics, calculates the point at a given t. Can be optimised to be seperate class (static), memory improvements possible. 
	 * @param originalPoints original points of the Bezier Curve
	 * @param ratio t at which the point shall be calculated
	 * @return the point for given t
	 */
	public static Point2D deCasteljauSAlgorithm(ArrayList<Point2D> originalPoints, double ratio){
		ArrayList<Point2D> working = new ArrayList<Point2D>();
		
		//calculates the first step and copies from the originalPoints to the working points set
		for (int i = 0; i < originalPoints.size()-1; i++){
			Double newX = originalPoints.get(i).getX() + ((originalPoints.get(i+1).getX() - originalPoints.get(i).getX()) * ratio);
			Double newY = originalPoints.get(i).getY() + ((originalPoints.get(i+1).getY() - originalPoints.get(i).getY()) * ratio);
			working.add(new Point2D.Double(newX, newY));
		}
		while (working.size() > 1){
			for (int i = 0; i < working.size()-1; i++){
				Double newX = working.get(i).getX() + ((working.get(i+1).getX() - working.get(i).getX()) * ratio);
				Double newY = working.get(i).getY() + ((working.get(i+1).getY() - working.get(i).getY()) * ratio);
				working.get(i).setLocation(newX, newY);;
			}
			working.remove(working.size()-1);
		}
		return working.get(0);
	}
	
	public String toString(){
		String string = "Bezier: ";
		for (int i = 0; i < super.points.size(); i++){
			string = string + "[" + super.points.get(i).getX() +","+ super.points.get(i).getY() + "] " ;
		}
		return string;
	}
}
