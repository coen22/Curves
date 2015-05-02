package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PolyLine extends Curve {

    protected PolyLine(Point2D point, String name) {
        super(name);
        super.points.add(point);
    }

    @Override
    protected ArrayList<Point2D> getPlot(int interval) {
        return points;
    }
    
    public void setClosed(boolean closed) {
		if (closed == true && super.isClosed() == false){//close
			super.points.add(super.points.get(0));
			super.setClosed(closed);
		}
		else if (closed == false && super.isClosed() == true){//open
			super.points.remove(super.numberOfPoints()-1);
			super.setClosed(closed);
		}
	}
    
    @Override
    protected int add(double x, double y) {
    	if (this.isClosed()){
    		this.setClosed(false);
    		this.points.add(new Point2D.Double(x, y));
    		this.setClosed(true);
    	}
    	else {
    		this.points.add(new Point2D.Double(x, y));
    	}
		return points.size() - 1;
	}
    
}
