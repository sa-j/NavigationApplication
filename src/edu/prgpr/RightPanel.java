/**
 * 
 */
package edu.prgpr;

import javax.swing.JPanel;

/**
 * @author Saj, Joanna
 *
 */
public class RightPanel extends JPanel {

    /**
     * panel with the buttons 
     */
    private static final long serialVersionUID = 1L;
    /**
     * graph with nodes and ways
     */
    public Graph graph;
    /*
     * constructor
     */
    public RightPanel(Graph graph) {
        this.graph = graph;

    }
}
