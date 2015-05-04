package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class NumericalApproximation {
	public static final int SHOELACE_AREA = 1;
	public static final int EXACT_AREA_CUBIC = 2;
	public static final int ROMBERG_ARCLENGTH = 1;
	public static final int SIMPSON_ARCLENGTH = 2;
	public static final int PYTHAGOREAN_ARCLENGTH = 3;

	public static double calcArea(Curve curve){
		if (curve.areaAlgorithm == EXACT_AREA_CUBIC){
			return Math.abs(exactCubicArea(curve));
		}
		else if (curve.areaAlgorithm == SHOELACE_AREA){
			return Math.abs(shoeLaceArea(curve));
		}
		return 0.0;
	}
	
	public static double calcArcLength(Curve curve){
		
		return 0.0;
	}
	
	private static double exactCubicArea(Curve curve){
		CubicSpline local = (CubicSpline)curve;
		
		int cutoff = curve.numberOfPoints()-1;
		if (curve.isClosed()){
			cutoff+= 1;
		}
		
		double finalArea = 0;
		
		for (int i = 0; i < cutoff; i++){
			finalArea += local.calcSubArea(i, 0.0, 1.0);
		}
		
		return finalArea;
	}
	
	private static  double shoeLaceArea(Curve curve) {
		double area = 0;

		if (!curve.closed)
			return 0;

		ArrayList<Point2D> listOfPoints = curve.getPlot(curve.numberOfPoints());
		for (int i = 0; i < listOfPoints.size() - 1; i++) {
			area += listOfPoints.get(i).getX() * listOfPoints.get(i + 1).getY()
					- listOfPoints.get(i + 1).getX()
					* listOfPoints.get(i).getY();
		}
		area += listOfPoints.get(listOfPoints.size() - 1).getX()
				* listOfPoints.get(0).getY() - listOfPoints.get(0).getX()
				* listOfPoints.get(listOfPoints.size() - 1).getY();
		
		return Math.abs(area / 2);
	}
	
	private static double pythagoreanLength(Curve curve) {
		double length = 0;
		ArrayList<Point2D> listOfPoints = curve.getPlot(curve.numberOfPoints());

		if (listOfPoints.isEmpty() || listOfPoints.size() == 1) {
			return 0;
		}
		for (int i = 1; i < listOfPoints.size() - 1; i++){
			length += Math.sqrt(Math.pow(listOfPoints.get(i).getX()
					- listOfPoints.get(i - 1).getX(), 2)
					+ Math.pow(listOfPoints.get(i).getY()- listOfPoints.get(i - 1).getY(), 2));
		}
			
		if (curve.closed)
			length += Math.sqrt(Math.pow(listOfPoints.get(0).getX()
					- listOfPoints.get(listOfPoints.size() - 1).getX(), 2)
					+ Math.pow(listOfPoints.get(0).getY()- listOfPoints.get(listOfPoints.size() - 1).getY(), 2));
		return length;
	}
}
