package utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Geolocation;
import model.Intersection;
import model.Section;
import model.Trip;
import tsp.TSP1;
import model.Circuit;

public class CircuitAlgorithm {
	
	private CityMap cityMap;
	private DeliveryRequest deliveryRequest;

	private int numberOfDeliveries;
	private Node warehouse;

	private HashMap<String, Node> nodes;
	private LinkedList<Circuit> circuits;
	
	private HashMap<String, HashMap<String, IntermediateResult>> shortestPaths;	
	
	private final int KMEANS_TIMEOUT = 10;
	
	
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
		this.numberOfDeliveries = deliveryRequest.getDeliveries().size();

		
		this.nodes = new HashMap<String, Node>();
		for(Intersection intersection : cityMap.getIntersections().values())
			nodes.put(intersection.getId(), new Node(intersection));
		
		for(Section section : cityMap.getSections()){
			Node startNode = nodes.get(section.getStartIntersection().getId());
			Node endNode = nodes.get(section.getEndIntersection().getId());
			Link newLink = new Link(section, startNode, endNode);
			nodes.get(section.getStartIntersection().getId()).addLink(newLink);
		}
		
		this.warehouse = new Node(deliveryRequest.getWarehouseIntersection());
		
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
	
	public void execute(int numberOfCouriers) {
		LinkedList<LinkedList<Delivery>> clusters = createClustersWithKMeans(numberOfCouriers);
		
		computeShortestPaths();
		
		
		
		//LinkedList<LinkedList<Delivery>> clusters = createClustersWithSweep(numberOfCouriers);
		//System.out.println(clusters);

		this.circuits = new LinkedList<Circuit>();
		for(LinkedList<Delivery> cluster : clusters) {
			runTSP(cluster, cluster.size());
		}
		System.out.println("clusters end");
		//call TSP
	}
	
	//////////////////////////// A FAIRE //////////////////////////
	//X//  Faire la boucle pour avoir plus de deux itérations et converger le kmeans
	// THINK OF TIMEOUT & DYNAMIC DISPLAY (pas forcément à nous)
	//X// Réorganiser les clusters quand un est trop peuplé
	// Regarder bug les circuits ne sont pas ronds
	// Calculer la qualité d'un cluster
	// faire kmeans plusieurs fois et stocker les clusters et la qualité du truc total
	// choisir la meilleure solution
	///////////////////////////////////////////////////////////////
	
	
	private LinkedList<LinkedList<Delivery>> createClustersWithKMeans(int numberOfCouriers){
		LinkedList<Delivery> deliveries = this.deliveryRequest.getDeliveries();
		int totalDeliveries = deliveries.size();
		
		//Initalisation of centers with random delivery geolocations
		Geolocation[] centers = new Geolocation[numberOfCouriers];
		HashMap<Geolocation, LinkedList<Delivery>> correspondingCenter = new HashMap<Geolocation, LinkedList<Delivery>>();

		for(int i = 0; i < numberOfCouriers; i++){
			int index = 0;
			boolean found = false;
			while (!found){
				index = getRandomIndex(totalDeliveries);
				
				if(correspondingCenter.get(deliveries.get(index).getGeolocation()) == null){
					LinkedList<Delivery> list = new LinkedList<Delivery>();
					correspondingCenter.put(deliveries.get(index).getGeolocation(), list);
					found = true;
				}
			}
			centers[i] = deliveries.get(index).getGeolocation();
		}
		
		
		for (int k = 0 ; k < KMEANS_TIMEOUT ; k ++) {
			
			//finding closest center for each delivery
			correspondingCenter = new HashMap<Geolocation, LinkedList<Delivery>>();

			double currentDistanceToCenter;
			for(Delivery delivery : deliveries){
				double minDistance = Double.MAX_VALUE;
				int closestCenter = 0;
				for(int i = 0; i < numberOfCouriers; i++){
					currentDistanceToCenter = centers[i].distance(delivery.getGeolocation());//distance ou plus court chemin entre delivery.getGeolocation() et centers[i]
					if(currentDistanceToCenter < minDistance){
						closestCenter = i;
						minDistance = currentDistanceToCenter;
					}
				}
				
				//add delivery to proper cluster
				LinkedList<Delivery> list = correspondingCenter.get(centers[closestCenter]);
				if(list == null) list = new LinkedList<Delivery>(); 
				list.add(delivery);
				correspondingCenter.put(centers[closestCenter],list);
			}
			System.out.println("hey");
			
			//Compute new center geolocation
			for(int i = 0; i<numberOfCouriers; i++){
				double meanLatitude = 0;
				double meanLongitude = 0;
				
				LinkedList <Delivery> deliveriesInCluster = correspondingCenter.get(centers[i]);
				
				for(Delivery delivery : deliveriesInCluster){
					meanLatitude += delivery.getGeolocation().getLatitude();
					meanLongitude += delivery.getGeolocation().getLongitude();
				}
				meanLatitude = meanLatitude / deliveriesInCluster.size();
				meanLongitude = meanLongitude / deliveriesInCluster.size();

				centers[i] = new Geolocation(meanLatitude, meanLongitude);
				System.out.println("hey2");

			}
			
		}
		
		
		
		
		
		
		
			
		
		
		//Return clusters
		LinkedList<LinkedList<Delivery>> clusters = new LinkedList<LinkedList<Delivery>>();
		for(Map.Entry<Geolocation, LinkedList<Delivery>> entry : correspondingCenter.entrySet()){
			clusters.add(entry.getValue());
		}
		
		
		double avgClusterSize = ((double)deliveries.size())/((double)numberOfCouriers);
		clusters = equalizeClustersSize(clusters,avgClusterSize);
		
		
		
		return clusters;
		
		
		
	}
	
	private LinkedList<LinkedList<Delivery>> equalizeClustersSize(LinkedList<LinkedList<Delivery>> clusters, double avgClusterSize){
		LinkedList<Delivery> deliveriesToChange = new LinkedList<Delivery>();
		
		for (LinkedList<Delivery> myCluster : clusters) {
			int clusterSize = myCluster.size();
			
			// If the cluster is bigger than expected
			if (clusterSize>avgClusterSize+1) {
			
				for (int i = 0 ; i < clusterSize-avgClusterSize ; i++) {
					deliveriesToChange.add(myCluster.getFirst());
					myCluster.removeFirst();
					
					
					System.out.println("// deliveriesToChange.size() = "+deliveriesToChange.size());
				}
				
			}
		}
		for (LinkedList<Delivery> myCluster : clusters) {
			int clusterSize = myCluster.size();
			
			// If the cluster is smaller than expected
			if (clusterSize<avgClusterSize-1) {
				
				for (int i = 0 ; i < clusterSize-avgClusterSize ; i++) {
					myCluster.add(myCluster.getFirst());
					deliveriesToChange.removeFirst();
					

					System.out.println("// deliveriesToChange.size() = "+deliveriesToChange.size());
				}
				
			}
			
		}
		
		if (!deliveriesToChange.isEmpty()) {
			for (LinkedList<Delivery> myCluster : clusters) {
				int clusterSize = myCluster.size();
				
				// If the cluster is smaller than expected
				if (clusterSize<avgClusterSize) {
					myCluster.add(deliveriesToChange.get(0));
					deliveriesToChange.removeFirst();		
					

					System.out.println("// deliveriesToChange.size() = "+deliveriesToChange.size());
				}
				
				if(deliveriesToChange.isEmpty()) {
					break;
				}
				
			}
			
			
		}
		
		
		
		
		return clusters;
	}

	private int getRandomIndex(int size){
		double randomDouble = Math.random() * size;
		int randomInt = (int) randomDouble;
		return randomInt;
	}
	
	private void computeShortestPaths() {
			
		LinkedList<Delivery> deliveries = deliveryRequest.getDeliveries();
		this.shortestPaths = new HashMap<String, HashMap<String, IntermediateResult>>();

		for(Delivery delivery : deliveries) {
			
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
		return;
	}
	
	private LinkedList<LinkedList<Delivery>> createClustersWithSweep(int numberOfCouriers){
		
		Delivery[] deliveries = deliveryRequest.getDeliveries().toArray(new Delivery[deliveryRequest.getDeliveries().size()]);
		bubbleSort(deliveries);
		
		//for testing purposes
		double[] angles = new double[deliveries.length];
		for(int i = 0; i < deliveries.length; i++) {
			angles[i] = computeAngle(deliveries[i]);
		}
		
		int[] circuitsSizes = computeCircuitsSizes(numberOfCouriers);
		
		
		LinkedList<LinkedList<Delivery>> clusters = new LinkedList<LinkedList<Delivery>>();
		int deliveriesIndex = 0;
		for(int i = 0; i< numberOfCouriers; i++) {
			LinkedList<Delivery> cluster = new LinkedList<Delivery>();
			
			int clusterSize = circuitsSizes[i];
			for(int j = 0; j<clusterSize; j++) {
				if(deliveriesIndex>=deliveries.length)
					break;//error
				cluster.add(deliveries[deliveriesIndex++]);
			}
			clusters.add(cluster);
		}
		
		return clusters;
	}
	
 	private void bubbleSort(Delivery[] deliveries) 
  { 
      Delivery temp; 
      boolean swapped; 
      for (int i = 0; i < deliveries.length - 1; i++)  
      { 
          swapped = false; 
          for (int j = 0; j < deliveries.length - i - 1; j++)  
          { 
              if ( computeAngle(deliveries[j])> computeAngle(deliveries[j + 1]))  
              { 
                  temp = deliveries[j]; 
                  deliveries[j] = deliveries[j + 1]; 
                  deliveries[j + 1] = temp; 
                  swapped = true; 
              } 
          }  
          if (swapped == false) 
              break; 
      } 
  } 
	
	private double computeAngle(Delivery delivery) {
		double numerator = this.warehouse.intersection.getGeolocation().getLatitude()-delivery.getGeolocation().getLatitude();
		double denominator = this.warehouse.intersection.getGeolocation().getLongitude()-delivery.getGeolocation().getLongitude();
		return Math.atan(numerator / denominator);
	}
	
	private int[] computeCircuitsSizes(int numberOfCouriers) {
		int[] circuitSize = new int[numberOfCouriers];
		for(int i = 0; i< numberOfCouriers; i++)
			circuitSize[i] = numberOfDeliveries / numberOfCouriers ;

		for(int i = 0; i< Math.floorMod(numberOfDeliveries, numberOfCouriers); i++)
			++circuitSize[i];
		return circuitSize;
	}
	
	private void runTSP(LinkedList<Delivery> cluster, int clusterSize) {
		int graphCardinality = clusterSize+1; //because we must include the warehouse
		int[][] tripLength = new int[graphCardinality][graphCardinality];
		int[] deliveryDuration = new int[graphCardinality];
		
		String warehouseAddress = this.deliveryRequest.getWarehouseAddress();
		
		for(int i = 1; i < graphCardinality; i++) { //i starts at 1 because 0 is the warehouse
			//Establish delivery duration
			Delivery delivery = cluster.get(i-1);
			String deliveryAddress = delivery.getAddress();
			
			HashMap<String, IntermediateResult> shortestPathsFromDelivery= this.shortestPaths.get(deliveryAddress);
			
			deliveryDuration[i] = delivery.getDuration();
			
			// Establish the shortest paths between warehouse and delivery
			tripLength[0][i] = (int) this.shortestPaths.get(warehouseAddress).get(deliveryAddress).length;
			
			// Establish the shortest paths between delivery and warehouse
			tripLength[i][0] = (int) shortestPathsFromDelivery.get(warehouseAddress).length;
			
			// Establish the shortest paths between deliveries
			for(int j = 1; j<graphCardinality; j++) {
				if(i == j) continue;
				tripLength[i][j] = (int) shortestPathsFromDelivery.get(cluster.get(j-1).getAddress()).length;
			}
		}
		
		
		TSP1 tsp = new TSP1();
		tsp.chercheSolution(60000, clusterSize + 1, tripLength , deliveryDuration);
		System.out.println("Tsp ended");
		
		
	//Circuit object creation

			Circuit circuit = new Circuit();
	    circuit.setDepartureTime( this.deliveryRequest.getDepartureTime() );
	    
	    //Add trip from warehouse to first delivery
			//trips are the business object equivalent to path
	    Delivery currentDelivery = cluster.get(tsp.getMeilleureSolution(1)-1); //because warehouse is not present in cluster
	    Trip nextTrip = new Trip();
			nextTrip.setSections(this.shortestPaths.get(this.deliveryRequest.getWarehouseAddress()).get(currentDelivery.getAddress()).path);
			circuit.addTrip(nextTrip);
			
			
	    Delivery nextDelivery = null;

			for(int i = 1; i < clusterSize; i++) {
				
				nextDelivery = cluster.get(tsp.getMeilleureSolution(i+1)-1);
				nextTrip = new Trip();
				nextTrip.setSections(this.shortestPaths.get(currentDelivery.getAddress()).get(nextDelivery.getAddress()).path);
				circuit.addTrip(nextTrip);
				circuit.addDelivery(currentDelivery);
				currentDelivery = nextDelivery;
				
			}
			//Add trip from last delivery to warehouse
			//trips are the business object equivalent to path
	    nextTrip = new Trip();
			nextTrip.setSections(this.shortestPaths.get(currentDelivery.getAddress()).get(this.deliveryRequest.getWarehouseAddress()).path);
			circuit.addTrip(nextTrip);
			circuit.addDelivery(currentDelivery);
			
			System.out.println("synch");
			this.circuits.add(circuit);
	}
		
	public LinkedList<Circuit> result() {
		return this.circuits;
	}
	
	private void addShortestPath(Node origin, Node target, IntermediateResult inter) {
		HashMap<String, IntermediateResult> innerMap= this.shortestPaths.get(origin.intersection.getId());
		if(innerMap == null)
			innerMap = new HashMap<String, IntermediateResult>();
		innerMap.put(target.intersection.getId(), inter);
		this.shortestPaths.put(origin.intersection.getId(), innerMap);
	}
	

}
