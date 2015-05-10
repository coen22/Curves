package curves;

import java.awt.geom.Point2D;

public class SemiCircle extends CubicSpline {

	private double radius;
	private double intervals;
	
	public SemiCircle(Point2D point, String name, int type) {
		super(point, name, type);
		points.clear();
		
		radius = 10;
		intervals = 10;
		double delta = radius * 2 / intervals;
		
		for (int i = 0; i <= intervals; i++)
			points.add(new Point2D.Double(-radius + delta * i, circleFunction(-radius + delta * i)));
		
		for (Point2D p : points) {
			System.out.println(p);
		}
		
		update();
	}
	
	/**
	 * Basic circle
	 * @param the x for the basic circle function
	 * @return the y for the basic circle function
	 */
	private double circleFunction(double x) {
		return -Math.sqrt(radius*radius - x*x);
	}
	
	public static void main(String[] args) {
		SemiCircle s = new SemiCircle(new Point2D.Double(0,0), "tst", 0);
	}
}
