package curves;

import java.awt.geom.Point2D;

public class GenitorIndividual implements Comparable<GenitorIndividual>{
	private static final double SHIFT_RATE_X = 0.5; //1.0
	private static final double NOISE_RATE_Y = 0.5; //0.5
	private static final double NOISE_RATE_X = 0.5; //0.5
	private static final double X_SHIFT_FACTOR = 0.05; //0.05
	private static final double Y_NOISE_FACTOR = 0.05; //0.05
	private static final double X_NOISE_FACTOR = 0.05; //0.05
	private static final double AL_RATIO_WEIGHT = 150; //150
	private static final double LENGTH_WEIGHT = 1; //1
	private static final double LENGTH_POWER = 1; //1
	
	private static final int LENGTH_METHOD = NumericalApproximation.ROMBERG_ARCLENGTH;
	
	private double fitness;
	private CubicSpline curve;
	private double targetLength;
	
	public GenitorIndividual(double targetLength, int n){
		this.targetLength = targetLength;
		this.curve = randomCubicSpline(n);
		calcFitness();
	}
	
	public GenitorIndividual(CubicSpline curve, double targetLength){
		this.targetLength = targetLength;
		this.curve = curve;
		calcFitness();
	}
	
	private CubicSpline randomCubicSpline(int n) {
		double seed = targetLength*Math.random();
		CubicSpline curve = new CubicSpline(new Point2D.Double(0, 0), "curve", CubicSpline.NATURAL_SPLINE);
		for (int i = 1; i < n-1; i++){
			curve.add(Math.abs(i*(seed/(n-1))) + ((seed/(n-1))*(Math.random()-0.5)), Math.abs(targetLength*Math.random()));
		}
		curve.add(seed,0.0);
		return curve;
	}
	
	protected void shiftX(){
		double maxShift = this.curve.points.get(this.curve.points.size()-1).getX() * ((Math.random()-0.5)*X_SHIFT_FACTOR);
		for (int i = this.curve.points.size()-1; i > 0; i--){
			this.curve.points.get(i).setLocation((this.curve.points.get(i).getX() + ((double)i/(double)(this.curve.points.size()-1))*maxShift), this.curve.points.get(i).getY());
		}
		this.curve.update();
		calcFitness();
	}
	
	protected void noiseY(){
		for (int i = 0; i < this.curve.points.size()-1; i++){
			this.curve.points.get(i).setLocation(this.curve.points.get(i).getX(), this.curve.points.get(i).getY() + (this.curve.points.get(i).getY() * (Math.random()-0.5) * Y_NOISE_FACTOR));
		}
		this.curve.update();
		calcFitness();
	}
	
	protected void noiseX(){
		for (int i = 0; i < this.curve.points.size()-1; i++){
			this.curve.points.get(i).setLocation(this.curve.points.get(i).getX() + (this.curve.points.get(i).getX() * (Math.random()-0.5) * X_NOISE_FACTOR), this.curve.points.get(i).getY());
		}
		this.curve.update();
		calcFitness();
	}

	private void calcFitness() {
		double length = curve.length(LENGTH_METHOD);
		this.fitness = AL_RATIO_WEIGHT * (Math.sqrt(this.curve.area(NumericalApproximation.EXACT_AREA_CUBIC))/length);
		fitness -= LENGTH_WEIGHT * (Math.pow(Math.abs(length-targetLength), LENGTH_POWER));
	}
	
	public double getALRatio(){
		return Math.sqrt(curve.area(NumericalApproximation.EXACT_AREA_CUBIC))/curve.length(LENGTH_METHOD);
	}

	public CubicSpline getElement(){
		return curve;
	}
	
	public double fitness(){
		return fitness;
	}

	@Override
	public int compareTo(GenitorIndividual b) {
		if (this.fitness > b.fitness()){
			return 1;
		}
		else if (this.fitness < b.fitness()){
			return -1;
		}
		else {
			return 0;
		}
	}
	
	protected GenitorIndividual reproduce(GenitorIndividual b){
		CubicSpline childCurve = new CubicSpline(new Point2D.Double(0.0, 0.0), "new", CubicSpline.NATURAL_SPLINE);
		int crossover = (int) (Math.random() * (curve.points.size()-1)) + 1;
		
		if (Math.random() < 0.5){
			for (int i = 1; i < this.curve.numberOfPoints(); i++){
				if (i < crossover){
					childCurve.add(this.curve.points.get(i).getX(), this.curve.points.get(i).getY());
				}
				else {
					childCurve.add(this.curve.points.get(i).getX(), b.curve.points.get(i).getY());
				}
			}
		}
		else {
			for (int i = 1; i < this.curve.numberOfPoints(); i++){
				if (i < crossover){
					childCurve.add(this.curve.points.get(i).getX(), this.curve.points.get(i).getY());
				}
				else {
					childCurve.add(this.curve.points.get(i).getX(), b.curve.points.get(i).getY());
				}
			}
		}
		
		GenitorIndividual child = new GenitorIndividual(childCurve, this.targetLength);
		if (Math.random() < SHIFT_RATE_X){
			child.shiftX();
		}
		if (Math.random() < NOISE_RATE_Y){
			child.noiseY();
		}
		if (Math.random() < NOISE_RATE_X){
			child.noiseX();
		}
		return child;
	}
	

	public String toString(){
		String string = "";
		string = string + ". length: " + curve.length(LENGTH_METHOD) + ". ";
		string = string + this.curve + ":::" + this.fitness; 
		return string;
	}

}
