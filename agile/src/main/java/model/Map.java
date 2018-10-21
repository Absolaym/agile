/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import java.util.Observable;

/**
 *
 * @author olivi
 */
public class Map extends Observable{
    
	private List<Section> sections;
    private List<Intersection> intersections;
    
    public Map(List<Section> someSections, List<Intersection> someIntersections) {
    	setSections(someSections);
    	setIntersections(someIntersections);
    }

	public List<Intersection> getIntersections() {
		return intersections;
	}

	public void setIntersections(List<Intersection> intersections) {
		this.intersections = intersections;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}
    
}
