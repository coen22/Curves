package curves;

import java.awt.geom.Point2D;

public class BezierSplineColinear extends BezierSpline {

    public BezierSplineColinear(String name) {
        super(name);
    }

    /**
     * Method to adjust the points
     * @param index of the moved point
     * @param relative horizontal movement
     * @param relative vertical movement
     */
    void adjustColinear(int i, double dx, double dy) {
        if (i % 3 == 0 && i < points.size() - 1) {
            // change neighbours position relative to this one
            points.get(getInBounds(i - 1)).setLocation(
                    points.get(getInBounds(i - 1)).getX() + dx,
                    points.get(getInBounds(i - 1)).getY() + dy);
            points.get(getInBounds(i + 1)).setLocation(
                    points.get(getInBounds(i + 1)).getX() + dx,
                    points.get(getInBounds(i + 1)).getY() + dy);
        } else if (i % 3 == 1 && points.size() > 4) {
            adjust(i, getInBounds(i - 1), getInBounds(i - 2)); // adjust the other control point
        } else if (i % 3 == 2 && points.size() > 4) {
            adjust(i, getInBounds(i + 1), getInBounds(i + 2)); // adjust the other control point
        }
    }

    /**
     * Method to adjust the opposite control point
     *
     * @param c1 first control point
     * @param knot point
     * @param c2 second control point which will be adjusted
     */
    void adjust(int c1, int knot, int c2) {
    	System.out.println("moved pointed = " + c1 + " , knot = " + knot + " , c2 = " + c2);
    	
        double ij = distance(points.get(c1).getX(),
        					points.get(c1).getY(),
        					points.get(knot).getX(),
        					points.get(knot).getY());
        double jk = distance(points.get(knot).getX(),
			        		points.get(knot).getY(),
			        		points.get(c2).getX(),
			        		points.get(c2).getY());
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
     *
     * @param the x of the new point
     * @param the y of the new point
     * @return the index of the new point
     */
    public int add(int x, int y) {
		points.add(new Point2D.Double(x, y));
		adjustColinear(points.size() - 1, 0, 0);
        return points.size() - 1;
    }
    
	protected void setPoint(int index, double x, double y) {
        // save old position to translate control points if needed
        double dx = x - points.get(index).getX();
        double dy = y - points.get(index).getY();
        super.setPoint(index, x, y);
        adjustColinear(index, dx, dy);
    }
    
    protected Point2D removePoint(int index) {
        super.removePoint(index);
        adjustColinear(index, 0, 0);

        // TODO make use of this in some way
        return null;
    }

}
