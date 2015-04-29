package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public class CubicSpline extends Curve {
	public static final int NATURAL_SPLINE = 1;
	public static final int CLAMPED_SPLINE = 2;
	public static final int NOT_A_KNOT_SPLINE = 3;
	public static final int CLOSED_SPLINE = 4;
	
	private static final double EPSILON = 1e-10;
	
	private double[][] Xcoefficients;
	private double[][] Ycoefficients;
	private double[][] dXcoefficients;
	private double[][] dYcoefficients;
	
	private int type;
	private ArrayList<Point2D> plot;
	private int divisions;

	public CubicSpline(Point2D point, String name, int type){
		super(name);
		add(point.getX(), point.getY());
		this.type = type;
		divisions = 1;
		update();
	}
	
	private void update() {
		calcCoefficients();
		calcPlot(divisions);
	}
	
        @Override
	protected void setPoint(int index, double x, double y) {
		super.setPoint(index, x, y);
		update();
	}
	
	public void setClosed(boolean closed) {
		if (closed == true && super.isClosed() == false){
			type = CLOSED_SPLINE;
			super.setClosed(closed);
		}
		else if (closed == false && super.isClosed() == true){
			type = NATURAL_SPLINE;
			super.setClosed(closed);
		}
		update();
	}
	
	private void calcPlot(int subPoints) {
		ArrayList<Point2D> plottingPoints = new ArrayList<Point2D>();
		
		double tInterval = (1 / (double)subPoints);
		
		int cutoff = Xcoefficients.length-1;
		if (type == CLOSED_SPLINE){
			cutoff+= 1;
		}
		
		for (int i = 0; i < cutoff; i++){
			for (double k = 0.0; k < 1; k+=tInterval){
				plottingPoints.add(new Point2D.Double((Xcoefficients[i][0] + k*(Xcoefficients[i][1] + k*(Xcoefficients[i][2] + Xcoefficients[i][3] * k))),
						(Ycoefficients[i][0] + k*(Ycoefficients[i][1] + k*(Ycoefficients[i][2] + Ycoefficients[i][3] * k)))));
			}
		}
		
		if (type != CLOSED_SPLINE){
			plottingPoints.add(super.points.get(super.points.size()-1));
		}
		else {
			plottingPoints.add(super.points.get(0));
		}
		
		this.plot = plottingPoints;
	}

	public ArrayList<Point2D> getPlot(int subPoints) {
		if (subPoints == divisions){
			return plot;
		}
		else {
			System.out.println("plotting points: " + subPoints);
			divisions = subPoints;
			update();
			return plot;
		}
	}
	
	public int add(double x, double y){
		int i = super.add(x, y);
		update();
		return i;
	}
	
	//currently works for natural spline & closed
	private void calcCoefficients(){
		double[][] CMatrixX = new double[super.points.size()][super.points.size()];
		double[][] CMatrixY = new double[super.points.size()][super.points.size()];
		double[] XvectorK = new double[super.points.size()];
		double[] YvectorK = new double[super.points.size()];
		
		double[] XvectorC = new double[super.points.size()];
		double[] YvectorC = new double[super.points.size()];
		
		//Create Vector Solution for C Matrix
		for (int i = 1; i < XvectorK.length-1; i++){
			XvectorK[i] = 3 * (super.points.get(i-1).getX() - (2*super.points.get(i).getX()) + super.points.get(i+1).getX());
			YvectorK[i] = 3 * (super.points.get(i-1).getY() - (2*super.points.get(i).getY()) + super.points.get(i+1).getY());
		}
		
		//Create C coefficient matrix 
		for (int i = 1; i < CMatrixX.length-1; i++){
			if ( i == 0){
				CMatrixX[i][i] = (double) 1;
				
				CMatrixY[i][i] = (double) 1;
			}
			else if (i == CMatrixX.length-1){
				CMatrixX[i][i] = (double) 1;
				
				CMatrixY[i][i] = (double) 1;
			}
			else{
				CMatrixX[i][i-1] = (double) 1;
				CMatrixX[i][i] = (double) 4;
				CMatrixX[i][i+1] = (double) 1;
				
				CMatrixY[i][i-1] = (double) 1;
				CMatrixY[i][i] = (double) 4;
				CMatrixY[i][i+1] = (double) 1;
			}
		}
		
		//adjust C coefficient matrix for each type of spline, currently only natural
		CMatrixX[0][0] = (double) 1;
		CMatrixY[0][0] = (double) 1;
		CMatrixX[CMatrixX.length -1][CMatrixX.length -1] = (double) 1;
		CMatrixY[CMatrixX.length -1][CMatrixX.length -1] = (double) 1;
		
		
		//changing Matrix and Vector for closed splines
		if (super.points.size() > 2 && type == CLOSED_SPLINE){
			CMatrixX[0][0] = 4;
			CMatrixX[0][1] = 1;
			CMatrixX[0][CMatrixX[0].length-1] = 1;
			
			CMatrixX[CMatrixX.length-1][CMatrixX[0].length-1] = 4;
			CMatrixX[CMatrixX.length-1][CMatrixX[0].length-2] = 1;
			CMatrixX[CMatrixX.length-1][0] = 1;
			
			CMatrixY[0][0] = 4;
			CMatrixY[0][1] = 1;
			CMatrixY[0][CMatrixY[0].length-1] = 1;
			
			CMatrixY[CMatrixY.length-1][CMatrixY[0].length-1] = 4;
			CMatrixY[CMatrixY.length-1][CMatrixY[0].length-2] = 1;
			CMatrixY[CMatrixY.length-1][0] = 1;
			
			XvectorK[0] = 3 * (super.points.get(XvectorK.length-1).getX() - (2*super.points.get(0).getX()) + super.points.get(1).getX());
			YvectorK[0] = 3 * (super.points.get(YvectorK.length-1).getY() - (2*super.points.get(0).getY()) + super.points.get(1).getY());
			
			XvectorK[XvectorK.length-1] = 3 * (super.points.get(XvectorK.length-2).getX() - (2*super.points.get(XvectorK.length-1).getX()) + super.points.get(0).getX());
			YvectorK[YvectorK.length-1] = 3 * (super.points.get(YvectorK.length-2).getY() - (2*super.points.get(YvectorK.length-1).getY()) + super.points.get(0).getY());
		}
		
		
		//Gaussian elimination to find c coefficients
		if (XvectorK.length > 2){
			XvectorC = gaussianElimination(CMatrixX, XvectorK);
			YvectorC = gaussianElimination(CMatrixY, YvectorK);
		}
		else {
			XvectorC = XvectorK;
			YvectorC = YvectorK;
		}
		
		Xcoefficients = new double[super.points.size()][4];
		Ycoefficients = new double[super.points.size()][4];
		
		//copies c coefficients and a coefficients into matrix
		for (int i = 0; i < Xcoefficients.length; i++){
			Xcoefficients[i][2] = XvectorC[i];
			Xcoefficients[i][0] = super.points.get(i).getX();
			
			Ycoefficients[i][2] = YvectorC[i];
			Ycoefficients[i][0] = super.points.get(i).getY();
		}
		
		//calculates b coefficients
		for (int i = 0; i < Xcoefficients.length-1; i++){
			Xcoefficients[i][1] = (Xcoefficients[i+1][0] - Xcoefficients[i][0]) - (((2*Xcoefficients[i][2]) + Xcoefficients[i+1][2])/3);
			
			Ycoefficients[i][1] = (Ycoefficients[i+1][0] - Ycoefficients[i][0]) - (((2*Ycoefficients[i][2]) + Ycoefficients[i+1][2])/3);
		}
		
		//calculates d coefficients
		for (int i = 0; i < Xcoefficients.length-1; i++){
			Xcoefficients[i][3] = ((Xcoefficients[i+1][2] - Xcoefficients[i][2])/3);
			
			Ycoefficients[i][3] = ((Ycoefficients[i+1][2] - Ycoefficients[i][2])/3);
		}
		
		if (super.points.size() > 2 && type == CLOSED_SPLINE){
			//b coefficients:
			Xcoefficients[Xcoefficients.length-1][1] = (Xcoefficients[0][0] - Xcoefficients[Xcoefficients.length-1][0]) - (((2*Xcoefficients[Xcoefficients.length-1][2]) + Xcoefficients[0][2])/3);
			Ycoefficients[Ycoefficients.length-1][1] = (Ycoefficients[0][0] - Ycoefficients[Ycoefficients.length-1][0]) - (((2*Ycoefficients[Ycoefficients.length-1][2]) + Ycoefficients[0][2])/3);
			
			//d coefficients
			Xcoefficients[Xcoefficients.length-1][3] = ((Xcoefficients[0][2] - Xcoefficients[Xcoefficients.length-1][2])/3);
			Ycoefficients[Ycoefficients.length-1][3] = ((Ycoefficients[0][2] - Ycoefficients[Ycoefficients.length-1][2])/3);
		}
		
		System.out.println("---------------------area: " + area(1));
	}
	
	//currently uses exact coefficient method
	protected double area(int METHOD) {
		calcDerivatives();
		double[][] unintegratedCoefficients = new double[dYcoefficients.length][6];
		
		//finding coefficients of y(t)*x'(t)
		for (int i = 0; i < unintegratedCoefficients.length; i++){
			unintegratedCoefficients[i][0] = Ycoefficients[i][0]*dXcoefficients[i][0];
			unintegratedCoefficients[i][1] = Ycoefficients[i][1]*dXcoefficients[i][0] + Ycoefficients[i][0]*dXcoefficients[i][1];
			unintegratedCoefficients[i][2] = Ycoefficients[i][2]*dXcoefficients[i][0] + Ycoefficients[i][1]*dXcoefficients[i][1] + Ycoefficients[i][0]*dXcoefficients[i][2];
			unintegratedCoefficients[i][3] = Ycoefficients[i][3]*dXcoefficients[i][0] + Ycoefficients[i][2]*dXcoefficients[i][1] + Ycoefficients[i][1]*dXcoefficients[i][2];
			unintegratedCoefficients[i][4] = Ycoefficients[i][3]*dXcoefficients[i][1] + Ycoefficients[i][2]*dXcoefficients[i][2];
			unintegratedCoefficients[i][5] = Ycoefficients[i][3]*dXcoefficients[i][2];
		}
		
		double[][] integratedCoefficients = calcAreaIntegral(unintegratedCoefficients);
		
		
		int cutoff = integratedCoefficients.length-1;
		if (type == CLOSED_SPLINE){
			cutoff+= 1;
		}
		
//		double dXdiscriminant;
		double finalArea = 0;
		
		for (int i = 0; i < cutoff; i++){
			finalArea += calcArea(integratedCoefficients[i], 0.0, 1.0);
		}
		
		return Math.abs(finalArea);
	}
	
	/**
	 * Method used to calculate the numerical value of the area under a piece of the cubic spline. This method uses integrated form of the function y(t)*x'(t)
	 * @param integratedCoefficients the 6 coefficients of the integrated function with powers ranging from t^1 to t^6
	 * @param a lower bound
	 * @param b upper bound
	 * @return area of defined sub-section of spline
	 */
	private double calcArea(double[] integratedCoefficients, double a, double b){
		double upper = ((((((integratedCoefficients[5] * b + integratedCoefficients[4]) * b + integratedCoefficients[3])* b + integratedCoefficients[2]) * b + integratedCoefficients[1]) * b + integratedCoefficients[0]) * b);
		double lower = ((((((integratedCoefficients[5] * a + integratedCoefficients[4]) * a + integratedCoefficients[3])* a + integratedCoefficients[2]) * a + integratedCoefficients[1]) * a + integratedCoefficients[0]) * a);
		return upper-lower;
	}
	
	/**
	 * 
	 * @param coefficients the un-integrated coefficients of the polynomial function to be integrated. index 0 has power x^0, index n has x^n
	 * @return returns the integrated matrix. index 0 has x^1 and index n has x^(n+1)
	 */
	private double[][] calcAreaIntegral(double[][] coefficients){
		double[][] integratedCoefficients = new double[coefficients.length][6];
		
		for (int i = 0; i < coefficients.length; i++){
			for (int k = 0; k < coefficients[0].length; k++){
				integratedCoefficients[i][k] = coefficients[i][k] / (k+1);
			}
		}
		
		return integratedCoefficients;
	}
	
	/**
	 * this method calculates the derivative coefficients using calculus methods, as it is a trivial polynomial
	 */
	private void calcDerivatives(){
		dXcoefficients = new double[Xcoefficients.length][3];
		dYcoefficients = new double[Ycoefficients.length][3];
		
		for (int i = 0; i < dXcoefficients.length; i++){
			for (int k = 0; k < 3; k++){
				dXcoefficients[i][k] = (k+1)*Xcoefficients[i][k+1];
				dYcoefficients[i][k] = (k+1)*Ycoefficients[i][k+1];
			}
		}
		
	}
	
	//Gaussian elimination with partial pivoting
	//method for temporary testing copied from: http://introcs.cs.princeton.edu/java/95linear/GaussianElimination.java.html
	//all credit belongs to original authors: Robert Sedgewick and Kevin Wayne
    public static double[] gaussianElimination (double[][] A, double[] b) {
        int N  = b.length;

        for (int p = 0; p < N; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double   t    = b[p]; b[p] = b[max]; b[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= EPSILON) {
                throw new RuntimeException("Matrix is singular or nearly singular");
            }

            // pivot within A and b
            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }
    
    public String toString(){
		String string = "CubicSpline: ";
		for (int i = 0; i < super.points.size(); i++){
			string = string + "[" + super.points.get(i).getX() +","+ super.points.get(i).getY() + "] " ;
		}
		return string;
	}
    
    public static String printMatrix(double[][] matrix, String name){
    	String string = name + ": \n";
		for (int i = 0; i < matrix.length; i++){
			for(int k = 0; k < matrix[0].length; k++){
				string = string + matrix[i][k] + ", ";
			}
			string = string + "\n";
		}
		return string;
    }
    
    public static String printVector(double[] vector, String name){
    	String string = name + ":";
		for (int i = 0; i < vector.length; i++){
			string = string + "\n" + vector[i] ;
		}
		return string;
    }
}
