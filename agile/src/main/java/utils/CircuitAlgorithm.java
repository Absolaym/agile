package utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Section;
import model.Trip;
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
	
	public void execute() {
		this.circuits = new LinkedList<Circuit>();
		for(Delivery del : deliveryRequest.getDeliveries()) {
			Circuit res = new Circuit();
			
			Queue<Node> queue = new LinkedList<Node>();
			
			queue.add( this.nodes.get(deliveryRequest.getWarehouseAddress()) );
			queue.element().cost = 0;
			
			while(!queue.isEmpty()) {
				Node node = queue.remove();
				for(Link l : node.links) {
					double cost = l.getLength() + node.cost;
					if(cost >= l.endNode.cost) continue;
					
					l.endNode.cost = cost;
					queue.add(l.endNode);
					l.endNode.previous = l;
				}
			}
			
			System.out.println(del);
			
			for( Delivery del2 : deliveryRequest.getDeliveries()  ) {
				if(del == del2)	continue;
				Node node = nodes.get( del2.getAddress() );
		
				LinkedList<Link> links = new LinkedList<Link>();
				while(node.previous != null) {
					System.out.println(node.cost);
					links.addFirst( node.previous );
					node = node.previous.startNode;
				}
				System.out.println( del2 );
				
				Trip trip = new Trip();
				for(Link l : links) {
					trip.addSection(l.section);
				}
				
				res.getTrips().add( trip );
				
			}
			
			this.circuits.add( res );
		}
	}
	
	public LinkedList<Circuit> result() {
		return this.circuits;
	}
	

	

}
