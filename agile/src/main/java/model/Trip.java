package model;

import java.util.LinkedList;
import java.util.List;

public class Trip {
    private List<Section> sections;

    public Trip() {
        this.sections = new LinkedList<Section>();
    }

    public List<Section> getSections() {
        return sections;
    }
    
    public void setSections(List<Section> s){
        sections = s;
    }

    public Trip addSection(Section section) {
        this.sections.add(section);
        return this;
    }

    @Override
    public String toString() {
        return "Trip{" + "sections=" + sections + '}';
    }
	
}
