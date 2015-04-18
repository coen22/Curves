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
	
	private ArrayList<double[]> Xcoefficients;
	private ArrayList<double[]> Ycoefficients; 
	private int type;

	public CubicSpline(Point2D point, String name, int type){
		super(name);
		add(point.getX(), point.getY());
		this.type = type;
	}
	
	public double length() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double area() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Point2D> calcPlotingCoordinates(double tInterval) {
		return null;
	}
	
	public void add(double x, double y){
		super.points.add(new Point2D.Double(x,y));
	}
	
	//currently works for natural spline
	public void calcCoefficients(){
		double[][] CMatrix = new double[super.points.size()-2][super.points.size()];
		double[] XvectorK = new double[super.points.size()];
		double[] XvectorC = new double[super.points.size()];
		
		for (int i = 0; i < CMatrix.length; i++){
			for(int k = 0; k < CMatrix[0].length; k++){
				CMatrix[i][k] = 0.0;
			}
			XvectorK[i] = 0.0;
		}
		
		if (type == NATURAL_SPLINE){
			for (int i = 1; i < XvectorK.length-1; i++){
				XvectorK[i] = 3 * (super.points.get(i-1).getX() - (2*super.points.get(i).getX()) + super.points.get(i+1).getX());
			}
			for (int i = 0; i < CMatrix.length; i++){
				CMatrix[i][i] = (double) 1;
				CMatrix[i][i+1] = (double) 4;
				CMatrix[i][i+2] = (double) 1;
			}
			System.out.println(printVector(XvectorK, "vector"));
			System.out.println(printMatrix(CMatrix, "test"));
			
			XvectorC = gaussianElimination(CMatrix, XvectorK);
			System.out.println(printVector(XvectorC, "elimination"));
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
    	String string = name + ": \n";
		for (int i = 0; i < vector.length; i++){
			string = string + vector[i] + "\n";
		}
		return string;
    }
}
