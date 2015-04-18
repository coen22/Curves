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
	private int type;

	public CubicSpline(Point2D point, String name, int type){
		super(name);
		add(point.getX(), point.getY());
		this.type = type;
		calcCoefficients();
	}
	
	public double length() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double area() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * returns a list of the points required for plotting. 
	 * @param tInterval the 
	 * @return
	 */
	public List<Point2D> calcPlottingCoordinates(double tInterval) {
		ArrayList<Point2D> plottingPoints = new ArrayList<Point2D>();
		
		for (int i = 0; i < Xcoefficients.length-1; i++){
			for (double k = 0.0; k < 1; k+=tInterval){
				plottingPoints.add(new Point2D.Double((Xcoefficients[i][0] + k*(Xcoefficients[i][1] + k*(Xcoefficients[i][2] + Xcoefficients[i][3] * k))),
						(Ycoefficients[i][0] + k*(Ycoefficients[i][1] + k*(Ycoefficients[i][2] + Ycoefficients[i][3] * k)))));
			}
		}
		
		plottingPoints.add(super.points.get(super.points.size()-1));
		
		
		return (List<Point2D>)plottingPoints;
	}
	
	public void add(double x, double y){
		super.points.add(new Point2D.Double(x,y));
		calcCoefficients();
	}
	
	//currently works for natural spline
	public void calcCoefficients(){
		double[][] CMatrixX = new double[super.points.size()][super.points.size()];
		double[][] CMatrixY = new double[super.points.size()][super.points.size()];
		double[] XvectorK = new double[super.points.size()];
		double[] YvectorK = new double[super.points.size()];
		
		double[] XvectorC = new double[super.points.size()];
		double[] YvectorC = new double[super.points.size()];
		
		if (type == NATURAL_SPLINE){
			
			//creation vector solution
			for (int i = 1; i < XvectorK.length-1; i++){
				XvectorK[i] = 3 * (super.points.get(i-1).getX() - (2*super.points.get(i).getX()) + super.points.get(i+1).getX());
				YvectorK[i] = 3 * (super.points.get(i-1).getY() - (2*super.points.get(i).getY()) + super.points.get(i+1).getY());
			}
			
			//creation of the C-coefficient matrix for solution
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
				CMatrixX[0][0] = (double) 1;
				CMatrixX[1][0] = (double) 0;
				CMatrixX[CMatrixX.length-1][CMatrixX[0].length-1] = (double) 1;
				CMatrixX[CMatrixX.length-2][CMatrixX[0].length-1] = (double) 0;
				
				CMatrixY[0][0] = (double) 1;
				CMatrixY[1][0] = (double) 0;
				CMatrixY[CMatrixX.length-1][CMatrixY[0].length-1] = (double) 1;
				CMatrixY[CMatrixX.length-2][CMatrixY[0].length-1] = (double) 0;
			}
			
			//Gaussian elimination to find c coefficients. Only if necessary
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
