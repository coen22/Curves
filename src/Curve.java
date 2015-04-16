import java.awt.geom.Point2D;
import java.util.ArrayList;


public abstract class Curve {
	
	protected ArrayList<Point2D> points;
	protected boolean closed;
	protected String name;
	
	public Curve(String name){
		this.name = name;
		closed = false;
		points = new ArrayList<Point2D>();
	}
	
	public double length(int METHOD) {
		return 0;
	}

	public double area(int METHOD) {
		return 0;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void translate(int POINT_ID, double deltaX, double deltaY){
		
	}

	public void add(double x, double y){
		
	}
	
	public void add(double x, double y, int index){
		
	}
	
	public int numberOfPoints(){
		return points.size();
	}
	
}
