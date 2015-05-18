package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NumericalApproximation {
	public static final int SHOELACE_AREA = 1;
	public static final int EXACT_AREA_CUBIC = 2;
	public static final int ROMBERG_ARCLENGTH = 1;
	public static final int SIMPSON_ARCLENGTH = 2;
	public static final int PYTHAGOREAN_ARCLENGTH = 3;
	public static final int RICHARDSON_EXTRAPOLATION_ARCLENGTH = 4;
	
	private static final int ROMBERG_MAX = 8;
	private static final int SIMPSON_N = 30;
	
	private static Evaluateable localCurve;

	public static double calcArea(Curve curve){
		if (curve.areaAlgorithm == EXACT_AREA_CUBIC){
//			System.out.println("exact area");
			return Math.abs(exactCubicArea(curve));
		}
		else if (curve.areaAlgorithm == SHOELACE_AREA){
//			System.out.println("shoelace");
			return Math.abs(shoeLaceArea(curve));
		}
		return Double.NaN;
	}
	
	public static double calcArcLength(Curve curve){
		if (curve.arcLengthAlgorithm == ROMBERG_ARCLENGTH){
//			System.out.println("romberg");
			return Math.abs(rombergArcLength(curve));
		}
		else if (curve.arcLengthAlgorithm == SIMPSON_ARCLENGTH){
//			System.out.println("simpson");
			return simpsonArcLength(curve);
		}
		else if (curve.arcLengthAlgorithm == PYTHAGOREAN_ARCLENGTH){
//			System.out.println("pythoagoras");
			return pythagoreanLength(curve, 10);
		}
		else if (curve.arcLengthAlgorithm == RICHARDSON_EXTRAPOLATION_ARCLENGTH){
//			System.out.println("richardson");
			return richardsonExtrapolation(curve, 10);
		}
		return Double.NaN;
	}
	
	private static double rombergArcLength(Curve curve) {
		localCurve = (Evaluateable)curve;
		double tmpLength = 0;
		
		for (int i = 0; i < curve.points.size()-1; i++){
			tmpLength += rombergEvaluation(i, 0, 1, ROMBERG_MAX);
		}
		if (curve.isClosed()){
			tmpLength += rombergEvaluation(curve.points.size()-1, 0, 1, ROMBERG_MAX);
		}
		return tmpLength;
	}
	
	
	private static double rombergEvaluation(int piece, double lower, double higher, int n){
		
		double[][] rombergMatrix = new double[n][n];
		
		for (int i = 0; i < n; i++){
			rombergMatrix[i][0] = trapezoidEvaluation(piece, lower, higher, (int)Math.pow(2, i));
		}
		double pow;
		for (int i = 1; i < n; i++){
			for (int k = i; k < n; k++){
				pow = Math.pow(4, i);
				rombergMatrix[k][i] = (pow/(pow-1))*rombergMatrix[k][i-1] - (1/(pow-1))*rombergMatrix[k-1][i-1];
			}
		}
		return rombergMatrix[n-1][n-1];
	}
	
	public static double richardsonExtrapolation(Curve curve, int n) {
		double[][] richardsonMatrix = new double[n][n];
		
		for (int i = 0; i < n; i++){
			richardsonMatrix[i][0] = pythagoreanLength(curve, (int)Math.pow(2, i));
		}
		
		for (int i = 1; i < n; i++){
			for (int k = i; k < n; k++){
				richardsonMatrix[k][i] = richardsonMatrix[k][i-1] + ((richardsonMatrix[k][i-1]-richardsonMatrix[k-1][i-1])/(Math.pow(2, i)-1));
			}
		}
		
		return richardsonMatrix[n-1][n-1];
	}
	
	private static double trapezoidEvaluation(int piece, double lower, double higher, int n){
		double h = (higher-lower) / n;
		double sum = 0;
		
		sum += 0.5 * localCurve.evaluateArcLengthFunction(piece, lower);
		sum += 0.5 * localCurve.evaluateArcLengthFunction(piece, higher);
		for (int i = 1; i <= (n-1); i++){
			sum += localCurve.evaluateArcLengthFunction(piece, lower + (h*i));
		}
		return sum*h;
	}
	
	private static double simpsonArcLength(Curve curve) {
		localCurve = (Evaluateable)curve;
		double tmpLength = 0;
		
		for (int i = 0; i < curve.points.size()-1; i++){
			tmpLength += simpsonEvaluation(i, 0, 1, SIMPSON_N);
		}
		if (curve.isClosed()){
			tmpLength += simpsonEvaluation(curve.points.size()-1, 0, 1, SIMPSON_N);
		}
		return tmpLength;
	}
	
	private static double simpsonEvaluation(int piece, double lower, double higher, int n){
		double h = (higher-lower) / n;
		double sum = 0;
		
		sum += localCurve.evaluateArcLengthFunction(piece, lower);
		sum += localCurve.evaluateArcLengthFunction(piece, higher);
		
		for (int i = 1; i < n; i+=2){
			sum += 4*localCurve.evaluateArcLengthFunction(piece, (lower + (i * h)));
		}
		
		for (int i = 2; i < n; i+=2){
			sum += 2*localCurve.evaluateArcLengthFunction(piece, (lower + (i * h)));
		}
		
		return (sum * (h/3));
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
			return Double.POSITIVE_INFINITY;
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
	
	private static double pythagoreanLength(Curve curve, int subDivisions) {
		double length = 0;
		
		if (subDivisions < 1){
			subDivisions = 1;
		}
		
		ArrayList<Point2D> listOfPoints = curve.getPlot(subDivisions);
		
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
