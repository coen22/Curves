package ui.Events;

/**
 * Interface to handle the GUI Events
 *
 * @author Kareem Horstink
 */
public interface GUI_Event_Listner {
    
    public void actionPerformed(Gui_Events e);

    public void handleCreate(Gui_Events_Create e);

    public void handleAdd(Gui_Events_Add e);

    public void handleMove(Gui_Events_Move e);

    public void handleDeleteP(Gui_Events_DeleteP e);

    public void handleDeleteC(Gui_Events_DeleteC e);

    public void handleClose(Gui_Events_Close e);

    public void handleOpen(Gui_Events_Open e);

    public void handleVis(Gui_Events_Vis e);

    public void handleCurrent(Gui_Events_Current e);

}
