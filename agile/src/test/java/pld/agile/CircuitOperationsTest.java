/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pld.agile;

import controller.Controller;
import junit.framework.*;
import model.*;
import java.util.LinkedList;
import utils.XmlParser;

/**
 *
 * @author Lucie
 */
public class CircuitOperationsTest extends TestCase {

    /**
     * test nb of circuits and nb of deliveries per circuit
     *
     * @throws Exception
     */
    public void testCircuitComputation() throws Exception {
        Controller c = new Controller();
        CityMap cityMap = null;
        Model model = c.getModel();
        XmlParser xmlParser = new XmlParser();
        cityMap = xmlParser.parseMap("src/main/assets/maps/petitPlan.xml");

        if (cityMap != null) {
            model.setCityMap(cityMap);
            model.setDeliveryRequest(null);
            model.resetShortestPaths();
            model.setCircuits(null);
        }

        DeliveryRequest delReq = null;
        delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");

        if (delReq != null) {
            delReq.computeDeliveryRequestGeolocation(model.getCityMap());
            model.setDeliveryRequest(delReq);
            model.computeShortestPaths();
            model.setCircuits(null);
        }

        model.setNumberOfCouriers(1);
        model.computeCircuits();
        
        assertEquals(1, model.getCircuits().size());
        
        
        model.setNumberOfCouriers(3);
        model.computeCircuits();

        assertEquals(3, model.getCircuits().size());
    }

    /**
     * make a delivery request with already the added delivery and a circuit
     * with already the added delivery make a dr and a circuit without and then
     * add the delivery check the circuit and dr are equal
     *
     * @throws Exception
     */
    public void testAddDelivery() throws Exception {
        DeliveryRequest dr = new DeliveryRequest();

    }

    /**
     * make a delivery request with the delivery and a circuit with the delivery
     * make a dr and a circuit without and then delete the delivery check the
     * circuit and dr are equal
     *
     * @throws Exception
     */
    public void testDeleteDelivery() throws Exception {

    }

    /**
     * make a dr and a circuit with the delivery already moved make a dr and a
     * circuit wth the delivery not moved move the delivery check the circuit
     * and dr are equal do it for a delivery in the middle of the circuit and
     * for one that is the first of the circuit (it shall not move then)
     *
     * @throws Exception
     */
    public void testMoveBefore() throws Exception {

    }

    /**
     * make a dr and a circuit with the delivery already moved make a dr and a
     * circuit wth the delivery not moved move the delivery check the circuit
     * and dr are equal do it for a delivery in the middle of the circuit and
     * for one that is the first of the circuit (it shall not move then)
     *
     * @throws Exception
     */
    public void testMoveAfter() throws Exception {

    }

}
