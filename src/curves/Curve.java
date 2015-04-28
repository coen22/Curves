package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public abstract class Curve {
	
	protected ArrayList<Point2D> points;
	protected boolean closed;
	protected String name;
	
	public Curve(String name) {
		this.name = name;
		closed = false;
		points = new ArrayList<Point2D>();
	}
	
	/**
	 * method which calculates or returns the plotting coordinates such that the UI can draw all curves. NOTE THE DEFINITION OF THE SUB-POINTS!
	 * @param subPoints this number signifies the number of plotting points in-between each pair of control-points. The larger the number, the more fine-grained the curve plot will be.
	 * @return
	 */
	protected ArrayList<Point2D> getPlot(int subPoints) {
        //add spefic methods for your type of lines here
    	return points;
    }
	
	protected double length(int METHOD) {
		return 0;
	}

	protected double area(int METHOD) {
		return 0;
	}
	
	protected boolean isClosed() {
		return closed;
	}
	
	protected void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	protected void setName(String name){
		this.name = name;
	}
	
	protected String getName(){
		return name;
	}

	protected int add(double x, double y) {
		this.points.add(new Point2D.Double(x, y));
		return points.size() - 1;
	}
	
	protected int numberOfPoints(){
		return points.size();
	}
        
    private double shoeLaceArea(){
    	double area = 0;
    	
    	if (!closed)
    		return 0;
    	
    	List<Point2D> listOfPoints = getPlot(numberOfPoints());
    	for (int i = 0; i < listOfPoints.size() - 1; i++) {
          area += listOfPoints.get(i).getX() * listOfPoints.get(i+1).getY() 
        		  - listOfPoints.get(i+1).getX() * listOfPoints.get(i).getY();
		}
		area += listOfPoints.get(listOfPoints.size() - 1).getX() * listOfPoints.get(0).getY() -
				listOfPoints.get(0).getX() * listOfPoints.get(listOfPoints.size() - 1).getY();
    	
    	return area/2;
    }
    
    private double pythagoreanLength(){
    	double length = 0;
    	List<Point2D> listOfPoints = getPlot(numberOfPoints());
    	
    	if (listOfPoints.isEmpty() || listOfPoints.size() == 1)
    		return 0;
    	
		for (int i = 0; i < listOfPoints.size() - 1; i++)
			length += Math.sqrt(Math.pow(listOfPoints.get(i).getX() - listOfPoints.get(i - 1).getX(), 2) +
								Math.pow(listOfPoints.get(i).getY() - listOfPoints.get(i - 1).getY(), 2));
		if (closed)
			length += Math.sqrt(Math.pow(listOfPoints.get(0).getX() - listOfPoints.get(listOfPoints.size() - 1).getX(), 2) +
								Math.pow(listOfPoints.get(0).getY() - listOfPoints.get(listOfPoints.size() - 1).getY(), 2));
    	return length;
    }
    
    protected List<Point2D> getControlPoints(){
    	return (List) points;
    }
    
    protected Point2D removePoint(int index) {
    	return points.remove(index);
    }
    
    protected void setPoint(int index, double x, double y) {
    	points.get(index).setLocation(x, y);
    }
}
