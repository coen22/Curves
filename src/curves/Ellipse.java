package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Ellipse extends Curve{

	private int type;
	private ArrayList <Point2D> plot;
	private double a;
	private double b;
	private double c;
	private double d;
	private int currentSubPoints;
	
	public Ellipse(double a, double b, double c, double d, String name, int type) {
		super(name);
		this.type = type;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		
		if( a >= 10 || b >= 10){
			currentSubPoints = 10000;
		}
		else{
		currentSubPoints = 1000;
		}
		calcPlot(currentSubPoints);
		
		areaAlgorithms.add(NumericalApproximation.SHOELACE_AREA);
		areaAlgorithms.add(NumericalApproximation.EXACT_ELLIPSE_AREA);
		
		arcLengthAlgorithms.add(NumericalApproximation.PYTHAGOREAN_ARCLENGTH);
		arcLengthAlgorithms.add(NumericalApproximation.RICHARDSON_EXTRAPOLATION_ARCLENGTH);
		arcLengthAlgorithms.add(NumericalApproximation.ELLIPSE_ARCLENGTH_EXACT);
	}

	private void calcPlot(int subPoints){
		double interval = (2*Math.PI)/((double)(4*subPoints));
		
		plot = new ArrayList <Point2D>();
		for(double t = 0; t <= 2*Math.PI; t += interval){
			plot.add(new Point2D.Double(calcX(t), calcY(t)));
		}
		
	}
	
	private double calcX(double t){
		return (a * Math.cos(b * t));
	}
	
	private double calcY(double t){
		return (c * Math.sin(d * t));
	}
	
	double calc_hR(){
		double hR = c;
		return hR;
	}
	
	double calc_vR(){
		double vR = a;
		return vR;
	}
	
	@Override
    protected double area(int method) {
		areaAlgorithm = method;
		return NumericalApproximation.calcArea(this);
	}
	
	@Override
	protected double length(int method) {
		arcLengthAlgorithm = method;
		return NumericalApproximation.calcArcLength(this);
	}
	@Override
	protected ArrayList<Point2D> getPlot(int subPoints) {
		calcPlot(subPoints);
		return plot;
	}
	
	@Override
	protected List<Point2D> getConversionPoints() {
		return null;
	}

}
