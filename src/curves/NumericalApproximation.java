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
	
	private static final int ROMBERG_MAX = 7;
	private static final int SIMPSON_N = 20;

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
		if (curve.arcLengthAlgorithm == ROMBERG_ARCLENGTH){
			return Math.abs(rombergArcLength(curve));
		}
		else if (curve.arcLengthAlgorithm == SIMPSON_ARCLENGTH){
			return simpsonArcLength(curve);
		}
		else if (curve.arcLengthAlgorithm == PYTHAGOREAN_ARCLENGTH){
			return pythagoreanLength(curve);
		}
		return 0.0;
	}
	
	private static double rombergArcLength(Curve curve) {
		CubicSpline local = (CubicSpline)curve;
		double tmpLength = 0;
		
		for (int i = 0; i < curve.points.size()-1; i++){
			tmpLength += local.rombergEvaluation(i, 0, 1, ROMBERG_MAX);
		}
		if (curve.isClosed()){
			tmpLength += local.rombergEvaluation(curve.points.size()-1, 0, 1, ROMBERG_MAX);
		}
		return tmpLength;
	}
	
	private static double simpsonArcLength(Curve curve) {
		CubicSpline local = (CubicSpline)curve;
		double tmpLength = 0;
		
		for (int i = 0; i < curve.points.size()-1; i++){
			tmpLength += local.simpsonEvaluation(i, 0, 1, SIMPSON_N);
		}
		if (curve.isClosed()){
			tmpLength += local.simpsonEvaluation(curve.points.size()-1, 0, 1, SIMPSON_N);
		}
		return tmpLength;
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

		if (!curve.isClosed()){
			return 0;
		}
			
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
		final int resolution = 15;
		
		ArrayList<Point2D> listOfPoints = curve.getPlot(resolution);

		if (listOfPoints.isEmpty() || listOfPoints.size() == 1) {
			return 0;
		}
		
		for (int i = 1; i < listOfPoints.size(); i++){
			length += Math.sqrt(Math.pow(listOfPoints.get(i).getX()
					- listOfPoints.get(i - 1).getX(), 2)
					+ Math.pow(listOfPoints.get(i).getY()- listOfPoints.get(i - 1).getY(), 2));
		}
			
		return length;
	}
}
