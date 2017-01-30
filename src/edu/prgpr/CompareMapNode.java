package edu.prgpr;

import java.util.Comparator;
import java.util.PriorityQueue;
/**
 *
 * @author Saj, Joanna
 */


public class CompareMapNode implements Comparator<MapNode>{
    /**
     * the class defines the structure by a heap with MapNodes nodes with the distance
     */
    /**
     *
     * @param node1 MapNode with the distance to compare
     * @param node2 MapNode with the distance to compare
     * @return the shift of one node with the distance in the heap
     */
 @Override
    public int compare(MapNode node1, MapNode node2) {

		if (node1.minDistanz < node2.minDistanz){
			return -1;
		} 
		if (node1.minDistanz > node2.minDistanz){
			return 1;
		}
		return 0;
	}
}
