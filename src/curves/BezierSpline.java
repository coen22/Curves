package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BezierSpline extends Curve {

	public BezierSpline(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/** gives a plot of the Bezier to draw
	 * @param the accuracy at which curve needs to be plotted
	 * @return the list of points to plot
	 */
	public ArrayList<Point2D> getPlot(int interval) {
		ArrayList<Point2D> plot = new ArrayList<Point2D>();

		for (int i = 0; i < points.size() - 2; i += 3) {
			for (int j = 1; j <= interval; j++) {
				plot.add(calculatePoint(i, j / (float) interval));
			}
		}
		
		return plot;
	}

	/**
	 * get point at line piece i, at interval t
	 * @param line piece
	 * @param interval
	 * @return a point on the curve
	 */
	protected Point2D calculatePoint(int i, float t) {
		double px = 0;
		double py = 0;
		
		for (int j = 0; j <= 3; j++) {
			px += b(j, t) * points.get(getInBounds(i + j)).getX();
			py += b(j, t) * points.get(getInBounds(i + j)).getY();
		}
		
		return new Point2D.Double((int) Math.round(px), (int) Math.round(py));
	}

	/**
	 * the basic functions of the Bezier Spline
	 */
	protected double b(int i, double t) {
		switch (i) {
			case 0:
				return (1 - t) * (1 - t) * (1 - t);
			case 1:
				return 3 * t * (1 - t) * (1 - t);
			case 2:
				return 3 * t * t * (1 - t);
			case 3:
				return t * t * t;
			default:
				return 0; // something went wrong
		}
	}

	protected int getInBounds(int i) {
		if (i < 0)
			i = points.size() - i;

		return i % points.size();
	}
}
