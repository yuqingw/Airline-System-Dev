/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;

import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Inventory.Session.BookingSessionBeanLocal;
import javax.ejb.EJB;

/**
 *
 * @author YiQuan
 */
@Named(value = "FlightDataManagedBean")
@ManagedBean
@SessionScoped
public class FlightDataManagedBean {
     @EJB
    private BookingSessionBeanLocal bs;
     
     

    /**
     * Creates a new instance of flightDataManageBean
     */
    public FlightDataManagedBean() {
    }
    
   
    
    
    private String flightNo;
    
    public  void createData(){
        bs.bookSeats(flightNo);
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }
    
}