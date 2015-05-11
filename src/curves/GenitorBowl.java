package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GenitorBowl extends Curve{
	private static final double SELECTIVE_PRESSURE_POWER = 2;
	
	private static final int POP_SIZE = 1000; //2000
	private static final int CHILDREN = 100000; //200000
	private static final int NUMBER_OF_POINTS = 5;
	
	private static final int LENGTH_METHOD = NumericalApproximation.SIMPSON_ARCLENGTH;

	ArrayList<GenitorIndividual> population;
	
	public GenitorBowl(String name, double targetLength){
		super(name);
		population = new ArrayList<GenitorIndividual>();
		generatePopulation(targetLength, NUMBER_OF_POINTS);
		System.out.println("target length: " + targetLength);
		replicate();
	}
	
	private void replicate(){
		for (int i = 0; i < CHILDREN; i++){
			binaryInsert(population.get(((int) ((Math.pow(Math.random(), SELECTIVE_PRESSURE_POWER))* POP_SIZE))).reproduce(population.get(((int) ((Math.pow(Math.random(), SELECTIVE_PRESSURE_POWER))* POP_SIZE)))));
			population.remove(POP_SIZE);
			
			if (i % 10000 == 0){
				System.out.println("current best: " + population.get(0).getALRatio() + ", length: " + population.get(0).getElement().length(LENGTH_METHOD) +  ", fitness: " + population.get(0).fitness());
			}
		}
		System.out.println("current best: " + population.get(0).getALRatio() + population.get(0).toString());
	}
	
	private void generatePopulation(double targetLength, int numberOfPoints) {
		for (int i = 0; i < POP_SIZE; i++){
			binaryInsert(new GenitorIndividual(targetLength, numberOfPoints));
		}
	}

	private void binaryInsert(GenitorIndividual indiv){
		if (population.size() == 0){
			population.add(indiv);
		}
		else {
			int test = population.size()/2;
			int min = 0;
			int max = population.size();
			while (max-min > 1){
				if (indiv.compareTo(population.get(test)) == 0){
					max = test;
					min = test;
					population.add(test, indiv);
				}
				else if (indiv.compareTo(population.get(test)) > 0){
					max = test;
					test = (test+min)/2;
				}
				else {
					min = test;
					test = (max+test)/2;
				}
			}
			if (indiv.compareTo(population.get(test)) > 0){
				population.add(test,indiv);
			}
			else {
				population.add(test+1,indiv);
			}
		}
	}

	@Override
	protected ArrayList<Point2D> getPlot(int subPoints) {
		return population.get(0).getElement().getPlot(subPoints);
	}
	
	@Override
	protected double area(int method){
		population.get(0).getElement().update();
		System.out.println("final area: " + population.get(0).getElement().area(method));
		return population.get(0).getElement().area(method);
	}
	
	@Override
	protected double length(int method){
		return population.get(0).getElement().length(method);
	}
	
	public String toString(){
		String string = "";
		for (int i = 0; i < population.size(); i++){
			string = string + population.get(i).toString() + "\n";
		}
		return string;
	}
	
}
