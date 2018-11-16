package model;

/**
 * Class represents a road section
 */
public class Section {
	
    private String streetName;
    private double length;
    private Intersection startIntersection;
    private Intersection endIntersection;
    private Circuit circuit;

    public Section() {
        this.streetName         = "";
        this.length             = 0;
        this.startIntersection 	= null;
        this.endIntersection 	= null;
    }

    public Section(String aStreetName, double aLength, Intersection aStartIntersection, Intersection anEndIntersection) {
        setStreetName(aStreetName);
        setLength(aLength);
        setStartIntersection(aStartIntersection);
        setEndIntersection(anEndIntersection);
    }
    
    public Section(Section s){
        streetName = s.getStreetName();
        length = s.getLength();
        startIntersection = s.getStartIntersection();
        endIntersection = s.getEndIntersection();
        circuit = s.getCircuit();
    }

    public String getStreetName() {
        return streetName;
    }
    
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
    
    public double getLength() {
        return length;
    }
    
    public void setLength(double length) {
        this.length = Math.max(0, length);
    }
    
    public Intersection getStartIntersection() {
        return startIntersection;
    }
    
    public void setStartIntersection(Intersection startIntersection) {
        this.startIntersection = startIntersection;
    }
    
    public Intersection getEndIntersection() {
        return endIntersection;
    }
    
    public void setEndIntersection(Intersection endIntersection) {
        this.endIntersection = endIntersection;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }
    

    @Override
    public String toString() {
        return "Section{" + "streetName=" + streetName + ", length=" + length + ", startIntersection=" + startIntersection + ", endIntersection=" + endIntersection + '}';
    }	

}
