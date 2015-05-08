package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GenitorBowl extends Curve{
	private static final int POP_SIZE = 20000;
	private static final int CHILDREN = 200000;
	private static final int NUMBER_OF_POINTS = 5;

	ArrayList<GenitorIndividual> population;
	
	public GenitorBowl(String name, double targetLength){
		super(name);
		population = new ArrayList<GenitorIndividual>();
		generatePopulation(targetLength, NUMBER_OF_POINTS);
		replicate();
	}
	
	private void replicate(){
		for (int i = 0; i < CHILDREN; i++){
			binaryInsert(population.get(((int) ((Math.random()*Math.random())* POP_SIZE))).reproduce(population.get(((int) ((Math.random()*Math.random())* POP_SIZE)))));
			population.remove(POP_SIZE);
			if (i % 10000 == 0){
				System.out.println("current best: " + population.get(0).getALRatio() + ", fitness: " + population.get(0).fitness());
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
		int i = 0;
		if (population.size() == 0){
			population.add(indiv);
		}
		else {
			while (i < population.size() && indiv.compareTo(population.get(i)) == -1){
				i++;
			}
			population.add(i, indiv);
		}
	}

	@Override
	protected ArrayList<Point2D> getPlot(int subPoints) {
		return population.get(0).getElement().getPlot(subPoints);
	}
	
	@Override
	protected double area(int method){
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
