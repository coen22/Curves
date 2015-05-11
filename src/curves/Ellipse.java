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
		currentSubPoints = 20;
		calcPlot();
	}

	private void calcPlot(){
		double interval = (2*Math.PI)/(4*currentSubPoints);
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
	@Override
	protected ArrayList<Point2D> getPlot(int subPoints) {
		if(subPoints == currentSubPoints){
			return plot;
		}
		else{
			currentSubPoints = subPoints;
			calcPlot();
			return plot;
		}
		
	}
	
	@Override
	protected List<Point2D> getConversionPoints() {
		return null;
	}

}
