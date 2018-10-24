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
import utils.CircuitAlgorithm.IntermediateResult;
import model.Circuit;

public class CircuitAlgorithm {
	
	private CityMap cityMap;
	private DeliveryRequest deliveryRequest;
	private HashMap<String, Node> nodes;
	private LinkedList<Circuit> circuits;
	
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
	
	public CircuitAlgorithm(){
		
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
	
	private CircuitAlgorithm cleanCosts(Map<String,Node> nodes) {
		for(Node node : nodes.values())			node.cost = 2e300;
		return this;
	}
	private CircuitAlgorithm cleanCosts() {
		return cleanCosts(this.nodes);
	}
	private CircuitAlgorithm dijkstra(Node origin) {
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
	
	public void execute() {
		this.circuits = new LinkedList<Circuit>();
		LinkedList<IntermediateResult> shortestFromWarehouse = new LinkedList<>();
		LinkedList<IntermediateResult> shortestBetweenDeliveries = new LinkedList<>();
		
		LinkedList<Delivery> deliveries = deliveryRequest.getDeliveries();
		
		// Establish the shortest paths between deliveries and warehouse
		for(Delivery delivery : deliveries) {
			
			//System.out.println( "Target delivery" + delivery );
			
			Node origin = this.nodes.get( deliveryRequest.getWarehouseAddress() );
			Node target = this.nodes.get( delivery.getAddress() );
			
			IntermediateResult inter = this.cleanCosts().dijkstra( origin ).resolveDijkstra( origin, target );
			
			inter.computeLength();
			shortestFromWarehouse.add( inter );
			//System.out.println( inter );
			
			for(Delivery delivery2 : deliveries) {
				
				if(delivery == delivery2) continue;
				
				origin = this.nodes.get( delivery2.getAddress() );
				inter = this.cleanCosts().dijkstra( origin ).resolveDijkstra( origin, target );
				inter.computeLength();
				shortestBetweenDeliveries.add( inter );
				
				//System.out.println( inter );
			}

		}
		
		// Establish the best circuit
		// Prepare data
		HashMap<String, Node> delivNodes = new HashMap<String,Node>();
		for(Delivery delivery : deliveries) {
			Node node = new Node( this.cityMap.getIntersectionById(delivery.getAddress()) );
			delivNodes.put(node.intersection.getId(), node);
		}
		for(IntermediateResult ir : shortestBetweenDeliveries) {
			Node nodeStart = delivNodes.get(ir.start.intersection.getId());
			Node nodeEnd = delivNodes.get(ir.end.intersection.getId());
			Section sec = new Section();
			sec.setStartIntersection( ir.start.intersection );
			sec.setEndIntersection( ir.end.intersection );
			sec.setLength( ir.length );

			nodeStart.links.add(new LinkTrip(sec, nodeStart, nodeEnd, ir));
		}
		
		// Random solution
		Circuit circuit = new Circuit();
		circuit.setCourierId("0");
		circuit.setDepartureTime( this.deliveryRequest.getDepartureTime() );
		circuit.setWarehouseAddress( this.deliveryRequest.getWarehouseAddress() );
		
		double cost = 2e300;
		IntermediateResult path = null;
		
		for(IntermediateResult ir : shortestFromWarehouse) {
			if(ir.length >= cost) continue;
			
			cost = ir.length;
			path = ir;
		}
		
		System.out.println( path );
		Delivery delivery = null;
		Trip trip = new Trip();
		for(Delivery delSearch : deliveries) {
			System.out.println(delSearch.getAddress() + " " + path.end.intersection.getId());
			if(delSearch.getAddress() == path.end.intersection.getId()) {
				delivery = delSearch;
				delivNodes.remove(delSearch.getAddress());
				System.out.println(delivery);
				break;
			}
		}
		trip.setSections( path.path );
		
		circuit.addTripAndDelivery(trip, delivery);
		
		while(!delivNodes.isEmpty()) {
			Node start = path.start;
			cost = 2e300;
			path = null;
			
			for(IntermediateResult ir : shortestBetweenDeliveries) {
				if(ir.start != start) continue;
				if(ir.length >= cost) continue;
				
				cost = ir.length;
				path = ir;
			}
			
			if(path == null) break;
			
			for(Delivery delSearch : deliveries) {
				if(delSearch.getAddress() == path.end.intersection.getId()) {
					delivery = delSearch;
					delivNodes.remove(delSearch.getAddress());
					break;
				}
			}
			
			circuit.addTripAndDelivery(trip, delivery);
		}
		System.out.println(circuit);
		circuits.add( circuit );
	}
	
	public LinkedList<Circuit> result() {
		return this.circuits;
	}
	

	

}
