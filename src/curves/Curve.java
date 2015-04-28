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
	
	protected void translatePoint(int pointIndex, double deltaX, double deltaY){
		//David will do this
	}

	protected int add(double x, double y) {
		this.points.add(new Point2D.Double(x, y));
		return points.size() - 1;
	}
	
	protected int numberOfPoints(){
		return points.size();
	}
        
    private double shoeLaceArea(){
    	return 0;
    }
    
    private double pythagoreanLength(){
    	return 0;
    }
    
    protected List<Point2D> getControlPoints(){
    	return (List) points;
    }
    
    protected Point2D removePoint(int index) {
        return null;
    }
    
    protected void setPointLocation(int index, double x, double y) {
    	points.get(index).setLocation(x, y);
    }
}
