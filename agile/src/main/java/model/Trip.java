package model;

import java.util.LinkedList;
import java.util.List;

public class Trip {
    private List<Section> sections;

    public Trip() {
        this.sections = new LinkedList<>();
    }
    
    public Trip(Trip t){
        sections = new LinkedList<>();
        for(Section s : t.getSections()){
            sections.add(new Section(s));
        }
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
    
    public int getLength() {
        int sum = 0;
        for(Section sec : sections) {
            sum += sec.getLength();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Trip{" + "sections=" + sections + '}';
    }
	
}
