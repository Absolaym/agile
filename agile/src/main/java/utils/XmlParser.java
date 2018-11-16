package utils;

import java.io.File;
import java.io.IOException;

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
import error.ProjectError;

/**
 * A class dedicated to the parsing of xml files
 *
 * @author Johnny
 */
public class XmlParser {

	private DocumentBuilderFactory docBuilderFactory;
	private DocumentBuilder docBuilder;

	/**
	 * Create the xml parser
	 */
	public XmlParser() {
		this.docBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			this.docBuilder = this.docBuilderFactory.newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse a delivery request given its absolute path
	 *
	 * @param filePath
	 * @return
	 */
	public DeliveryRequest parseDeliveryRequest(String filePath) {
		DeliveryRequest delReq = new DeliveryRequest();

		if (!extensionCheck(filePath)) {
			ErrorLogger.getInstance().log(ProjectError.NON_XML_DR);
			return delReq;
		}

		try {

			final Document doc = this.docBuilder.parse(new File(filePath));
			System.out.println("Parsing the file: " + filePath);

			final Element root = doc.getDocumentElement();

			NodeList nodes = root.getChildNodes();

			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}

				Element elem = (Element) nodes.item(i);
				switch (elem.getTagName()) {
				case "livraison":

					Delivery del = new Delivery();
					del.setAddress(elem.getAttribute("adresse"));
					del.setDuration(Integer.parseInt(elem.getAttribute("duree")));

					if (del.getDuration().time < 0) {
						ErrorLogger.getInstance().log(ProjectError.NEGATIVE_DURATION);
						break;
					}
					delReq.addDelivery(del);

					break;

				case "entrepot":

					delReq.setWarehouseAddress(elem.getAttribute("adresse"));

					String[] hdStr = elem.getAttribute("heureDepart").split(":");
					int sec = Integer.parseInt(hdStr[2]);
					int min = Integer.parseInt(hdStr[1]);
					int hour = Integer.parseInt(hdStr[0]);
					Time hd = new Time(hour, min, sec);
					delReq.setDepartureTime(hd);

					break;
				}

				//if(delReq.getWarehouseAddress())
			}
		} catch (final SAXException e) {
			ErrorLogger.getInstance().log(ProjectError.CANT_OPEN_FILE);
			e.printStackTrace();
		} catch (final IOException e) {
			ErrorLogger.getInstance().log(ProjectError.CANT_OPEN_FILE);
			e.printStackTrace();
		}

		return delReq;
	}

	/**
	 * Parse a map given its absolute path
	 *
	 * @param filePath the path to the xml file you want to convert in a Map
	 * @return the Map converted
	 */
	public CityMap parseMap(String filePath) {
		CityMap map = new CityMap();

		if (!extensionCheck(filePath)) {
			ErrorLogger.getInstance().log(ProjectError.NON_XML_CM);
			return null;
		}

		try {
			final Document doc = this.docBuilder.parse(new File(filePath));
			System.out.println("Parsing the file: " + filePath);

			final Element root = doc.getDocumentElement();

			NodeList nodes = root.getChildNodes();

			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}

				Element elem = (Element) nodes.item(i);
				switch (elem.getTagName()) {
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
					double length = Double.parseDouble(elem.getAttribute("longueur"));
					if (!checkPositive(length)) {
						ErrorLogger.getInstance().log(ProjectError.NEGATIVE_SECTION_LENGTH_VALUE);
						return null;
					}
					;
					sec.setLength(length);
					sec.setStreetName(elem.getAttribute("nomRue"));
					sec.setStartIntersection(map.getIntersectionById(elem.getAttribute("origine")));
					sec.setEndIntersection(map.getIntersectionById(elem.getAttribute("destination")));
					if(sec.getStartIntersection() == null || sec.getEndIntersection() == null ) {
						ErrorLogger.getInstance().log(ProjectError.CORRUPTED_XML_CM);
					}

					map.AddSection(sec);

					break;
				}
			}
		} catch (final SAXException e) {
			ErrorLogger.getInstance().log(ProjectError.CANT_OPEN_FILE);
			e.printStackTrace();
		} catch (final IOException e) {
			ErrorLogger.getInstance().log(ProjectError.CANT_OPEN_FILE);
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * Checks if the number is positive
	 *
	 * @param value the number
	 * @return true if the number is positive
	 */
	private boolean checkPositive(double value) {
		if (value < 0) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if the selected file is a .xml
	 *
	 * @param the path to the file
	 * @return true if the file is a .xml
	 */
	private boolean extensionCheck(String path) {
		return this.extensionCheck(path, "xml");
	}

	/**
	 * Checks if the selected file has a certain extension
	 *
	 * @param path the path to the file
	 * @param extension the extension in question
	 * @return true if the file has the said extension
	 */
	private boolean extensionCheck(String path, String extension) {
		String[] split = path.split("[.]");
		return (split.length > 1 && split[split.length - 1].equals(extension));
	}
}
