package model;

public class Section {
	
	private String streetName;
	private double length;
	private Intersection startIntersection;
	private Intersection endIntersection;
	
	public Section() {
		this.streetName 			= "";
		this.length 				= 0;
		this.startIntersection 	= null;
		this.endIntersection 	= null;
	}
	
	public Section(String aStreetName, double aLength, Intersection aStartIntersection, Intersection anEndIntersection) {
		setStreetName(aStreetName);
		setLength(aLength);
		setStartIntersection(aStartIntersection);
		setEndIntersection(anEndIntersection);
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
		this.length = length;
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
	

}
