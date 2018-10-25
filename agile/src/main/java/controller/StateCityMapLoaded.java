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
public class StateCityMapLoaded extends StateDefault{
    
    public DeliveryRequest LoadDeliveryRequest(String path,Controller c){
        
        try{
            DeliveryRequest dr = null;
            XmlParser parser = new XmlParser();
            dr = parser.parseDeliveryRequest(path);
            dr.computeDeliveryRequestGeolocation(c.getCityMap());
            return dr;
        }catch (Exception e){
            return null;
        }
    }
    
}
