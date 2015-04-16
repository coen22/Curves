import java.util.ArrayList;
package curves;

public class Controller {

	private ArrayList<Curve> curves;
	
	public Controller(){
		curves = new ArrayList<Curve>();
	}
	
	public void createCurve(int TYPE, double x, double y, String name){
		
	}
	
	public boolean closeCurve(int CURVE_ID){
		return false;
	}
	
	public boolean openCurve(int CURVE_ID){
		return false;
	}
	
	public int getCurveID(Curve curve){
		return 0;
	}
	
	public void addLastPoint(double x, double y, int CURVE_ID){
		
	}
	
	public void addPoint(double x, double y, int CURVE_ID, int index){
		
	}
	
	public void translate(double deltaX, double deltaY, int CURVE_ID, int POINT_ID){
		
	}
	
}
