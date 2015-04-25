package curves;

import java.awt.geom.Point2D;

public class BezierSplineColinear extends BezierSpline {

	double deltax;
	double deltay;
	
	public BezierSplineColinear(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	void adjustColinear(int i) {
		if (i % 3 == 0 && i < points.size() - 1) {
			// change neighbours position relative to this one
			points.get(getInBounds(i - 1)).setLocation(
					points.get(getInBounds(i - 1)).getX() + deltax,
					points.get(getInBounds(i - 1)).getY() + deltay);
			points.get(getInBounds(i + 1)).setLocation(
					points.get(getInBounds(i + 1)).getX() + deltax,
					points.get(getInBounds(i + 1)).getY() + deltay);
		} else if (i % 3 == 1 && points.size() > 4)
			adjust(i, getInBounds(i - 1), getInBounds(i - 2)); // adjust the other control point
		else if (i % 3 == 2 && points.size() > 4)
			adjust(i, getInBounds(i + 1), getInBounds(i + 2)); // adjust the other control point
	}

	/**
	 * Method to adjust the opposite control point
	 * @param c1 first control point
	 * @param knot 
	 * @param c2 second control point which will be adjusted
	 */
	void adjust(int c1, int knot, int c2) {
		double ij = distance(points.get(c1).getX(), points.get(c1).getY(), points
				.get(knot).getX(), points.get(c1).getY());
		double jk = distance(points.get(knot).getX(), points.get(knot).getX(), points
				.get(c2).getX(), points.get(c2).getY());
		double r = jk / ij;

		points.get(c2).setLocation(
				points.get(knot).getX() + r * (points.get(knot).getX() - points.get(c1).getX()),
				points.get(knot).getY() + r * (points.get(knot).getY() - points.get(c1).getY()));
	}

	
	double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	/**
	 * add an extra point then adjust the other control points
	 * @param the x of the new point
	 * @param the y of the new point
	 * @return the index of the new point
	 */
	public int add(int x, int y) {
		int i = super.add(x, y);
		adjustColinear(i);
		return i;
	}

	/**
	 * set selected control point */
    protected void setPointLocation(int index, double x, double y) {
		// save old position to translate control points if needed
		deltax = x - points.get(index).getX();
		deltay = y - points.get(index).getY();
		super.setPointLocation(index, x, y);
		adjustColinear(index);
	}

	/** remove selected control point */
	protected Point2D removePoint(int index) {
		super.removePoint(index);
		adjustColinear(index);
		
		// TODO make use of this in some way
		return null;
	}

}
