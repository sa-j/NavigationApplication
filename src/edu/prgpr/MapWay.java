package edu.prgpr;

import java.util.ArrayList;
import java.util.Hashtable;
import edu.prgpr.Coordinates.UTMPoint;

/**
*
* @author Saj, Joanna
*
*/
public class MapWay {
        /**
         * The class MapWay represents a way is from the nodes which are connected 
         */
    
        /**
         * the id-number of the way
         */
	public int id;
        /**
         * the list of the id-numbers of the way
         */
	public ArrayList<Integer> nd;
        /**
         * the table with key and value
         */
	public Hashtable<String, String> tag;
	/**
	 * the list of MapNodes nodes of the way
	 */
	public ArrayList <MapNode> wayNodes;
        /**
         * the UTMPoint with x-and y-coordinates
         */
	public UTMPoint utm;
        
        /**
         * empty constructor
         */
        public MapWay(){
            
        }
        /**
	 * @param id the id-number of the way
	 * @param nd the list of the id-numbers from the nodes
	 * @param tag the hashtable with values and keys
	 */
	public MapWay(int id, ArrayList<Integer> nd, Hashtable<String, String> tag) {
		this.id = id;
		this.nd = nd;
		this.tag = tag;
	}
	/**
	 * @param id the id-number of the way
	 * @param nd the list of the id-numbers from nodes
	 * @param tag the hashtable with the values and keys
         * @param utm the Point with x-and y-coordinates
	 */
	public MapWay(int id, ArrayList<Integer> nd, Hashtable<String, String> tag , UTMPoint utm) {
		this.id = id;
		this.nd = nd;
		this.tag = tag;
		this.utm = utm;
	}

	/**
	 * @return the id-number of the way
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id-number of the way to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the nd-list with id-numbers from nodes
	 */
	public ArrayList<Integer> getNd() {
		return nd;
	}
	/**
	 * @param nd the nd-list with id-numbers from nodes to set
	 */
	public void setNd(ArrayList<Integer> nd) {
		this.nd = nd;
	}
	/**
	 * @return the hashtable with tags
	 */
	public Hashtable<String, String> getTag() {
		return tag;
	}
	/**
	 * @param tag the hashtable with tags to set
	 */
	public void setTag(Hashtable<String, String> tag) {
		this.tag = tag;
	}
	/** (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MapWay [id=" + id + ", nd=" + nd + ", tag=" + tag + "]";
	}
     /**
      * 
     * adds a tag to this way
     * 
     * @param key - key with which the specified value is to be associated value 
     * @param value - value to be associated with the specified key
     */
    void addTag(String key, String value) {
        this.tag.put(key, value);
    }
    /**
     * @param x the referenznumber - ref (a id number of a node the to a way includes)
     */
    public void addNd(int ref){
    	this.nd.add(ref);
    }
    /**
     * @return the nodes there in a way are
     */
    public ArrayList<MapNode> getWayNodes() {
	return wayNodes;
    }

    /**
     * @param wayNodes the Nodes from a way to set
     */
    public void setWayNodes(ArrayList<MapNode> wayNodes) {
	this.wayNodes = wayNodes;
    }

	
    }
