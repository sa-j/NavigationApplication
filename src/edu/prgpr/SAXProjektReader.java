package edu.prgpr;



import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import javax.swing.JFrame;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import edu.prgpr.Coordinates.UTMPoint;
import edu.prgpr.Coordinates.WGS84Point;

/**
*
* @author Saj, Joanna
*/

 
public class SAXProjektReader extends DefaultHandler {
	 static Graph graph = new Graph();
    /**
     * read, parse, save the XML-date
     */
         
    /**
     * @param bnode flag for node tag: if opened (i.e. <node>) then true else (i.e. </node>) false 
     */
    boolean bnode = false;
    /** 
     * @param bway flag for way tag: if <way> then true else (i.e. </way>) false
     */
    boolean bway = false;
    /**
     * @param nodes the list of the Nodes
     */
    ArrayList<MapNode> nodes = new ArrayList<MapNode>();
    /** 
     *@param ways list of Ways
     */
    ArrayList<MapWay> ways = new ArrayList<MapWay>();

     /**
     * Override methods of the DefaultHandler class
     * to gain notification of SAX Events.
     * @see org.xml.sax.ContentHandler for all available events.
     */
      public void startElement( String namespaceURI,String localName,String qName,Attributes attr ) 
				throws SAXException {
	
     /**
      * For each node we create a hashtable. It can also be empty if the node does not have any tags.
      * We create also a UTMPoint from lat and lon Coordinates.
      */
     if (localName.equals("node")) {
    	 this.bnode = true;
    	 
        UTMPoint utm;
    	double latDeg = java.lang.Double.parseDouble(attr.getValue("lat"));
   	  	double lonDeg = java.lang.Double.parseDouble(attr.getValue("lon"));
   	  	WGS84Point wgs = new WGS84Point();
   	  	wgs.SetDegrees(latDeg, lonDeg);
   	  	utm = new UTMPoint();
   	    utm.SetZone(32);
   	    Coordinates.convertWGS84toUTM(wgs, utm);
   	  		
    	 Hashtable<String, String> tag1 = new Hashtable<String, String>();
    	 
         this.nodes.add(new MapNode(Integer.parseInt(attr.getValue(0)), utm, tag1));
     }
     /**
      * If we have a tag in the node then we add it to the table.
      */
     if (localName.equals("tag") && this.bnode == true) {
    	 this.nodes.get(this.nodes.size()-1).addTag(attr.getValue(0), attr.getValue(1));
     }
     /**
      * After reading of a way we set bway to true and we create a hashtable and a ndlist.
      */
     if (localName.equals("way")) {
         this.bway = true;
         
         Hashtable<String, String> tag = new Hashtable<String, String>();
         
         ArrayList<Integer> nd = new ArrayList<Integer>();
         
         this.ways.add(new MapWay(Integer.parseInt(attr.getValue(0)), nd, tag));
     
     }
     /**
      * After reading of a tag is it added into the taglist
      */
     if (localName.equals("tag") && this.bway == true) {
    	 this.ways.get(this.ways.size()-1).addTag(attr.getValue(0), attr.getValue(1));
     }
    /**
     * After reading of a nd it is added into the ndlist
     */
	 if (localName.equals("nd") && this.bway == true) {
         this.ways.get(this.ways.size()-1).addNd(Integer.parseInt(attr.getValue(0)));
          }
}


      public void endElement( String namespaceURI,String localName,String qName ) 
	           throws SAXException {
      /**
       * After reading the nodes, we set  bnode to false
       */
      if (localName.equals("node")) {
	          this.bnode = false;
	      }
      /**
        *  if way has lesser nd that 2 than this way is deleted
	*  After reading the nodes, we set  bway to false
	*/
       if (localName.equals("way")) {
            if (this.ways.get(this.ways.size()-1).nd.size()<=1) {
	              this.ways.remove(this.ways.size()-1);
                }
	    this.bway = false;
	          
	      }
	   }

      /**
      * Parse the file...
      */
	    public void MapParser() {
	    	
	        try {

	         /**
	          *  Create SAX  parser...
	          */
	         XMLReader xmlReader = XMLReaderFactory.createXMLReader();

	         /**
	          * Set the ContentHandler...
	          */
	         xmlReader.setContentHandler(this);

	         xmlReader.parse(new InputSource(new FileReader("prg-frankfurt.osm")) );
	
	        } catch ( Exception e ) {
                     e.printStackTrace();
	      }
       }

	  /**
           * 
           * @param g graph there to converted is
           * converts list of nodes to a hashmap with id as a key and a node as a value
           */
	   public void nodeListToHashMap(Graph g){
		   for (MapNode node : this.nodes){
			   g.addNode(node);
		   }
            }
	   /**
            * 
            * @param g graph there to converted is
            * converts list of ways to a list of ways with tag "highway"
            */
	   public void wayListToHashMap(Graph g){
		   for (MapWay way : this.ways){
			  g.addWay(way);
                          if (way.getTag().containsKey("highway")){
                              for (int i=0; i<=way.getNd().size()-1;i++) {
                                    g.highwayNodeList.add(g.hashNodes.get(way.getNd().get(i)));
                              }
                       }
		   }
                   // delete the double entries
                   HashSet<MapNode> h = new HashSet<MapNode>(g.highwayNodeList);
                   g.highwayNodeList.clear();
                   g.highwayNodeList.addAll(h);
            }    
	   /**
            * 
            * @param graph the graph there we change while it is to big
            * @return smallest graph 
            */
	   public Graph ChangeGraph(Graph graph){
		   /**
		    * calculate the smallest value of the coordinates
		    */

	        int mX = Integer.MAX_VALUE;
	        int mY = Integer.MAX_VALUE;

	        for (MapNode node : graph.getHashNodes().values()){
	            if (node.getX_Coordinate() < mX){
	                mX = (int) node.getX_Coordinate();
	            }

	            if (node.getY_Coordinate() < mY){
	                mY = (int) node.getY_Coordinate();
	            }
	        }
                for (MapNode node : graph.getHashNodes().values()){
	        	UTMPoint point = new UTMPoint();
	        	point.Set((node.getX_Coordinate() - mX), node.getY_Coordinate() - mY, 32);
	        	node.setUtm(point);
                }

	        for (MapWay way : graph.getHashWays().values()){
                    ArrayList<MapNode> helpList = new ArrayList<MapNode>();
	          
	            for (MapNode node : way.getWayNodes()) {
	            	
	                MapNode convert = graph.getHashNodes().get(node.getId());
	                UTMPoint point = new UTMPoint();
	                point.Set(convert.getX_Coordinate() , convert.getY_Coordinate(), 32);
	                helpList.add(node);
	            }
	         way.setWayNodes(helpList);
	        }
                 return graph;
             } 
	   /**
	    * we make in our ways a list of nodes
	    * 
	    */
	   public void ndToMapNode(Graph g){
		for(int i=0;i<=this.ways.size()-1;i++){
			ArrayList<MapNode> wayNodes = new ArrayList<MapNode>();
                        for(int j=0;j<=this.ways.get(i).nd.size()-1;j++){
                            if(g.getHashNodes().containsKey(ways.get(i).nd.get(j))){
				wayNodes.add(g.getIdNode(ways.get(i).getNd().get(j)));
                            }
			this.ways.get(i).setWayNodes(wayNodes);
			}
		}
            } 
       
	public static void main( String[] args ){
	      /**
	       * create a parser
	       */
	      SAXProjektReader reader = new  SAXProjektReader();
	      /**
               * parse the file
               */
              reader.MapParser();
              /**
               * create a HashMap with nodes in graph
               */
              reader.nodeListToHashMap(graph);
              /**
               * create a HashMap with ways in graph
               */
              reader.wayListToHashMap(graph);
              /**
               * create a list with nodes in all ways 
               */
              reader.ndToMapNode(graph);
              /**
               * convert the graph
               */
              graph = reader.ChangeGraph(graph);
              /**
               * create a frame
               */
	      Frame frame = new Frame(graph);
	      frame.setSize(800, 600);
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.setVisible(true); 
           }
           
           
	  

	   
}

