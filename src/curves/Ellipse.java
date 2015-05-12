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
			currentSubPoints = 20000;
		}
		else{
		currentSubPoints = 1000;
		}
		calcPlot();
	}

	private void calcPlot(){
		double interval = (2*Math.PI)/((double)currentSubPoints);
		System.out.println("Interval: " + interval);
		plot = new ArrayList <Point2D>();
		
		for(double t = 0; t <= 2*Math.PI; t += interval){
			plot.add(new Point2D.Double(calcX(t), calcY(t)));
			System.out.println("CalcX: " + calcX(t));
			}
		System.out.println("plot size: " + plot.size());
	}
	
	private double calcX(double t){
		return (a * Math.cos(b * t));
	}
	
	private double calcY(double t){
		return (c * Math.sin(d * t));
	}
	
	protected double lenght(){
		double length = 0;
		
		return length;
	}
	protected double area(){
		double area = 0;
		//Area is calculated by 2*PI*horizontalRadius*verticalRadius
		//First calculate hR and vR
		//To get hR fill in t = 0 or t = PI. To get vR fill in t = 0.5*PI or t = 1.5*PI. 
		double t1 = 0;
		double t2 = (0.5*Math.PI);
		double hR, vR;
		hR = calcX(t1);
		vR = calcY(t2);		
		area = Math.PI*hR*vR;
		return area;
	}
	@Override
	protected ArrayList<Point2D> getPlot(int subPoints) {
//		System.out.println("Passed subpoints " + subPoints);
//		subPoints *= 10;
//		if(subPoints == currentSubPoints){
//			return plot;
//		}
//		else{
//			currentSubPoints = subPoints;
			calcPlot();
			return plot;
//		}
		
	}
	
	@Override
	protected List<Point2D> getConversionPoints() {
		return null;
	}

}
