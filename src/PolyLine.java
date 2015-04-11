import java.awt.geom.Point2D;

public class Polyline extends Curve {
	
	public Point2D[] plot() {
		return points;
	}
	
	public double length() {
		double length = 0;
		
		for (int i = 0; i < points.length - 1; i++)
			length += Math.sqrt(Math.pow(points[i].getX() - points[i - 1].getX(), 2) +
								Math.pow(points[i].getY() - points[i - 1].getY(), 2));
		
		if (closed)
			length += Math.sqrt(Math.pow(points[0].getX() - points[points.length - 1].getX(), 2) +
								Math.pow(points[0].getY() - points[points.length - 1].getY(), 2));
		
		return length;
	}

	public double area() {
		if (!closed)
			return 0;
		
		double area = 0;
        
		for (int i = 0; i < points.length - 1; i++) {
            area += points[i].getX() * points[i + 1].getY() - points[i + 1].getX() * points[i].getY();
		}
	
		area += points[points.length - 1].getX() * points[0].getY() -
				points[0].getX() * points[points.length - 1].getY();
        
        return area / 2;
	}
}
