/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.prgpr;

import java.util.ArrayList;

/**
 *
 * @author Saj, Joanna
 */
public class NearestWay {
    /**
     * the class calculates the nearest way from the node that do not belong to a way
     */
    
    /**
     * graph with nodes and ways
     */
    public Graph graf = new Graph();
    /**
     * constructor
     * @param graph graph with nodes and ways
     */
    NearestWay(Graph graph){
        this.graf = graph;
    }
    /**
     * @param ofNode node from which we calculate the way
     * @return nearest node of node ofNode
     * 
     */
    public MapNode findNearstHighwayNode(MapNode ofNode){
        MapNode nd = new MapNode();
        double abs = Double.MAX_VALUE;
        double temp = 0;
        //ref is the nearest Node from nd
        MapNode ref = new MapNode();
        for(int i=0;i<=graf.highwayNodeList.size()-1;i++){
                nd = graf.highwayNodeList.get(i);
                temp = Math.sqrt(Math.pow((nd.getX_Coordinate() - ofNode.getX_Coordinate()),2) + Math.pow((nd.getY_Coordinate() - ofNode.getY_Coordinate()),2));
                if(temp < abs){
                    abs = temp;
                    ref = nd;
                }
            }
        return ref;
    }
}
