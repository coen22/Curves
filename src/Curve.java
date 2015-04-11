import java.awt.geom.Point2D;


public abstract class Curve {
	public Point2D[] points;
	
	public Point2D[] plot() {
		return null;
	}
	
	public double length() {
		return 0;
	}

	public double area() {
		return 0;
	}
}
