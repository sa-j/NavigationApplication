package edu.prgpr;

import java.util.Comparator;

/**
 *
 * @author Saj, Joanna *
 */
public class CompareMapNodeTime implements Comparator<MapNode>{

     /**
     * the class defines the structure by a heap with MapNodes nodes with the time
     */
    /**
     *
     * @param node1 MapNode with the time to compare
     * @param node2 MapNode with the time to compare
     * @return the shift of one node with the time in the heap
     */
        @Override
	public int compare(MapNode node1, MapNode node2) {

		if (node1.timeValue < node2.timeValue){
			return -1;
		}
		if (node1.timeValue > node2.timeValue){
			return 1;
		}
		return 0;
	}
}
