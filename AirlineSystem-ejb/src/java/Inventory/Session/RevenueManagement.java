/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import Inventory.Entity.SeatAvailability;
import APS.Entity.Flight;
import Inventory.Entity.BookingClass;


/**
 *
 * @author YiQuan
 */
@Stateless
@LocalBean
public class RevenueManagement implements RevenueManagementLocal{
    
    private Calendar cflightDate;
    private Date rDate;
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public int[] generateAvailability(int economy, int business, int firstClass){
        int[] seats = new int[5];
        seats[0]= (economy/3)+5;
        seats[1]= (economy/3)+5;
        seats[2]= (economy/3)+5;
        seats[3]= business+5;
        seats[4]= business+5;
        return seats;
    }
    
    public void createAvailability(String flightNo, String flightDate, 
            String flightTime){
        
        SeatAvailability sa = new SeatAvailability();
        int[] seats = generateAvailability(40,20,10);
        System.out.println(flightDate);
        int day = Integer.parseInt(flightDate.substring(0,2));
        System.out.println(day);
        int month = Integer.parseInt(flightDate.substring(2,4));
        System.out.println(month);
        int year = Integer.parseInt(flightDate.substring(4));
        System.out.println(year);
        int hour = Integer.parseInt(flightTime.substring(0,2));
        System.out.println(hour);
        int min = Integer.parseInt(flightTime.substring(2));
        System.out.println(min);
        //cflightDate.set(year,month,day,hour,min);
        System.out.println("Create Availability()!!!!!!");
        createSQ001();
        Flight flight= em.find(Flight.class,flightNo);
        sa.createSeatAvail(flight, seats, rDate, cflightDate.getTime());
        em.persist(sa);
        System.out.println("Seat Availability Persisted!!!!!!");
    }
    
    public void createSQ001(){
        Flight flight= new Flight();
        
        flight.createFlight("SQ001", "10", "10", 5.0, 100.0);
        em.persist(flight);
    }
    
    public int getPrice(Date fDate, String serviceClass, int realSold){
        int pricePercent=100;
        Date current = new Date();
        try{
            Query q = em.createQuery("SELECT o from BookingClass o WHERE o.serviceClass = :serviceClass");
            q.setParameter("serviceClass", serviceClass);
            List<BookingClass> classList = q.getResultList();
            int size = classList.size();
            long days = fDate.getTime()-current.getTime();
            for(int i=0;i<size;i++){
                BookingClass b = classList.get(i);
                int daysOut = b.getAdvancedSales();
                int sold = b.getPercentSold();
                if (realSold >= sold && days<=daysOut){
                    pricePercent= b.getPricePercent();
                }
            }
            return pricePercent;
        }
        catch(IllegalArgumentException ex){
            System.out.println(ex);
            return 100;
        }
        
    }
    
    
}
