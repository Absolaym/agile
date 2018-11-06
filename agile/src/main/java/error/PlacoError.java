package error;

public enum PlacoError {
	
	UNCONNECTED_INTERSECTION("UC1-UI", "One intersection is not connected to the reste of the map making it impossible to reach"),
	CORRUPTED_XML_CM("UC1-CX", "This file is not well formatted as an xml"),
	EMPTY_XML_CM("UC1-EX", "This file is empty"),
	NON_XML_CM("UC1_NX","This file is not an XML file"),
	
	CORRUPTED_XML_DR("UC2-CX", "This file is not well formatted as an xml"),
	EMPTY_XML_DR("UC2-EX", "This file is empty"),
	NON_XML_DR("UC2_NX","This file is not an XML file"),
	NON_STD_DR("UC2_NS", "The object described in the file does not respect the standard description of a DR, thus the object cannot be created"),
	DEL_ADDRESS_NOT_IN_MAP("UC2-DANIM", "The address of one of deliveries is not in the map"),
	NO_MAP_PREVIOUSLY_LOADED("UC2-NMPL", "No map has been previously loaded"),
	
	NO_COURRIER_ASSIGNED("UC3-NCA", "You didn't provide the number of courriers you want to assign circuits"),
	NO_DR_BEF_CIRCUIT("UC3-NDBC", "You have no delivery request so the circuit is already the best by its absence"),
	TIMEOUT("UC3-TO", "Your request is killin' it, the program is not designed to support such a huge file"),
	DEAD_END("UC3-DE", "One of the deliveries is accessible but the path is one sided and the courrier can't come back"),
	
	IMMATEAPOT("HTCPCP", "I'm a teapot and thus can't serve you any coffee")
	;
	
	
	public String code = "-";
	public String message = "";
	
	PlacoError(String code, String msg) {
		this.code = code;
		this.message = msg;
	}
	
	public String toString() {
		return "Error (" + this.code + ") : " + this.message;
	}
}
