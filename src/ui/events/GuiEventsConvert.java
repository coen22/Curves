package ui.events;

public class GuiEventsConvert extends GuiEvents{
	
	private int curveID, TYPE;
	
    public GuiEventsConvert(Object source, int curveID, int TYPE) {
        super(source);
        this.curveID = curveID;
        this.TYPE = TYPE;
    }
    
    public int getType() {
        return TYPE;
    }
    
    public int getCurveID() {
        return curveID;
    }
}
