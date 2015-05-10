/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.events;

/**
 * An event to show that the algorithm to calculate the length should be changed 
 *
 * @author Kareem Horstink
 * @version 1.0
 */
public class GuiEventsLengthChange extends GuiEvents {

    private int algorithm;

    public GuiEventsLengthChange(Object source, int algorithm) {
        super(source);
        this.algorithm = algorithm;
    }

    public int getAlgorithm() {
        return algorithm;
    }
}
