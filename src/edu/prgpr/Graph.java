package edu.prgpr;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * 
 * @author Saj, Joanna
 */
public class Graph {
        /**
         * The class represents a graph with nodes and ways
         */
        
        /**
         * HashMap with id-number of the node as key and node as a value
         */
	public HashMap<Integer , MapNode> hashNodes;
        /**
         * HashMap with id-number of the way as key and way as a value
         */
	public HashMap<Integer , MapWay> hashWays;
        /**
         * need for sort of the distancen
         */
	public Comparator<MapNode> comparator;
        /**
         * queue heap for algo1 with distancen
         */
        public PriorityQueue<MapNode> queue;
        /**
         * HashTable as a faterarray
         */
	public HashMap<MapNode,MapNode> parentTable = new HashMap<MapNode,MapNode>();
       /**
         * need for sort of the time
         */
        public Comparator<MapNode> comparatorTime;
        /**
         * queue heap for algo2 with time
         */
        public PriorityQueue<MapNode> queueTime;
        /**
         * list of nd numbers there in highway are
         */
        public ArrayList<Integer> highwayList= new ArrayList<Integer>();
        /**
         * list of the nodes there in a highway are 
         */
        public ArrayList<MapNode> highwayNodeList= new ArrayList<MapNode>();
         /**
          * list for nearesWay class
          * list of Way there nearby from a node are
          */
         public ArrayList<MapWay> waysListNear = new ArrayList<MapWay>();
        /**
	 * constructor
	 * Create a new Hashtables with MapNodes und MapWays
	 */
	public Graph(){
		hashNodes = new HashMap<Integer , MapNode>();
		hashWays = new HashMap<Integer , MapWay>();
		comparator = new CompareMapNode();
		queue = new PriorityQueue<MapNode>(100,comparator);
                comparatorTime = new CompareMapNodeTime();
		queueTime = new PriorityQueue<MapNode>(100,comparator);
        }
	/**
	 * @return the nodes in a hashMap
	 */
	public HashMap<Integer, MapNode> getHashNodes() {
		return hashNodes;
	}
	/**
	 * @param hashNodes the list of nodes to set in a hashMap
	 */
	public void setHashNodes(HashMap<Integer, MapNode> hashNodes) {
		this.hashNodes = hashNodes;
	}
	/**
	 * @return the ways in a hashMap
	 */
	public HashMap<Integer, MapWay> getHashWays() {
		return hashWays;
	}
	/**
	 * @param hashWays the list of ways to set in a hashMap
	 */
	public void setHashWays(HashMap<Integer, MapWay> hashWays) {
		this.hashWays = hashWays;
	}
	/**
	 * Added a Node in the hashmap
	 * @param node Node from the map
	 */
	public void addNode(MapNode node){
		this.hashNodes.put(node.getId(),node);
	}
	/**
	 * 
	 * @param id the id-number of a node
	 * @return a MapNode from the given id
	 */
	public MapNode getIdNode(int id){
		return hashNodes.get(id);
		
	}
	/**
	 * 
	 * @param id the id-number of a way
	 * @return a MapWay from the given id
	 */
	public MapWay getWayId(int id){
		return hashWays.get(id);
		
	}
	/**
	 * Added a Way in the hashmap 
	 * @param way the way
	 */
	public void addWay(MapWay way){
		this.hashWays.put(way.getId(),way);
	}
        //####################### ALGO SHORTEST PATH ###################################
        /**
         * 
         * @param id the id-number of a MapNode
         * @return the list of MapNodes there are neighbours of the MapNode with id-number id
         */
	public ArrayList<MapNode> getAllNeighbours(int id){
		ArrayList<MapNode> neighboursList = new ArrayList<MapNode>();
		for (MapWay way : this.getHashWays().values()) {
			if (way.getTag().containsKey("building")){
                            
                        }
                        else {
                            if(way.wayNodes.get(0).getId() == id) neighboursList.add(way.wayNodes.get(1));
                            if(way.wayNodes.get(way.wayNodes.size()-1).getId() == id){
                                neighboursList.add(way.wayNodes.get(way.wayNodes.size()-2));
                            }
                            else{
                                for(int i=1;i<=way.wayNodes.size()-2;i++){
                                     if(way.wayNodes.get(i).getId() == id) {
                                          neighboursList.add(way.wayNodes.get(i-1));
                                          neighboursList.add(way.wayNodes.get(i+1));
                                          break;
                                            }
                                    }

                            }
                        }

			
		}
		//remove double entries
		HashSet<MapNode> h = new HashSet<MapNode>(neighboursList);
		neighboursList.clear();
		neighboursList.addAll(h);
                
                return neighboursList;
	}
	/**
         * 
         * @param from first MapNode
         * @param to   second MapNode
         * @return the leanght of the edge between first and second MapNode
         */
	public double calculateLenght(MapNode from, MapNode to){
		double dy = from.getY_Coordinate() - to.getY_Coordinate();
		double dx = from.getX_Coordinate() - to.getX_Coordinate();
                
                return (double)Math.sqrt(dx * dx + dy * dy); 
		}
        /**
         * 
         * @param node node from which we calculate the distance
         * @return the list of distancen of the given node
         */
	public ArrayList<MapNode> getDistanceList(MapNode node){
            
	    ArrayList<MapNode> nlist = new ArrayList<MapNode>();
	    nlist = this.getAllNeighbours(node.getId());
            
	    for (int i=0;i<=nlist.size()-1;i++) {
	    	
	    	int distance = (int)this.calculateLenght(node, nlist.get(i));
	    	distance = distance + node.minDistanz;
                if (nlist.get(i).getDistance()<distance) {  
                     if(nlist.get(i).getDistance() == Integer.MAX_VALUE){
                         nlist.remove(i);
                     }
	    	}else{
	        nlist.get(i).setDistance(distance); 
	        nlist.get(i).Predecessor = node;
	      }
	    }
	    return nlist;
	}
      /**
       * inserts the neighbours of the given node into queue
       * @param node MapNode 
        */
	public void insertInQueue(MapNode node){
		ArrayList<MapNode> nDisList = new ArrayList<MapNode>();
		nDisList =this.getDistanceList(node);
		for (int i=0;i<=nDisList.size()-1;i++){
                    if (!nDisList.get(i).reviewed){
                        nDisList.get(i).reviewed=true;
                        this.queue.add(nDisList.get(i));                        
                    }
		}
        }
        /**
         * The algo calculate the shortest path
         * 
         * @param start the start node
         * @param destination the last node of the route
         * @return the list of the nodes there to one route belong
         */
	public ArrayList<MapNode> Algo(MapNode start,MapNode destination){
            parentTable.clear();
            parentTable.put(start,start);
            start.setDistance(0);
            start.reviewed = true;
            this.insertInQueue(start);
            MapNode out = this.queue.poll();
            this.parentTable.put(out, out.Predecessor);
            while (out.getId()!=destination.getId() && !this.queue.isEmpty()) {
                this.insertInQueue(out);
                out = this.queue.poll(); 
                this.parentTable.put(out, out.Predecessor);
            }
            ArrayList<MapNode> path = new ArrayList<MapNode>();
            path.add(destination);
            while(this.hashNodes.get(path.get(path.size()-1).getId()).getId() != 
                  this.parentTable.get(this.hashNodes.get(path.get(path.size()-1).getId())).getId()){
                path.add( this.parentTable.get(this.hashNodes.get(path.get(path.size()-1).getId())) );
            }
            return path;
            
          }
        
        //############################# ALGO SHORTEST TIME #####################################
        /**
         * 
         * @param the id-number of a MapNode
         * @return the list of MapNodes there are neighbours of the MapNode with id-number id
         */
        public ArrayList<MapNode> getAllNeighboursSpeed(int id){
        ArrayList<MapNode> neighboursList = new ArrayList<MapNode>();
        for (MapWay way : this.getHashWays().values()) {
               if (way.wayNodes.get(0).getId() == id) {
                    if (way.getTag().containsKey("maxspeed")){
                        way.wayNodes.get(1).speed = Integer.parseInt(way.getTag().get("maxspeed"));
                    }
                    neighboursList.add(way.wayNodes.get(1));
                }
                if(way.wayNodes.get(way.wayNodes.size()-1).getId() == id){
                    if(way.getTag().containsKey("maxspeed")){
                        way.wayNodes.get(way.wayNodes.size()-1).speed = Integer.parseInt(way.getTag().get("maxspeed"));
                    }
                    neighboursList.add(way.wayNodes.get(way.wayNodes.size()-2));
                }else {
                        for(int i=1;i<=way.wayNodes.size()-2;i++){
                                if(way.wayNodes.get(i).getId() == id){
                                        if (way.getTag().containsKey("maxspeed")) {
                                            way.wayNodes.get(i-1).speed=Integer.parseInt(way.getTag().get("maxspeed"));
                                            way.wayNodes.get(i+1).speed=Integer.parseInt(way.getTag().get("maxspeed"));
                                        }
                                        neighboursList.add(way.wayNodes.get(i-1));
                                        neighboursList.add(way.wayNodes.get(i+1));
                                        break;
                                }
                        }

                }
            


        }
        //remove double entries
        HashSet<MapNode> h = new HashSet<MapNode>(neighboursList);
        neighboursList.clear();
        neighboursList.addAll(h);
        
        return neighboursList;
    }
        /**
         * 
         * @param node from which we calculate the speed
         * @return the list of speeds of the given node
         */
        public ArrayList<MapNode> getSpeedList(MapNode node){
	    ArrayList<MapNode> nlist = new ArrayList<MapNode>();
	    nlist = this.getAllNeighbours(node.getId());
	    for(int i=0;i<=nlist.size()-1;i++){
	    	int distance = (int)this.calculateLenght(node, nlist.get(i));
	    	distance = distance + node.minDistanz; 
                int time = (distance/nlist.get(i).getSpeed());
                if (!nlist.get(i).reviewed) {
                    nlist.get(i).timeValue = (time); // set TIMEVALUE instead of distanceValue
                    nlist.get(i).setDistance(distance); // AND ALSO SET DISTANDE
                    nlist.get(i).Predecessor = node; // AND set predecessor                    
                }else{
                    if(nlist.get(i).timeValue<time){  // default = infinity
                        if(nlist.get(i).getDistance()==Integer.MAX_VALUE){
                            nlist.remove(i);
                        }
                    }else{
                        nlist.get(i).timeValue=(time);; // set TIMEVALUE instead of distanceValue
                        nlist.get(i).setDistance(distance);
                        nlist.get(i).Predecessor = node;
                        // AND set predecessor                    
                    }                    
                }
	    }
	    return nlist;
	}


        /**
         * inserts the neighbours of the given node into queue
         * @param node MapNode  
         */
        public void insertInQueueTime(MapNode node){
            ArrayList<MapNode> nDisList = new ArrayList<MapNode>();
            nDisList = this.getSpeedList(node);
            for (int i=0;i<=nDisList.size()-1;i++){
                if (!nDisList.get(i).reviewed){
                    nDisList.get(i).reviewed=true;
                    this.queueTime.add(nDisList.get(i));                        
                }
            }
        }

        /**
         * The algo calculate the shortest time 
         * 
         * @param start the start node
         * @param destination the last node of the route
         * @return the list of the nodes there to one route belong
         */
	public ArrayList<MapNode> AlgoSpeed(MapNode start,MapNode destination){
            parentTable.clear();
            parentTable.put(start, start);
            start.setDistance(0);
            start.reviewed=true;
            this.insertInQueueTime(start);
            MapNode out = this.queueTime.poll();
            this.parentTable.put(out, out.Predecessor);
            while(out.getId()!= destination.getId()){ // or if IDs are equal
                this.insertInQueueTime(out);
                out = this.queueTime.poll(); 
                this.parentTable.put(out, out.Predecessor);
            }
            //if finished return the parentTable which contaisn the shortest path
            ArrayList<MapNode> path = new ArrayList<MapNode>();
            path.add(destination);
            // while (key!= value) do
            while(this.hashNodes.get(path.get(path.size()-1).getId()).getId() != 
                  this.parentTable.get(this.hashNodes.get(path.get(path.size()-1).getId())).getId()){
                path.add( this.parentTable.get(this.hashNodes.get(path.get(path.size()-1).getId())) );
            }
            return path;
          }
        
        /**
         * reset all nodes 
         */
        public void resetAllNodes() {
            for (MapNode node : this.hashNodes.values()) {
                node.Predecessor = null;
                node.minDistanz = Integer.MAX_VALUE;
                node.reviewed = false;
                node.timeValue = Integer.MAX_VALUE;
            }
        }


}
		
	
	

	
	
	
	

