package utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.*;
import model.Delivery;
import model.DeliveryRequest;
import model.Geolocation;
import model.Intersection;
import model.CityMap;
import model.Section;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import error.ErrorLogger;
import error.Error;

/**
 * @author Johnny
 */
public class XmlParser {
	private DocumentBuilderFactory docBuilderFactory;
	private DocumentBuilder docBuilder;

	public XmlParser() {
		this.docBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			this.docBuilder = this.docBuilderFactory.newDocumentBuilder();
		}
		catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private boolean extensionCheck(String path) {
		return this.extensionCheck(path, "xml");
	}
	private boolean extensionCheck(String path, String extension) {
		String[] split = path.split("[.]");
		return (split.length > 1 && split[split.length - 1].equals(extension));
	}

	public DeliveryRequest parseDeliveryRequest(String filePath) {
		DeliveryRequest delReq = new DeliveryRequest();

		if(!extensionCheck(filePath)) {
			ErrorLogger.getInstance().log(Error.NON_XML_DR );
			return delReq;
		}
		
		try {

			final Document doc = this.docBuilder.parse( new File(filePath) );
			System.out.println( "Parsing the file: " + filePath);

			final Element root = doc.getDocumentElement();

			NodeList nodes = root.getChildNodes();

			for(int i = 0; i < nodes.getLength(); i++) {
				if(nodes.item(i).getNodeType() != Node.ELEMENT_NODE)	continue;

				Element elem = (Element) nodes.item(i);
				switch( elem.getTagName() ) {
				case "livraison":

					Delivery del = new Delivery();
					del.setAddress(elem.getAttribute("adresse"));
					del.setDuration(Integer.parseInt(elem.getAttribute("duree")));

					delReq.addDelivery(del);

					break;

				case "entrepot":

					delReq.setWarehouseAddress( elem.getAttribute("adresse"));

					String[] hdStr = elem.getAttribute("heureDepart").split(":");
					int sec = Integer.parseInt(hdStr[2]);
					int min = Integer.parseInt(hdStr[1]);
					int hour = Integer.parseInt(hdStr[0]);
					Time hd = new Time(hour, min, sec);		
					delReq.setDepartureTime(hd);

					break;
				}
			}
		} catch (final SAXException e) {
			e.printStackTrace();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}

		return delReq;
	}

	/**
	 * 
	 * @param filePath the path to the xml file you want to convert in a Map
	 * @return the Map converted
	 */
	public CityMap parseMap(String filePath) {
		CityMap map = new CityMap();
		
		if(!extensionCheck(filePath)) {
			ErrorLogger.getInstance().log(Error.NON_XML_CM );
			return map;
		}
		
		try {
			final Document doc = this.docBuilder.parse( new File(filePath) );
			System.out.println( "Parsing the file: " + filePath);

			final Element root = doc.getDocumentElement();

			NodeList nodes = root.getChildNodes();
			// If we keep list in the Plan object, this will stay, tho, 
			// this IMHO should be handled by the class Plan

			for(int i = 0; i < nodes.getLength(); i++) {
				if(nodes.item(i).getNodeType() != Node.ELEMENT_NODE)	continue;

				Element elem = (Element) nodes.item(i);
				switch( elem.getTagName() ) {
				case "noeud":
					Geolocation geoI = new Geolocation(
							Double.parseDouble(elem.getAttribute("latitude")),
							Double.parseDouble(elem.getAttribute("longitude"))
							);
					Intersection inter = new Intersection(geoI, elem.getAttribute("id"));

					map.AddIntersection(inter);

					break;
				case "troncon":
					Section sec = new Section();
					sec.setLength(Double.parseDouble(elem.getAttribute("longueur")));
					sec.setStreetName(elem.getAttribute("nomRue"));
					sec.setStartIntersection( map.getIntersectionById(elem.getAttribute("origine")) );
					sec.setEndIntersection( map.getIntersectionById(elem.getAttribute("destination")) );

					map.AddSection(sec);

					break;
				}
			}
		} catch (final SAXException e) {
			e.printStackTrace();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}

		return map;
	}
}
