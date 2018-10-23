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
 * @author olivi
 */
public class Plan extends Observable{
    
    private List<Section> sections;
    private Map<String,Intersection> intersections;
    
    /**
     * Default constructor that creates empty lists of sections and intersections
     */
    public Plan() {
        this.sections = new LinkedList<Section>();
        this.intersections = new HashMap<String,Intersection>();
    }
    
    public void AddIntersection(Intersection inter) {
        this.intersections.put( inter.getId(), inter );
    }
    
    public void AddSection(Section sec) {
        this.sections.add( sec );
    }

    /**
     * Get an intersection by its Id
     * @param intersectionId
     * @return if found the intersection, null if not
     */
    public Intersection getIntersectionById(String intersectionId) {
        // Olivia we should discuss the container you chose since Map seems better

        return this.intersections.get( intersectionId );
    }
    
    public Map<String,Intersection> getIntersections() {
        return intersections;
    }

    public List<Section> getSections() {
        return sections;
    }


    public Geolocation getIntersectionGeolocation(String address){
        return this.intersections.get(address).getGeolocation();
    }

    @Override
    public String toString() {
        return "Plan{" + "sections=" + sections + ", intersections=" + intersections + '}';
    }
    
    public String getPlanInfos() {
        String str = "";
        
        str += "Plan : " + intersections.size() + " intersections, ";
        str += sections.size() + " sections" ;

        return str;
    }
    
    
}
