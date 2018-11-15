package utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import model.Delivery;
import model.DeliveryRequest;
import model.Geolocation;
import model.Trip;
import tspImproved.TSP1;
import model.Circuit;

/**
 * 
 * @author MarieFrance
 * 
 * Class divides deliveries into a given amount of clusters
 * and computes circuits 
 *
 */
public class CircuitComputer {

    private DeliveryRequest deliveryRequest;

    private LinkedList<Circuit> circuits;

    private HashMap<String, HashMap<String, Trip>> shortestPaths;

    private final int KMEANS_TIMEOUT = 10;
    private final int KMEANS_EXECUTION_NUMBER = 10;
    private final int TSP_TIMEOUT = 30000;

    public CircuitComputer() {

    }

    /**
  	 * Initializes class to 
  	 * @param aDeliveryRequest the delivery request containing the deliveries to use
  	 * @param someShortestPaths the shortest paths that link the deliveries with eachother and with the warehouse
  	 */
    public void init(DeliveryRequest aDeliveryRequest, HashMap<String, HashMap<String, Trip>> someShortestPaths) {
        this.deliveryRequest = aDeliveryRequest;
        this.shortestPaths = someShortestPaths;
    }

    /**
  	 * Creates the clusters with a K-MEANS approach. This class must be initialized before calling execute
  	 * and creates the circuits with calling a TSP algorithm
  	 * @param numberOfCouriers the number of circuits to create. Must be less than the number of deliveries in the request
  	 */
    public void execute(int numberOfCouriers) {
        LinkedList<LinkedList<Delivery>> clusters = findBestClusters(numberOfCouriers);
        
        this.circuits = new LinkedList<Circuit>();
        for (LinkedList<Delivery> cluster : clusters) {
            runTSP(cluster, cluster.size());
        }
        
        int circuitNumber = 1;
        for(Circuit circuit : circuits){
            int arrivalTimeSeconds = circuit.getDepartureTime().time;
            System.out.println(circuit.getDepartureTime());
            circuit.setCourierId(circuitNumber++);
            int i = 0;
            for(Delivery delivery : circuit.getDeliveries()){
                delivery.setCircuit(circuit);
                int tripDurationSeconds = (int)(circuit.getTrips().get(i).getLength() / (Circuit.SPEED / 3.6));
                arrivalTimeSeconds += tripDurationSeconds;
                delivery.setArrivalTime( arrivalTimeSeconds );
                arrivalTimeSeconds += delivery.getDuration().getSeconds();
                i++;
            }
        }
    }

    /**
  	 * Creates clusters several times with a K-MEANS approach having roughly the same size and chooses the one with the best quality
  	 * @param numberOfCouriers the number of circuits to create. Must be less than the number of deliveries in the request
  	 */
    private LinkedList<LinkedList<Delivery>> findBestClusters(int numberOfCouriers) {

        LinkedList<LinkedList<Delivery>> bestClusters = new LinkedList<LinkedList<Delivery>>();
        double bestQuality = Double.MAX_VALUE;

        for (int i = 0; i < KMEANS_EXECUTION_NUMBER; i++) {

            Pair<LinkedList<LinkedList<Delivery>>, Geolocation[]> clustersAndCenters = createClustersWithKMeans(numberOfCouriers);

            double quality = computeClustersQuality(clustersAndCenters);

            if (quality < bestQuality) {
                bestQuality = quality;
                bestClusters = clustersAndCenters.getFirst();
            }
        }

        return (bestClusters);
    }

    /**
  	 * Computes the quality of a set of clusters adding the distance between each delivery and its cluster center. 
  	 * This is useful to assess which clustering solution is better, after the clusters have been moved to even out the cluster sizes
  	 * @param clustersAndCenters each cluster of the solution paired with its center
  	 */
    public double computeClustersQuality(Pair<LinkedList<LinkedList<Delivery>>, Geolocation[]> clustersAndCenters) {
        // The quality of a group of clusters equals to the sum of all of the distances between deliveries
        double quality = 0;

        LinkedList<LinkedList<Delivery>> clusters = clustersAndCenters.getFirst();
        Geolocation[] centers = clustersAndCenters.getSecond();

        for (int i = 0; i < clusters.size(); i++) {
            LinkedList<Delivery> cluster = clusters.get(i);
            for (int j = 0; j < cluster.size(); j++) {
                quality += cluster.get(j).getGeolocation().distance(centers[i]);
            }
        }

        return (quality);
    }

    /**
  	 * Creates the clusters with a K-MEANS approach. The clusters are not necessarily the same size
  	 * @param numberOfCouriers the number of circuits to create. Must be less than the number of deliveries in the request
  	 */
    private Pair<LinkedList<LinkedList<Delivery>>, Geolocation[]> createClustersWithKMeans(int numberOfCouriers) {
        LinkedList<Delivery> deliveries = this.deliveryRequest.getDeliveries();
        int totalDeliveries = deliveries.size();

        //Initalisation of centers with random delivery geolocations
        Geolocation[] centers = new Geolocation[numberOfCouriers];
        HashMap<Geolocation, LinkedList<Delivery>> correspondingCenter = new HashMap<Geolocation, LinkedList<Delivery>>();

        for (int i = 0; i < numberOfCouriers; i++) {
            int index = 0;
            boolean found = false;
            while (!found) {
                index = getRandomIndex(totalDeliveries);

                if (correspondingCenter.get(deliveries.get(index).getGeolocation()) == null) {
                    LinkedList<Delivery> list = new LinkedList<Delivery>();
                    correspondingCenter.put(deliveries.get(index).getGeolocation(), list);
                    found = true;
                }
            }
            centers[i] = deliveries.get(index).getGeolocation();
        }

        LinkedList<LinkedList<Delivery>> clusters = new LinkedList<LinkedList<Delivery>>();
        
        for (int k = 0; k < KMEANS_TIMEOUT; k++) {

            //finding closest center for each delivery
            correspondingCenter = new HashMap<Geolocation, LinkedList<Delivery>>();

            double currentDistanceToCenter;
            for (Delivery delivery : deliveries) {
                double minDistance = Double.MAX_VALUE;
                int closestCenter = 0;
                for (int i = 0; i < numberOfCouriers; i++) {
                    currentDistanceToCenter = centers[i].distance(delivery.getGeolocation());
                    if (currentDistanceToCenter < minDistance) {
                        closestCenter = i;
                        minDistance = currentDistanceToCenter;
                    }
                }

                //add delivery to proper cluster
                LinkedList<Delivery> list = correspondingCenter.get(centers[closestCenter]);
                if (list == null) {
                    list = new LinkedList<Delivery>();
                }
                list.add(delivery);
                correspondingCenter.put(centers[closestCenter], list);
            }

            clusters = new LinkedList<LinkedList<Delivery>>();
            for (Map.Entry<Geolocation, LinkedList<Delivery>> entry : correspondingCenter.entrySet()) {
                clusters.add(entry.getValue());
            }
            
            centers = computeNewCentersGeolocations(numberOfCouriers, clusters);

        }


        double avgClusterSize = ((double) deliveries.size()) / ((double) numberOfCouriers);
        
        // Equalize sizes of clusters 
        clusters = equalizeClustersSize(clusters, avgClusterSize);
        centers = computeNewCentersGeolocations(numberOfCouriers, clusters);


        Pair<LinkedList<LinkedList<Delivery>>, Geolocation[]> clustersAndCenters = new Pair<LinkedList<LinkedList<Delivery>>, Geolocation[]>(clusters, centers);
        return (clustersAndCenters);
    }
    
    /**
  	 * Computes the center of a cluster. Useful for the K-MEANS clustering implementation
  	 * @param numberOfCouriers the number of couriers which is also the number of clusters
  	 * @param clusters the cluster to be computed
  	 * @return the new cluster centers
  	 */
    private Geolocation[] computeNewCentersGeolocations(int numberOfCouriers, LinkedList<LinkedList<Delivery>> clusters) {
    	
    	Geolocation[] centers = new Geolocation[numberOfCouriers]; 
    	
       
        for (int i = 0; i < numberOfCouriers; i++) {
            double meanLatitude = 0;
            double meanLongitude = 0;

            for (Delivery delivery : clusters.get(i)) {
                meanLatitude += delivery.getGeolocation().getLatitude();
                meanLongitude += delivery.getGeolocation().getLongitude();
            }
            meanLatitude = meanLatitude / clusters.get(i).size();
            meanLongitude = meanLongitude / clusters.get(i).size();

            centers[i] = new Geolocation(meanLatitude, meanLongitude);
        }
        
        return (centers);
    }

    /*
     * Takes random deliveries from clusters that are too big and puts them
     *  in clusters that are too small and vice-versa until they are all roughly the same size.
     *  This means that the biggest cluster can has at most 2 deliveries more than the smallest one
     * @param clusters the clusters to be equalized
     * @param avgClusterSize the number of deliveries in the request devided by the number of clusters
     * @return a list of clusters that are roughly the same size
     */
    private LinkedList<LinkedList<Delivery>> equalizeClustersSize(LinkedList<LinkedList<Delivery>> clusters, double avgClusterSize) {

        for (LinkedList<Delivery> cluster : clusters) {
            //if way too big, put deliveries elsewhere 
            while (cluster.size() > avgClusterSize + 1) {
                for (LinkedList<Delivery> targetCluster : clusters) {
                    if (targetCluster.size() < avgClusterSize) {
                        targetCluster.add(cluster.removeFirst());
                        break;
                    }
                }
            }

            //if way too small, pick deliveries from elsewhere 
            while (cluster.size() < avgClusterSize - 1) {
                for (LinkedList<Delivery> targetCluster : clusters) {
                    if (targetCluster.size() > avgClusterSize) {
                        cluster.add(targetCluster.removeFirst());
                        break;
                    }
                }
            }
        }

        return clusters;
    }

    /*
     * Returns a random index in [0;size[
     * @param size the size of the collection
     * @return a random index inferior to the size of the collection
     */
    private int getRandomIndex(int size) {
        double randomDouble = Math.random() * size;
        int randomInt = (int) randomDouble;
        return randomInt;
    }

    /*
     * Creates a circuit for a given cluster. An more specifically, creates the necessary 
     * parameters to call the TSP algorithm provided for a given cluster. Then calls it. 
     * Then transforms the results into the business objects used in the rest of the program.
     * Then adds it to the attribute circuits of this class
     * @param cluster the cluster for which a circuit must be computed
     * @param clusterSize the size of the cluster
     */
    private void runTSP(LinkedList<Delivery> cluster, int clusterSize) {
        int graphCardinality = clusterSize + 1; //because we must include the warehouse
        int[][] tripLength = new int[graphCardinality][graphCardinality];
        int[] deliveryDuration = new int[graphCardinality];

        String warehouseAddress = this.deliveryRequest.getWarehouseAddress();

        for (int i = 1; i < graphCardinality; i++) { //i starts at 1 because 0 is the warehouse
            //Establish delivery duration
            Delivery delivery = cluster.get(i - 1);
            String deliveryAddress = delivery.getAddress();

            HashMap<String, Trip> shortestPathsFromDelivery = this.shortestPaths.get(deliveryAddress);

            deliveryDuration[i] = delivery.getDuration().getSeconds();

            // Establish the shortest paths between warehouse and delivery
            tripLength[0][i] = (int) this.shortestPaths.get(warehouseAddress).get(deliveryAddress).getLength();

            // Establish the shortest paths between delivery and warehouse
            tripLength[i][0] = (int) shortestPathsFromDelivery.get(warehouseAddress).getLength();

            // Establish the shortest paths between deliveries
            for (int j = 1; j < graphCardinality; j++) {
                if (i == j) {
                    continue;
                }
                tripLength[i][j] = (int) shortestPathsFromDelivery.get(cluster.get(j - 1).getAddress()).getLength();
            }
        }

        long startTime = System.currentTimeMillis();
        TSP1 tsp = new TSP1();
        tsp.chercheSolution(TSP_TIMEOUT, clusterSize + 1, tripLength, deliveryDuration);
        long tspDuration = System.currentTimeMillis() - startTime;
        System.out.println("Tsp lasted " + tspDuration + " ms");

        //Circuit object creation
        Circuit circuit = new Circuit();
        circuit.setDepartureTime(this.deliveryRequest.getDepartureTime());

        //Add trip from warehouse to first delivery
        //trips are the business object equivalent to path
        Delivery currentDelivery = cluster.get(tsp.getMeilleureSolution(1) - 1); //because warehouse is 0 and is not present in cluster
        Trip nextTrip = new Trip();
        nextTrip.setSections(this.shortestPaths.get(this.deliveryRequest.getWarehouseAddress()).get(currentDelivery.getAddress()).getSections());
        circuit.addTrip(nextTrip);

        Delivery nextDelivery = null;

        for (int i = 1; i < clusterSize; i++) {

            nextDelivery = cluster.get(tsp.getMeilleureSolution(i + 1) - 1);
            nextTrip = new Trip();
            nextTrip.setSections(this.shortestPaths.get(currentDelivery.getAddress()).get(nextDelivery.getAddress()).getSections());
            circuit.addTrip(nextTrip);
            circuit.addDelivery(currentDelivery);
            currentDelivery = nextDelivery;

        }
        //Add trip from last delivery to warehouse
        //trips are the business object equivalent to path
        nextTrip = new Trip();
        nextTrip.setSections(this.shortestPaths.get(currentDelivery.getAddress()).get(this.deliveryRequest.getWarehouseAddress()).getSections());
        circuit.addTrip(nextTrip);
        circuit.addDelivery(currentDelivery);

        this.circuits.add(circuit);
    }

    /* 
     * Returns the list of computed circuits
     * The methods init and execute must be called first
     * @return the list of computed circuits
     */
    public LinkedList<Circuit> result() {
        return this.circuits;
    }

}
