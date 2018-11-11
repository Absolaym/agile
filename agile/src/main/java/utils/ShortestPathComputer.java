package utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Section;
import model.Trip;

public class ShortestPathComputer {
	
	private CityMap cityMap;
	private DeliveryRequest deliveryRequest;

	private HashMap<String, Node> nodes;
	private HashMap<String,HashMap<String,Trip>> shortestPaths;		
	
	public ShortestPathComputer(){
		
	}
	
	public void init(CityMap aCityMap, DeliveryRequest aDeliveryRequest){
		this.cityMap = aCityMap;
		this.deliveryRequest = aDeliveryRequest;

		
		this.nodes = new HashMap<String, Node>();
		for(Intersection intersection : cityMap.getIntersections().values())
			nodes.put(intersection.getId(), new Node(intersection));
		
		for(Section section : cityMap.getSections()){
			Node startNode = nodes.get(section.getStartIntersection().getId());
			Node endNode = nodes.get(section.getEndIntersection().getId());
			Link newLink = new Link(section, startNode, endNode);
			nodes.get(section.getStartIntersection().getId()).addLink(newLink);
		}
				
	}
	
	public void computeAllShortestPaths() { //formerly: execute
		
		LinkedList<Delivery> deliveries = deliveryRequest.getDeliveries();
		this.shortestPaths = new HashMap<String, HashMap<String, Trip>>();

		for(Delivery delivery : deliveries) {
			setShortestPathsForDelivery(delivery, deliveries);
		}
		return;
	}

	public void setShortestPathsForDelivery(Delivery delivery, LinkedList<Delivery> deliveries){
		// Establish the shortest paths between warehouse and deliveries
		Node origin = this.nodes.get( deliveryRequest.getWarehouseAddress() );
		Node target = this.nodes.get( delivery.getAddress() );
		
		IntermediateResult inter = this.cleanCosts().dijkstra( origin ).resolveDijkstra( origin, target );
		inter.computeLength();
		addShortestPath(origin, target, inter);
		
		// Establish the shortest paths between deliveries and warehouse
		inter = this.cleanCosts().dijkstra( target ).resolveDijkstra( target, origin );
		inter.computeLength();
		addShortestPath(target, origin, inter);
		
		// Establish the shortest paths between deliveries
		for(Delivery delivery2 : deliveries) {
			
			if(delivery == delivery2) {
				continue;
			}
			
			origin = this.nodes.get( delivery2.getAddress() );
			inter = this.cleanCosts().dijkstra( origin ).resolveDijkstra( origin, target );
			inter.computeLength();
			addShortestPath(target, origin, inter);				
		}
	}
	
	public HashMap<String, HashMap<String, Trip>> result() {
		return this.shortestPaths;
	}
	
	private void addShortestPath(Node origin, Node target, IntermediateResult inter) {
		HashMap<String, Trip> innerMap= this.shortestPaths.get(origin.intersection.getId());
		if(innerMap == null)
			innerMap = new HashMap<String, Trip>();
		Trip trip = new Trip();
		trip.setSections(inter.path);
		innerMap.put(target.intersection.getId(), trip);
		this.shortestPaths.put(origin.intersection.getId(), innerMap);
	}
	//-------------Dijkstra section---------------
	
	private ShortestPathComputer cleanCosts(Map<String,Node> nodes) {
		for(Node node : nodes.values())			node.cost = 2e300;
		return this;
	}
	private ShortestPathComputer cleanCosts() {
		return cleanCosts(this.nodes);
	}
	private ShortestPathComputer dijkstra(Node origin) {
		Queue<Node> queue = new LinkedList<Node>();
		
		origin.cost = 0;
		queue.add( origin );
		
		while(!queue.isEmpty()) {
			Node node = queue.remove();
			for(Link l : node.links) {
				double cost = l.getLength() + node.cost;
				if(cost >= l.endNode.cost) continue;
				
				l.endNode.cost = cost;
				l.endNode.previous = l;
				queue.add( l.endNode );
			}
		}
		return this;
	}
	private IntermediateResult resolveDijkstra(Node origin, Node target) {
		IntermediateResult inter = new IntermediateResult();
		
		inter.start = origin;
		inter.end = target;
			
		LinkedList<Link> links = new LinkedList<Link>();
			
		Node node = target;
		while(node.previous != null && node != origin) {
			links.addFirst( node.previous );
			inter.path.addFirst( node.previous.section );
			node = node.previous.startNode;
		}
		
		return inter;
	}
	
	

//------------------------------Inner classes --------------------------
	
	private class Node {
		Intersection intersection;
		LinkedList<Link> links; 
		public double cost = 2e300;
		public Link previous = null;
		
		public Node(Intersection anIntersection){
			intersection = anIntersection;
			links = new LinkedList<Link>();
		}
		
		public void addLink(Link link) {
			links.add(link);
		}
		
		public String toString() {
			String res = "";
			res += intersection + " | " + this.links.size() + " | " + this.cost;
			return res;
		}
		
	}
	
	public class Link {
		Section section;
		Node startNode;
		Node endNode;
		
		public Link(Section aSection, Node aStartNode, Node anEndNode) {
			section = aSection;
			startNode = aStartNode;
			endNode = anEndNode;
		}
		
		public double getLength() {
			return this.section.getLength();
		}
	}
	
	public class LinkTrip extends Link {
		IntermediateResult intermediateResult;
		public LinkTrip(Section aSection, Node aStartNode, Node anEndNode, IntermediateResult ir) {
			super(aSection, aStartNode, anEndNode);
			this.intermediateResult = ir;
		}
	}
	
	public class IntermediateResult {
		Node start;
		Node end;
		LinkedList<Section> path;
		double length;
		
		public IntermediateResult() {
			this.path = new LinkedList<Section>();
		}
		
		public double computeLength() {
			double length = 0;
			for(Section s : path) length += s.getLength();
			return this.length = length;
		}
		
		public String toString() {
			String res = "Inter res: (";
			
			res += start + ") | (" + end + ") | " + this.length + " | " + this.path.size();
			
			return res;
		}
	}
	
}
