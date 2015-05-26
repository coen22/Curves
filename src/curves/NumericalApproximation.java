package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NumericalApproximation {
	
	private static final boolean DEBUG = false;
	
	public static final int SHOELACE_AREA = 1;
	public static final int EXACT_AREA_CUBIC = 2;
	public static final int EXACT_ELLIPSE_AREA = 3;
	public static final int RICHARDSON_EXTRAPOLATION_AREA = 4;
	
	public static final int ROMBERG_ARCLENGTH = 1;
	public static final int SIMPSON_ARCLENGTH = 2;
	public static final int PYTHAGOREAN_ARCLENGTH = 3;
	public static final int RICHARDSON_EXTRAPOLATION_ARCLENGTH = 4;
	public static final int ELLIPSE_ARCLENGTH_EXACT = 5;
	
	private static final int ROMBERG_MAX = 8;
	private static final int SIMPSON_N = 30;
	private static final int RICHARDSON_N = 10;
	private static final int POLYLINE_APPROXIMATION_SUBDIVISIONS = 10;
	
	private static Evaluateable localCurve;

	/**
	 * calculates the area of the curve based solely on the curve as input
	 * @param curve
	 * @return area
	 */
	public static double calcArea(Curve curve){
		if (DEBUG) System.out.println("algorithm: " + curve.areaAlgorithm);
		if (curve.areaAlgorithm == EXACT_AREA_CUBIC){
			if (DEBUG) System.out.println("exact area");
			return Math.abs(exactCubicArea(curve));
		}
		else if (curve.areaAlgorithm == SHOELACE_AREA){
			if (DEBUG)System.out.println("shoelace");
			return Math.abs(shoeLaceArea(curve));
		}
		else if (curve.areaAlgorithm == EXACT_ELLIPSE_AREA){
			if (DEBUG)System.out.println("ellipse area");
			return Math.abs(exactEllipseArea(curve));
		}
		else if (curve.areaAlgorithm == RICHARDSON_EXTRAPOLATION_AREA){
			if (DEBUG)System.out.println("richardson area");
			return Math.abs(richardsonArea(curve));
		}
		return Double.NaN;
	}
	
	/**
	 * calculates the arc-length of the curve, based solely on the curve as input
	 * @param curve
	 * @return arc-length
	 */
	public static double calcArcLength(Curve curve){
		if (curve.arcLengthAlgorithm == ROMBERG_ARCLENGTH){
			if (DEBUG)System.out.println("romberg length using exact formula");
			return Math.abs(rombergArcLength(curve));
		}
		else if (curve.arcLengthAlgorithm == SIMPSON_ARCLENGTH){
			if (DEBUG)System.out.println("simpson length using exact formula");
			return simpsonArcLength(curve);
		}
		else if (curve.arcLengthAlgorithm == PYTHAGOREAN_ARCLENGTH){
			if (DEBUG)System.out.println("pythoagoras");
			return pythagoreanLength(curve, POLYLINE_APPROXIMATION_SUBDIVISIONS);
		}
		else if (curve.arcLengthAlgorithm == RICHARDSON_EXTRAPOLATION_ARCLENGTH){
			if (DEBUG)System.out.println("richardson length");
			return richardsonLength(curve);
		}
		else if (curve.arcLengthAlgorithm == ELLIPSE_ARCLENGTH_EXACT){
			if (DEBUG)System.out.println("ellipse length");
			return exactEllipseLength(curve);
		}
		return Double.NaN;
	}
	
	/**
	 * specific method for the "evaluateable" functions, hence the arc-length equation can be evaluated. Uses romberg integration  
	 * @param curve
	 * @return the area
	 */
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
	
	/**
	 * this method performs the romberg integration for arc-length on each actual piece of the spline
	 * @param piece is the index of the piece to be used
	 * @param lower bound
	 * @param higher higher bound
	 * @param n romberg limit, such as Rn,n
	 * @return arc-length of piece
	 */
	private static double rombergEvaluation(int piece, double lower, double higher, int n){
		
		double[][] rombergMatrix = new double[n][n];
		
		for (int i = 0; i < n; i++){
			rombergMatrix[i][0] = trapezoidArcLength(piece, lower, higher, (int)Math.pow(2, i));
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
	
	/**
	 * this method actually evaluates the arc-length function and uses the trapezoidal method with n sub-divisions
	 * @param piece index
	 * @param lower bound
	 * @param higher bound
	 * @param n number of sub-divisions
	 * @return individual arc-length with n sub-divisions
	 */
	private static double trapezoidArcLength(int piece, double lower, double higher, int n){
		double h = (higher-lower) / n;
		double sum = 0;
		
		sum += 0.5 * localCurve.evaluateArcLengthFunction(piece, lower);
		sum += 0.5 * localCurve.evaluateArcLengthFunction(piece, higher);
		for (int i = 1; i <= (n-1); i++){
			sum += localCurve.evaluateArcLengthFunction(piece, lower + (h*i));
		}
		return sum*h;
	}
	
	/**
	 * 
	 * @param curve
	 * @return
	 */
	private static double richardsonArea(Curve curve) {
		double[][] richardsonMatrix = new double[RICHARDSON_N][RICHARDSON_N];
		
		for (int i = 0; i < RICHARDSON_N; i++){
			richardsonMatrix[i][0] = polyLineArea(curve, (int)Math.pow(2, i));
		}
		
		for (int i = 1; i < RICHARDSON_N; i++){
			for (int k = i; k < RICHARDSON_N; k++){
				richardsonMatrix[k][i] = richardsonMatrix[k][i-1] + ((richardsonMatrix[k][i-1]-richardsonMatrix[k-1][i-1])/(Math.pow(2, i)-1));
			}
		}
		
		return richardsonMatrix[RICHARDSON_N-1][RICHARDSON_N-1];
	}
	
	private static double polyLineArea(Curve curve, int n) {
		ArrayList<Point2D> plot = curve.getPlot(n);
		double area = 0;
		for (int i = 0; i < plot.size()-1; i++){
			area += pointWiseArea(plot.get(i), plot.get(i+1));
		}
		return Math.abs(area);
	}

	private static double pointWiseArea(Point2D point1, Point2D point2) {
		double subArea = 0;
		
		double x1 = point1.getX();
		double x2 = point2.getX();
		double y1 = point1.getY();
		double y2 = point2.getY();
		
		double xdiff = x2-x1;
		double midPointY = (y1+y2)/2;
		
		
		if (x1 == x2){ //the x-values are the same, hence the area = 0
			return subArea;
		}
		else if (Math.signum(y1) == Math.signum(y2)){ //both points are either above or below the x-axis
			if (Math.signum(y1) == 1){
				subArea = xdiff*midPointY;
			}
			else if (Math.signum(y1) == -1){
				subArea = xdiff*midPointY;
			}
		}
		else if (y1 == 0 || y2 == 0){ //one of the points is on the x-axis
			if (Math.signum(y1) == 1){
				subArea = xdiff*midPointY;
			}
			else if (Math.signum(y1) == -1){
				subArea = xdiff*midPointY;
			}
			else if (Math.signum(y2) == 1){
				subArea = xdiff*midPointY;
			}
			else if (Math.signum(y2) == -1){
				subArea = xdiff*midPointY;
			}
		}
		else { // one point is above, and one point is below the x-axis
			double m = (y2-y1)/xdiff;
			double c = y1 - x1*m;
			double x3 = -c/m;
			
			double xdiff1 = x3-x1;
			double midPointY1 = (y1)/2;
			
			double xdiff2 = x2-x3;
			double midPointY2 = (y2)/2;
			
			if (Math.signum(y1) == 1) {
				subArea += xdiff1*midPointY1;
				subArea += -xdiff2*midPointY2;
			}
			else {
				subArea += -xdiff1*midPointY1;
				subArea += xdiff2*midPointY2;
			}
		}
		
		return subArea;
	}

	private static double richardsonLength(Curve curve) {
		double[][] richardsonMatrix = new double[RICHARDSON_N][RICHARDSON_N];
		
		for (int i = 0; i < RICHARDSON_N; i++){
			richardsonMatrix[i][0] = pythagoreanLength(curve, (int)Math.pow(2, i));
		}
		
		for (int i = 1; i < RICHARDSON_N; i++){
			for (int k = i; k < RICHARDSON_N; k++){
				richardsonMatrix[k][i] = richardsonMatrix[k][i-1] + ((richardsonMatrix[k][i-1]-richardsonMatrix[k-1][i-1])/(Math.pow(2, i)-1));
			}
		}
		
		return richardsonMatrix[RICHARDSON_N-1][RICHARDSON_N-1];
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
			return Double.NEGATIVE_INFINITY;
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
	
	private static double exactEllipseLength(Curve curve){
		Ellipse local = (Ellipse)curve;
		
		//Length of an ellipse calculated exactly using an "infinite sum" formula
		double vR = local.calc_vR();
		double hR = local.calc_hR();
		double x = (Math.pow((hR - vR), 2))/(Math.pow((vR + hR), 2));
		return Math.PI*(hR + vR)*(1 + (1/4)*x + (1/64)*Math.pow(x, 2) + (1/256)*Math.pow(x, 3) + (25/16384)*Math.pow(x, 4));
	}
	
	private static double exactEllipseArea(Curve curve){
		Ellipse local = (Ellipse)curve;
		return Math.PI*local.calc_hR()*local.calc_vR();
	}
}
