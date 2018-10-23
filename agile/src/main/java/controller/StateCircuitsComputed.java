/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.DeliveryRequest;
import utils.XmlParser;

/**
 *
 * @author lgalle
 */
public class StateCircuitsComputed extends StateDefault{
    
    public DeliveryRequest LoadDeliveryRequest(String path){
        XmlParser parser = new XmlParser();
        DeliveryRequest dr = parser.parseDeliveryRequest(path);
        //dr = setDeliveryRequestGeolocation(dr);
        return dr;
    }
}
