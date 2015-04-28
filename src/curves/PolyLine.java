package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PolyLine extends Curve {
		
	protected PolyLine(Point2D point, String name){
		super(name);
		super.points.add(point);
	}
	
    protected ArrayList<Point2D> getPlot(int interval) {
    	return super.getPlot(interval);
    }
	
//	public double length() {
//		double length = 0;
//		
//		for (int i = 0; i < points.length - 1; i++)
//			length += Math.sqrt(Math.pow(points[i].getX() - points[i - 1].getX(), 2) +
//								Math.pow(points[i].getY() - points[i - 1].getY(), 2));
//		
//		if (closed)
//			length += Math.sqrt(Math.pow(points[0].getX() - points[points.length - 1].getX(), 2) +
//								Math.pow(points[0].getY() - points[points.length - 1].getY(), 2));
//		
//		return length;
//	}

//	public double area() {
//		if (!closed)
//			return 0;
//		
//		double area = 0;
//        
//		for (int i = 0; i < points.length - 1; i++) {
//            area += points[i].getX() * points[i + 1].getY() - points[i + 1].getX() * points[i].getY();
//		}
//	
//		area += points[points.length - 1].getX() * points[0].getY() -
//				points[0].getX() * points[points.length - 1].getY();
//        
//        return area / 2;
//	}
}
