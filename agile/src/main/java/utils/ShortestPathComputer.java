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

/**
 * <<<<<<< HEAD Class computes the shortest paths that link deliveries to each
 * other and to the warehouse using the Dijkstra algorithm It's designed to keep
 * track of the shortest path throughout the calls @author johnny
 */
public class ShortestPathComputer {

    private CityMap cityMap;
    private DeliveryRequest deliveryRequest;

    private HashMap<String, Node> nodes;
    private HashMap<String, HashMap<String, Trip>> shortestPaths;

    public ShortestPathComputer() {
    }

    /**
     * Initializes class to be able to perform the computations
     *
     * @param aCityMap the city map in which the deliveries are based
     * @param aDeliveryRequest a delivery request containing a not null list of
     * deliveries
     */
    public void init(CityMap aCityMap, DeliveryRequest aDeliveryRequest) {
        this.cityMap = aCityMap;
        this.deliveryRequest = aDeliveryRequest;

        this.nodes = new HashMap<String, Node>();

        for (Intersection intersection : cityMap.getIntersections().values()) {
            nodes.put(intersection.getId(), new Node(intersection));
        }

        for (Section section : cityMap.getSections()) {
            Node startNode = nodes.get(section.getStartIntersection().getId());
            Node endNode = nodes.get(section.getEndIntersection().getId());
            Link newLink = new Link(section, startNode, endNode);
            nodes.get(section.getStartIntersection().getId()).addLink(newLink);
        }

    }

    /*
	 * Computes all shortest paths linking each delivery from the delivery request 
	 * with one another and with the warehouse
	 * The init method must be called first
     */
    public void computeAllShortestPaths() { //formerly: execute

        LinkedList<Delivery> deliveries = deliveryRequest.getDeliveries();
        this.shortestPaths = new HashMap<String, HashMap<String, Trip>>();

        for (Delivery delivery : deliveries) {
            setShortestPathsFromDelivery(delivery, deliveries);
        }
        return;
    }

    /*
	 * Computes all shortest paths linking the new delivery to the warehouse and
	 * to the other deliveries from the delivery request
	 * The init method must be called first
	 * @param newDelivery the new delivery to take into account
	 * @param previousShortestPaths the shortest paths already computed for the delivery request
     */
    public void setShortestPathsForNewDelivery(Delivery newDelivery, HashMap<String, HashMap<String, Trip>> previousShortestPaths) {
        this.shortestPaths = previousShortestPaths;

        //set the shortest paths from the new delivery
        setShortestPathsFromDelivery(newDelivery, this.deliveryRequest.getDeliveries());

        //set the shortest paths from the other deliveries to the new delivery
        setShortestPathsToDelivery(newDelivery, this.deliveryRequest.getDeliveries());

    }

    /**
     * Computes shortest paths that go from to this delivery to all of the rest
     * and shortest paths involving this delivery and the warehouse
     *
     * @param delivery the target delivery
     * @param deliveries the list of origin deliveries. It can contain the
     * origin delivery
     */
    private void setShortestPathsFromDelivery(Delivery delivery, LinkedList<Delivery> deliveries) {
        Node origin = this.nodes.get(delivery.getAddress());
        Node target = this.nodes.get(deliveryRequest.getWarehouseAddress());

        IntermediateResult inter = null;
        // Establish the shortest paths between delivery and warehouse
        inter = this.cleanCosts().dijkstra(origin).resolveDijkstra(origin, target);
        inter.computeLength();
        addShortestPath(origin, target, inter);

        // Establish the shortest paths between warehouse and delivery
        inter = this.cleanCosts().dijkstra(target).resolveDijkstra(target, origin);
        inter.computeLength();
        addShortestPath(target, origin, inter);

        // Establish the shortest paths between this delivery and the other deliveries
        for (Delivery delivery2 : deliveries) {

            if (delivery == delivery2) {
                continue;
            }

            target = this.nodes.get(delivery2.getAddress());
            inter = this.cleanCosts().dijkstra(origin).resolveDijkstra(origin, target);
            inter.computeLength();
            addShortestPath(origin, target, inter);
        }
    }

    /**
     * Sets shortest paths that arrive to this delivery from all of the rest
     *
     * @param delivery the target delivery
     * @param deliveries the list of origin deliveries. It can contain the
     * target delivery
     */
    private void setShortestPathsToDelivery(Delivery delivery, LinkedList<Delivery> deliveries) {
        Node origin;
        Node target = this.nodes.get(delivery.getAddress());

        IntermediateResult inter;

        // Establish the shortest paths between this delivery and the other deliveries
        for (Delivery delivery2 : deliveries) {

            if (delivery == delivery2) {
                continue;
            }

            origin = this.nodes.get(delivery2.getAddress());
            inter = this.cleanCosts().dijkstra(origin).resolveDijkstra(origin, target);
            inter.computeLength();
            addShortestPath(origin, target, inter);
        }
    }

    /**
     * Returns an object containing the shortest paths that link the deliveries
     * of the request to each other and to the warehouse
     *
     * @return a hashmap containing, for each origin delivery, a hashmap
     * containing for each target delivery the shortest path that links the
     * origin delivery to the target delivery
     */
    public HashMap<String, HashMap<String, Trip>> result() {
        return this.shortestPaths;
    }

    /**
     * Adds shortest path that links an origin delivery to a target delivery
     *
     * @param origin the node corresponding to the origin delivery
     * @param target the node corresponding to the target delivery
     * @param inter the object containing the shortest path computed by the
     * Dijkstra algorithm.
     */
    private void addShortestPath(Node origin, Node target, IntermediateResult inter) {
        HashMap<String, Trip> innerMap = this.shortestPaths.get(origin.intersection.getId());
        if (innerMap == null) {
            innerMap = new HashMap<String, Trip>();
        }
        Trip trip = new Trip();
        trip.setSections(inter.path);
        innerMap.put(target.intersection.getId(), trip);
        this.shortestPaths.put(origin.intersection.getId(), innerMap);
    }

    // ===========================================
    // =========== Dijkstra section ==============
    // ===========================================
    /*
	 * Resets the costs to reach each node and resets their parent nodes to be able 
	 * to compute the Dijkstra algorithm starting from another node.
	 * @param nodes the nodes of the graph to clean up
     */
    private ShortestPathComputer cleanCosts(Map<String, Node> nodes) {
        for (Node node : nodes.values()) {
            node.cost = 2e300;
            node.previous = null;
        }
        return this;
    }

    /*
	 * Resets the nodes of this instance: resets costs to reach each node
	 *  and resets their parent nodes to be able to compute the Dijkstra
	 *  algorithm starting from another node.
     */
    private ShortestPathComputer cleanCosts() {
        return cleanCosts(this.nodes);
    }

    private ShortestPathComputer dijkstra(Node origin) {
        Queue<Node> queue = new LinkedList<Node>();

        origin.cost = 0;
        queue.add(origin);

        while (!queue.isEmpty()) {
            Node node = queue.remove();
            for (Link l : node.links) {
                double cost = l.getLength() + node.cost;
                if (cost >= l.endNode.cost) {
                    continue; // keeps the first best path in case of a draw
                }
                l.endNode.cost = cost;
                l.endNode.previous = l;
                queue.add(l.endNode);
            }
        }
        return this;
    }

    /**
     * Recreates the shortest path after all the costs and parent nodes have
     * been set
     *
     * @param origin the origin node
     * @param target the target node
     * @return an object containing the shortest path
     */
    private IntermediateResult resolveDijkstra(Node origin, Node target) {
        IntermediateResult inter = new IntermediateResult();

        inter.start = origin;
        inter.end = target;

        LinkedList<Link> links = new LinkedList<Link>();

        Node node = target;
        while (node.previous != null && node != origin) {
            links.addFirst(node.previous);
            inter.path.addFirst(node.previous.section);
            node = node.previous.startNode;
        }

        return inter;
    }

    //---------------------- Inner classes --------------------------
    // These classes wrap the informations of the cityMap to make the 
    // computations easier
    /**
     * This class wraps intersections to make the Dijkstra implementation easier
     */
    private class Node {

        Intersection intersection;
        LinkedList<Link> links;
        public double cost = 2e300;
        public Link previous = null;

        public Node(Intersection anIntersection) {
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

    /**
     * This class wraps sections to make the Dijkstra implementation easier
     */
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

    /**
     * This class ressembles the trip class but with this inner class tooltip
     *
     */
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
            for (Section s : path) {
                length += s.getLength();
            }
            return this.length = length;
        }

        public String toString() {
            String res = "Inter res: (";

            res += start + ") | (" + end + ") | " + this.length + " | " + this.path.size();

            return res;
        }
    }

}
