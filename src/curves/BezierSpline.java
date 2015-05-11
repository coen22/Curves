package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BezierSpline extends Curve {

	public BezierSpline(String name) {
		super(name);
	}

	@Override
	public ArrayList<Point2D> getPlot(int interval) {
		ArrayList<Point2D> plot = new ArrayList<Point2D>();

		plot.add(points.get(0));
		
		for (int i = 0; i < points.size() - ((closed) ? 2 : 3); i += 3) {
			for (int j = 1; j <= interval; j++) {
				plot.add(calculatePoint(i, j / (float) interval));
			}
		}

		return plot;
	}

	/**
	 * get point at line piece i, at interval t
	 *
	 * @param line piece
	 * @param interval
	 * @return a point on the curve
	 */
	protected Point2D calculatePoint(int i, float t) {
		double px = 0;
		double py = 0;
		
		for (int j = 0; j <= 3; j++) {
			px += basic(j, t) * points.get(getInBounds(i + j)).getX();
			py += basic(j, t) * points.get(getInBounds(i + j)).getY();
		}

		return new Point2D.Double(px, py);
	}

	/**
	 * the basic functions of the Bezier Spline
	 */
	protected double basic(int i, double t) {
		switch (i) {
		case 0:
			return (1 - t) * (1 - t) * (1 - t);
		case 1:
			return 3 * t * (1 - t) * (1 - t);
		case 2:
			return 3 * t * t * (1 - t);
		case 3:
			return t * t * t;
		}
		
		return 0; // something went wrong
	}

	/**
	 * method to make sure that a point is inside the domain
	 * @param index of a point
	 * @return index of that point inside the domain
	 */
	int getInBounds(int i) {
		if (points.size() == 0)
			return 0;
		
		if (i < 0)
			i = points.size() + i;

		return i % points.size();
	}
	
	//Coen, you make sure this returns the right points, that's YOUR responsibility. 
	@Override
	protected List<Point2D> getConversionPoints() {
		return null;
	}
}
