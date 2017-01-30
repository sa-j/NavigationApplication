package edu.prgpr;

import edu.prgpr.Coordinates.UTMPoint;
import java.util.Hashtable;
/**
*
* @author Saj, Joanna
*/

public class MapNode {
    /**
     * The class MapNode represents a node from a map
     */

    /**
     * if node is not visited then it is set to false
     */
    public boolean reviewed = false;
    /**
     * the minimum distance to the next node
     * if he has not been calculated, then we put him on Integer UNDEND
     */
    public int minDistanz = Integer.MAX_VALUE; 
    /**
     * the minimum time to the next node
     * if he has not been calculated, then we put him on Integer UNDEND
     */
    public int timeValue = Integer.MAX_VALUE;
    /**
     * predecessor, father node from the node
     */
    public MapNode Predecessor;
    /**
     * we set the speed from all nodes to 50 km/h
     */
    public int speed = 50;
    /**
     * the id-number of the node
     */
    int id;
    /**
     * table with a key and a value
     */
    public Hashtable<String, String> tag;
    /**
     * the Point with x- and y-coordinates and the UTM-Zone
     */
    public UTMPoint utm;
    
      /**
       * empty constructor
       */
        public MapNode() {
            
	}
	/**
	 * @param id the id number of the node
	 * @param utm the Point with x- and y-coordinates and the UTM-Zone
	 * @param tag the hashtable
	 */
	public MapNode(int id, UTMPoint utm, Hashtable<String, String> tag) {
		this.id = id;
		this.utm = utm;
		this.tag = tag;
		
	}
	/**
	 * @return the id-number from the node
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id from the node
	 */
	public void setId(int id) {
		this.id = id;
	}
	

	/**
	 * @return the UTMPoint from the node
	 */
	public UTMPoint getUtm() {
		return utm;
	}
	/**
	 * @param utm the Point with x- and y-coordinates and the UTM-Zone to set
	 */
	public void setUtm(UTMPoint utm) {
		this.utm = utm;
	}
	/**
	 * 
	 * @return the x-coordinate from the UTMPoint as integer
	 */
	public int getX_Coordinate(){
		int x = (int) this.utm.getX();
		return x;
		
	}
	/**
	 * 
	 * @return the y-coordinate from the UTMPoint as integer
	 */
	public int getY_Coordinate(){
		int y = (int) this.utm.getY();
		return y;
	}
	/**
	 * set the UTM-Point
         * 
	 * @param x the x-coordinate from the Point as integer
	 * @param y the y-coordinate from the Point as integer
	 */
	public void setPoint(int x, int y){
		this.utm.Set(x, y, 32);
	}
	
	/**
	 * @return the hashtable with strings
	 */
	public Hashtable<String, String> getTag() {
		return tag;
	}
	/**
	 * @param tag the hashtable to set
	 */
	public void setTag(Hashtable<String, String> tag) {
		this.tag = tag;
	}
	/**
	 * add Arguments in a hastable
         * 
	 * @param key  the key to set
	 * @param value the value to set
	 */
        public void addTag(String key, String value) {
	        this.tag.put(key, value);
	    }
        
	@Override
	public String toString() {
		return "MapNode [id=" + id + ", tag=" + tag + ", utm=" + utm + "]";
	}
    
        /**
         * 
         * @param distance the minimum distance to the next node
         */
        public void setDistance(int distance){
                this.minDistanz = distance;
        }
        /**
        * @return the minimum distance to the next node
        */
        public int getDistance() {
        	return minDistanz;
        }
        /**
         * 
         * @param speed the speed in km/h
         */
        public void setSpeed(int speed){
        	this.speed = speed;
        }
        /**
        * @return the speed
        */
        public int getSpeed() {
               return speed;
}
    
	
	
	
	

}
