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

public class CircuitComputer {

    private DeliveryRequest deliveryRequest;

    private LinkedList<Circuit> circuits;

    private HashMap<String, HashMap<String, Trip>> shortestPaths;

    private final int KMEANS_TIMEOUT = 10;
    private final int KMEANS_EXECUTION_NUMBER = 10;

    public CircuitComputer() {

    }

    public void init(DeliveryRequest aDeliveryRequest, HashMap<String, HashMap<String, Trip>> someShortestPaths) {
        this.deliveryRequest = aDeliveryRequest;
        this.shortestPaths = someShortestPaths;
    }

    public void execute(int numberOfCouriers) {
        LinkedList<LinkedList<Delivery>> clusters = findBestClusters(numberOfCouriers);

        //LinkedList<LinkedList<Delivery>> clusters = createClustersWithKMeans(numberOfCouriers);
        this.circuits = new LinkedList<Circuit>();
        for (LinkedList<Delivery> cluster : clusters) {
            runTSP(cluster, cluster.size());
        }
        
        int circuitNumber = 1;
        for(Circuit circuit : circuits){
            int arrivalTimeSeconds = circuit.getDepartureTime().time;
            circuit.setCourierId(circuitNumber++);
            int i = 0;
            for(Delivery delivery : circuit.getDeliveries()){
                delivery.setCircuit(circuit);
                int tripDurationSeconds = (int)(circuit.getTrips().get(i).getLength() / (Circuit.SPEED / 3.6));
                arrivalTimeSeconds += tripDurationSeconds;
                delivery.setArrivalTimeSeconds(arrivalTimeSeconds);
                arrivalTimeSeconds += delivery.getDuration() * 60;
                i++;
            }
        }
        System.out.println("clusters end");
    }

    //////////////////////////// A FAIRE //////////////////////////
    //X//  Faire la boucle pour avoir plus de deux itérations et converger le kmeans
    // TIMEOUT & DYNAMIC DISPLAY (pas forcément à nous)
    //X// Réorganiser les clusters quand un est trop peuplé
    // Regarder bug les circuits ne sont pas ronds
    // Calculer la qualité d'un cluster
    // faire kmeans plusieurs fois et stocker les clusters et la qualité du truc total
    // choisir la meilleure solution
    //ameliorer tsp
    ///////////////////////////////////////////////////////////////
    public LinkedList<LinkedList<Delivery>> findBestClusters(int numberOfCouriers) {

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

        for (int k = 0; k < KMEANS_TIMEOUT; k++) {

            //finding closest center for each delivery
            correspondingCenter = new HashMap<Geolocation, LinkedList<Delivery>>();

            double currentDistanceToCenter;
            for (Delivery delivery : deliveries) {
                double minDistance = Double.MAX_VALUE;
                int closestCenter = 0;
                for (int i = 0; i < numberOfCouriers; i++) {
                    currentDistanceToCenter = centers[i].distance(delivery.getGeolocation());//distance ou plus court chemin entre delivery.getGeolocation() et centers[i]
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

            //Compute new center geolocation
            for (int i = 0; i < numberOfCouriers; i++) {
                double meanLatitude = 0;
                double meanLongitude = 0;

                LinkedList<Delivery> deliveriesInCluster = correspondingCenter.get(centers[i]);

                for (Delivery delivery : deliveriesInCluster) {
                    meanLatitude += delivery.getGeolocation().getLatitude();
                    meanLongitude += delivery.getGeolocation().getLongitude();
                }
                meanLatitude = meanLatitude / deliveriesInCluster.size();
                meanLongitude = meanLongitude / deliveriesInCluster.size();

                centers[i] = new Geolocation(meanLatitude, meanLongitude);

            }

        }

        //Return clusters
        LinkedList<LinkedList<Delivery>> clusters = new LinkedList<LinkedList<Delivery>>();
        for (Map.Entry<Geolocation, LinkedList<Delivery>> entry : correspondingCenter.entrySet()) {
            clusters.add(entry.getValue());
        }

        double avgClusterSize = ((double) deliveries.size()) / ((double) numberOfCouriers);
        clusters = equalizeClustersSize(clusters, avgClusterSize);

        //Compute new centers geolocations (after equalization)
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

        Pair<LinkedList<LinkedList<Delivery>>, Geolocation[]> clustersAndCenters = new Pair<LinkedList<LinkedList<Delivery>>, Geolocation[]>(clusters, centers);
        return (clustersAndCenters);
    }

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

    private int getRandomIndex(int size) {
        double randomDouble = Math.random() * size;
        int randomInt = (int) randomDouble;
        return randomInt;
    }

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

            deliveryDuration[i] = delivery.getDuration();

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
        tsp.chercheSolution(60000, clusterSize + 1, tripLength, deliveryDuration);
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

    public LinkedList<Circuit> result() {
        return this.circuits;
    }

}
