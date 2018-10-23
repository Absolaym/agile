package utils;

import java.util.HashMap;
import java.util.LinkedList;

import model.CityMap;
import model.DeliveryRequest;
import model.Intersection;
import model.Section;

public class CircuitAlgorithm {
	
	private CityMap cityMap;
	private DeliveryRequest deliveryRequest;
	private HashMap<String, Node> nodes;
	
	public class Node {
		Intersection intersection;
		LinkedList<Link> links; 
		
		public Node(Intersection anIntersection){
			intersection = anIntersection;
			links = new LinkedList<Link>();
		}
	}
	
	public class Link {
		Section section;
		Node startNode;
		Node endNode;
		
	}
	
	public CircuitAlgorithm(){
		
	}
	
	public void init(CityMap aCityMap, DeliveryRequest aDeliveryRequest){
		this.cityMap = aCityMap;
		this.deliveryRequest = aDeliveryRequest;
		
		nodes = new HashMap<String, Node>();
		for(Intersection intersection : cityMap.getIntersections().values()){
				/*
			new Node().intersection = node;
			newNode.instersection = intersection;
			
			nodes.put(intersection.getId(), arg1)
			*/
		}
		
		
	}
	
	

	

}
