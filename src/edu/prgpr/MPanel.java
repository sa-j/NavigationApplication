
package edu.prgpr;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * @author Saj, Joanna
 *
 */
public class MPanel extends JPanel implements MouseMotionListener{

	/**
         * The class represents a map of Frankfurt with the buildings and the ways
          */
	private static final long serialVersionUID = 1L;
        /**
         * zoomfactor for our map
         */
	public int zoom = 4;
        /**
         * the graph with nodes and ways 
         */
	private Graph cityGraph ;
        /**
         * if showPoint is true then print the point
         */
	public boolean showPoint = false;
        /**
         * the coordinates from the point
         */
	int[] coordinate = new int[2];
        /**
         * the list where are the nodes from the shortest or fastest way
         */
	ArrayList<MapNode> path = new ArrayList<MapNode>();
        /**
         * the point until which path is simulated
         */
        int simu = 0;
        /**
         * the lengh of the shortest or fastest way
         */
        double pathLenght;
        /**
         * the list of the distances of the nodes in a shortest or fastest way
         */
        ArrayList<Double> lenghtArray = new ArrayList<Double>();
        /**
        * The mouse configurations
        */
        public int maxX = 4000;
        public int maxY = 4000;
        /**
        * need for moving the map
        */
        public int startX = Integer.MIN_VALUE; //-1 = not started moving yet
        public int startY = Integer.MIN_VALUE; //-1 = not started moving yet
        public int moveX = -3000;
        public int moveY = 1600;
        public int oldMoveX;
        public int oldMoveY;

/**
 * constructor
 * @param graph the graph with the ways und nodes
 */
        public MPanel(Graph graph) {
		cityGraph = graph;

	}


    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D graphic = (Graphics2D) g;
        graphic.setColor(Color.WHITE);
        graphic.fillRect(0, 0, 5500, 5500);
        /**
         * transform the graph
         */

        AffineTransform transform = new AffineTransform();
        transform.scale((double) zoom / 15, (double) zoom / 15);
        transform.translate(moveX, moveY);
        graphic.setTransform(transform);
        /**
         * print the landuse
         */
        this.printObject("landuse", Color.getHSBColor(40,250,201), graphic);
        /**
         * print the buildings
         */
        this.printObject("building", Color.LIGHT_GRAY, graphic);
         /**
         * print the hospital or police or public building or courthouse
         */
        this.printObject("amenity", Color.getHSBColor(255,255,100), graphic);
         /**
         * print the green area
         */
        this.printObject("area",Color.getHSBColor(40,250,210), graphic);
         /**
         * print the leisure
         */
        this.printObject("leisure", Color.getHSBColor(50, 255, 50), graphic);

        /**
         * print the river
         */
        this.printObjectWith2Arguments("waterway","riverbank", Color.BLUE, graphic);
        
        /**
         * print the river
         */
        this.printObjectWith2Arguments("natural","water", Color.BLUE, graphic);
         /**
         * print the maininsel
         */
        this.printObjectWith2Arguments("name","Maininsel", Color.getHSBColor(40,250,215), graphic);
        
        /**
         * print all the ways
         */
        graphic.setColor(Color.DARK_GRAY);  
        for (MapWay way : cityGraph.getHashWays().values()){
        	for(int i=0;i<=way.getWayNodes().size()-2;i++){
        		graphic.drawLine(way.getWayNodes().get(i).getX_Coordinate(),2500-way.getWayNodes().get(i).getY_Coordinate() ,
        				way.getWayNodes().get(i+1).getX_Coordinate(), 2500-way.getWayNodes().get(i+1).getY_Coordinate());
        		
        	}	
        }

        /**
         * print the highway
         */
       this.printWay("highway", Color.getHSBColor(255, 100, 200), graphic);
       /**
        * print the waterway
        */
       this.printWayWith2Arguments("waterway","river",Color.CYAN,graphic);
       /**
        * print the point where we search with findAdress 
        */
        if(showPoint) {
        	graphic.setColor(Color.RED);
        	graphic.fillOval(coordinate[0], (2500-coordinate[1]), 50 , 50);       	
        }
        /**
         * 
         * print the shortest or fastest way
         */
        graphic.setColor(Color.RED);
        graphic.setStroke( new BasicStroke( 12 ) );
        for(int i=0;i<=path.size()-2;i++){
            graphic.drawLine(path.get(i).getX_Coordinate(),2500-path.get(i).getY_Coordinate() ,
                path.get(i+1).getX_Coordinate(), 2500-path.get(i+1).getY_Coordinate());
            }
        /**
         * print the simulation
         */
        graphic.setColor(Color.BLACK);

        for(int i=0;i<=simu-2;i++){
            graphic.drawLine(path.get(i).getX_Coordinate(),2500-path.get(i).getY_Coordinate() ,
                path.get(i+1).getX_Coordinate(), 2500-path.get(i+1).getY_Coordinate());
            }
 
    }
 
/**
 * 
 * @param street the searched street
 * @param housenumber the housenumber of the searched street
 */
    public void findAdress(String street,String housenumber){
    	for(MapNode node: cityGraph.getHashNodes().values()){  		
    		if (node.getTag().containsKey("addr:street") && node.getTag().get("addr:street").equals(street)){	
    			if(node.getTag().containsKey("addr:housenumber") && node.getTag().get("addr:housenumber").equals(housenumber)){
    				coordinate[0] = node.getX_Coordinate();
    				coordinate[1] = node.getY_Coordinate();
    				showPoint = true;
                                break;
    			}
    		}
    	}
        repaint();
    }
    /**
     * 
     * @param street the searched street
     * @param housenumber the housenumber of the searched street
     * @return the MapNode from the searched adress
     */
    public MapNode findAdressNode(String street, String housenumber){
            MapNode nd = new MapNode();
            for(MapNode node: cityGraph.getHashNodes().values()){
    		if (node.getTag().containsKey("addr:street") && node.getTag().get("addr:street").equals(street)){
    			if(node.getTag().containsKey("addr:housenumber") && node.getTag().get("addr:housenumber").equals(housenumber)){
                            nd = node;
                            break;
    			}
    		}
            }
            return nd;

    }
    /**
     * 
     * @param street the searched street
     * @param housenumber the housenumber of the searched street
     * @return the MapWay from the searched adress
     */
    public MapWay findAdressWay(String street, String housenumber){
            MapWay w = new MapWay();
            for(MapWay way: cityGraph.getHashWays().values()){
    		if (way.getTag().containsKey("addr:street") && way.getTag().get("addr:street").equals(street)){
    			if(way.getTag().containsKey("addr:housenumber") && way.getTag().get("addr:housenumber").equals(housenumber)){
                            w = way;
                            break;
    			}
    		}
            }
            return w;

    }
/**
 * 
 * @param pathList the list of the nodes in the shortest or fastest way
 */
    public void setPath(ArrayList<MapNode> pathList) {
        path = pathList;
        repaint();
    }

/**
 * 
 * @return the dimension
 */
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(5500, 5500);
    }

    /**
     * @return the zoom
     */
    public int getZoom()
    {
        return zoom;
    }

    /**
     * @param zoomf the zoomfactor to set
     */
    public void setZoom(int zoomf ) 
    {
        int zm=-zoomf;
        if (zoom + zm  < 1)
        {
            zoom = 1;
        }
        else if (zoom + zm  > 20)
        {
        	zoom = 20;
        }
        else
        {
        	zoom +=  zm  ;
        }
    }


   
    @Override
    public void mouseDragged(MouseEvent e) {
        /**
         * move the map
         */
        if(startX == Integer.MIN_VALUE && startY == Integer.MIN_VALUE){
            startX = e.getX();
            startY = e.getY();
            oldMoveX = moveX;
            oldMoveY = moveY;
        }
        if(oldMoveX - startX + e.getX() > this.getWidth() - maxX/zoom) moveX = oldMoveX - startX + e.getX();
        if(moveX > 0) moveX = 0;
        if(oldMoveY - startY + e.getY() > this.getHeight() - maxY/zoom) moveY = oldMoveY - startY + e.getY();
        if(moveY > 0) moveY = 0;
        repaint();
    } // better option: use the scrollPanel to dragg or moven the map !!!

    @Override
    public void mouseMoved(MouseEvent e) {
        //no operation
    }
        /**
     * Printed the ways
     * @param string the waytype
     * @param color the color of the way
     * @param g the graphictype
     */
    public void printWay(String key, Color color, Graphics2D g){
        g.setColor(color);  
        for (MapWay way : cityGraph.getHashWays().values()){
            if(way.getTag().containsKey(key)){
        	for(int i=0;i<=way.getWayNodes().size()-2;i++){
                        g.setStroke( new BasicStroke( 9 ) );
        		g.drawLine(way.getWayNodes().get(i).getX_Coordinate(),2500-way.getWayNodes().get(i).getY_Coordinate() ,
        			   way.getWayNodes().get(i+1).getX_Coordinate(), 2500-way.getWayNodes().get(i+1).getY_Coordinate());
                }
        	}	
        }
        
        
    }
    /**
     * Printed the ways
     * @param string the waytype
     * @param color the color of the way
     * @param g the graphictype
     */
    public void printWayWith2Arguments(String key, String value, Color color, Graphics2D g){
        g.setColor(color);  
        for (MapWay way : cityGraph.getHashWays().values()){
            if(way.getTag().containsKey(key)&& way.getTag().get(key).equals(value)){
        	for(int i=0;i<=way.getWayNodes().size()-2;i++){
                        g.setStroke( new BasicStroke( 9 ) );
        		g.drawLine(way.getWayNodes().get(i).getX_Coordinate(),2500-way.getWayNodes().get(i).getY_Coordinate() ,
        			   way.getWayNodes().get(i+1).getX_Coordinate(), 2500-way.getWayNodes().get(i+1).getY_Coordinate());
                }
        	}	
        }
        
        
    }
    /**
     * Print the objects 
     * 
     * @param string the type of the object
     * @param color the color of the object
     * @param g graphics
     */
    public void printObject(String string, Color color, Graphics2D g){
        g.setColor(color);
        for(MapWay way : cityGraph.getHashWays().values()){
            if(way.getTag().containsKey(string)){
	        	int size = way.getWayNodes().size();
	        	int[] arrayXPoints = new int[size];
                        int[] arrayYPoints = new int[size];        	
	        	for(int i=0;i<=way.getWayNodes().size()-1;i++){
        			arrayXPoints[i]=(int)way.getWayNodes().get(i).getX_Coordinate();
        			arrayYPoints[i]=(int)2500-way.getWayNodes().get(i).getY_Coordinate();       			   				        		
	        	}
	        	
	        g.fillPolygon(arrayXPoints,arrayYPoints,arrayXPoints.length);
	        	
        	}

        }
        
    }
    /**
     * printing from objects
     * @param key the key in the waytag where we are printing
     * @param value the value in the waytag where we are printing
     * @param color the color of the object
     * @param g graphics
     */
       public void printObjectWith2Arguments(String key, String value, Color color, Graphics2D g){
        g.setColor(color);
        for(MapWay way : cityGraph.getHashWays().values()){
            if(way.getTag().containsKey(key)&& way.getTag().get(key).equals(value)){
	        	int size = way.getWayNodes().size();
	        	int[] arrayXPoints = new int[size];
                        int[] arrayYPoints = new int[size];        	
	        	for(int i=0;i<=way.getWayNodes().size()-1;i++){
        			arrayXPoints[i]=(int)way.getWayNodes().get(i).getX_Coordinate();
        			arrayYPoints[i]=(int)2500-way.getWayNodes().get(i).getY_Coordinate();       			   				        		
	        	}
	        	
	        g.fillPolygon(arrayXPoints,arrayYPoints,arrayXPoints.length);
	        	
        	}

        }
        
    }

    /**
     * 
     * @param path the list of the nodes there are in a shortest or fastest path
     * @return a list of lenght of the edges there in the shortest or fastest path
     */
    public ArrayList<Double> calculatePathLenght(ArrayList<MapNode> path){
        this.lenghtArray.clear();
        for (int i=0; i<=path.size()-2;i++){
            lenghtArray.add(cityGraph.calculateLenght(path.get(i), path.get(i+1)));
        }

        return lenghtArray;

    }
    /**
     * 
     * @param lArray a list of distances of the nodes
     * @return the distance
     */
    public double giveDistance(ArrayList<Double> lArray){
        double sum = 0;
        for(int i=0;i<=lArray.size()-1;i++){
            sum += lArray.get(i);
        }

        this.pathLenght = sum;
        return sum;
    }
    /**
     * 
     * @return the gemaining distance
     */
    public double remainingDistance(){
        double sum = 0;
        for(int i=0;i<=this.simu-2;i++){
            sum += this.lenghtArray.get(i);
        }
        sum = this.pathLenght - sum;
        return sum;
    }
  
}





