/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * This class represents a map containing the road sections and intersections
 */
public class CityMap extends Observable {

    private List<Section> sections;
    private Map<String, Intersection> intersections;

    /**
     * Default constructor that creates empty lists of sections and
     * intersections
     */
    public CityMap() {
        this.sections = new LinkedList<Section>();
        this.intersections = new HashMap<String, Intersection>();
    }

    public Vector2D getCoveredAreaDimensions() {
        Vector2D dims = new Vector2D();
        double maxLat = -2e300;
        double minLat = 2e300;
        double maxLon = -2e300;
        double minLon = 2e300;
        for (Intersection inter : intersections.values()) {
            Geolocation geo = inter.getGeolocation();
            maxLat = Math.max(geo.getLatitude(), maxLat);
            minLat = Math.min(geo.getLatitude(), minLat);
            maxLon = Math.max(geo.getLongitude(), maxLon);
            minLon = Math.min(geo.getLongitude(), minLon);
        }
        dims.y = Math.max(Geolocation.distance(minLat, minLon, maxLat, minLon), Geolocation.distance(minLat, maxLon, maxLat, maxLon));
        dims.x = Math.max(Geolocation.distance(minLat, minLon, minLat, maxLon), Geolocation.distance(maxLat, minLon, maxLat, maxLon));
        return dims;
    }
    
    /**
     * Adds an intersection to the intersections attribute of the map
     * @param intersection the intersection
     */
    public void AddIntersection(Intersection intersection) {
        this.intersections.put( intersection.getId(), intersection );
    }
    
    /**
     * Adds an intersection to the sections attribute of the map
     * @param section the section to add
     */
    public void AddSection(Section section) {
        this.sections.add( section );
    }

    /**
     * Get an intersection by its Id
     *
     * @param intersectionId the id of the address of the intersection
     * @return if found the intersection, null if not
     */
    public Intersection getIntersectionById(String intersectionId) {
        // Olivia we should discuss the container you chose since Map seems better

        return this.intersections.get(intersectionId);
    }

    public Map<String, Intersection> getIntersections() {
        return intersections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Geolocation getIntersectionGeolocation(String address) {
        return this.intersections.get(address).getGeolocation();
    }

    @Override
    public String toString() {
        return "Plan{" + "sections=" + sections + ", intersections=" + intersections + '}';
    }

    public String getPlanInfos() {
        String str = "";

        str += "Plan : " + intersections.size() + " intersections, ";
        str += sections.size() + " sections";

        return str;
    }

    public boolean isEmpty() {
        boolean isEmpty = true;
        if (this.sections != null && this.sections.size() > 0) {
            isEmpty = false;
        }
        if (this.intersections != null && this.sections.size() > 0) {
            isEmpty = false;
        }
        return isEmpty;
    }

}
