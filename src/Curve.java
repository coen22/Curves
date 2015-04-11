import java.awt.geom.Point2D;


public abstract class Curve {
	
	protected Point2D[] points;
	protected boolean closed;
	
	public Point2D[] plot() {
		return null;
	}
	
	public double length() {
		return 0;
	}

	public double area() {
		return 0;
	}
	
	public Point2D[] getPoints() {
		return points;
	}
	
	public void setPoints(Point2D[] points) {
		this.points = points;
	}
	
	public boolean getClosed() {
		return closed;
	}
	
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
}
