/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pld.agile;

import junit.framework.*;
/**
 *
 * @author Lucie
 */
public class CircuitOperationsTest  extends TestCase {
    
    /**
     * test nb of circuits and nb of deliveries per circuit
     */
    public void testCircuitComputation() throws Exception {
        
    }
    
    /**
     * make a delivery request with already the added delivery
     * and a circuit with already the added delivery
     * make a dr and a circit without and then add the delivery
     * check the circuit and dr are equal
     */
    public void testAddDelivery() throws Exception {
        
    }
    
    /**
     * make a delivery request with the delivery
     * and a circuit with the delivery
     * make a dr and a circit without 
     * and then delete the delivery
     * check the circuit and dr are equal
     */
    public void testDeleteDelivery() throws Exception {
        
    }
    
    /**
     * make a dr and a circuit with the delivery already moved
     * make a dr and a circuit wth the delivery not moved
     * move the delivery 
     * check the circuit and dr are equal
     * do it for a delivery in the middle of the circuit and
     * for one that is the first of the circuit (it shall not move then)
     * @throws Exception 
     */
    public void testMoveBefore() throws Exception{
        
    }
    
    /**
     * make a dr and a circuit with the delivery already moved
     * make a dr and a circuit wth the delivery not moved
     * move the delivery 
     * check the circuit and dr are equal
     * do it for a delivery in the middle of the circuit and
     * for one that is the first of the circuit (it shall not move then)
     * @throws Exception 
     */
    public void testMoveAfter() throws Exception{
        
    }
    
}
