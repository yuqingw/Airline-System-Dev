/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import static APS.Entity.Schedule_.flight;
import Inventory.Entity.SeatAvailability;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author YiQuan
 */
public class PricingSessionBeanTest {
    
    private PricingSessionBeanRemote pm = lookup();
    
    public PricingSessionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of generateAvailability method, of class PricingSessionBean.
     */
    @Test
    public void testGenerateAvailability() throws Exception {
        System.out.println("generateAvailability");
        String flightNo = "";
        int economy = 60;
        int business = 15;
        int firstClass = 5;
        int[] result =  new int[5];
        result[0]= 25;
        result[1]= 25;
        result[2]= 25;
        result[3]= 20;
        result[4]= 10;
        assertArrayEquals(result, pm.generateAvailability("MA202", economy, business, firstClass));
        assertNotNull(pm.generateAvailability("MA303", economy, business, firstClass));
    }
    
    @Test
    public void testcalTurnOut(){
        System.out.println("calTurnOut");
        String flightNo ="MA777";
        String serviceClass = "Business";
        String result = pm.calTurnOut(flightNo, serviceClass);
        assertNotNull(result);
    }
    
    @Test
    public void testfindSA(){
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 11, 25, 12, 0);
        System.out.println("calTurnOut");
        System.out.println(cal.getTime());
        String flightNo ="MA777";
        SeatAvailability result = pm.findSA(cal.getTime(), flightNo);
        assertNotNull(result);
    }
    
    @Test
    public void testgetPrice(){
        Schedule s = new Schedule();
        Flight f = new Flight();
        f.setBasicFare(200);
        s.setFlight(f);
        String test1 = "C14";
        String test2 = "A11";
        String test3 = "E15";
        System.out.println("getPrice");
        String result1 = Double.toString(pm.getPrice(test1, s));
        String result2 = Double.toString(pm.getPrice(test2, s));
        String result3 = Double.toString(pm.getPrice(test3, s));
        assertEquals("400.0", result1);
        assertEquals("120.0", result2);
        assertEquals("1000.0", result3);  

    }
    
    @Test
    public void testgetClassCode(){
        //Case 1
        Schedule s1 = new Schedule();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(cal1.getTimeInMillis()+((long)19*1000*60*60*24));
        Date date1 = cal1.getTime();
        s1.setStartDate(date1);
        SeatAvailability sa1 = new SeatAvailability();
        sa1.setEconomySaverTotal(90);
        sa1.setEconomySaverBooked(9);
        s1.setSeatAvailability(sa1);
        String result1= pm.getClassCode(s1, "Economy Saver", 3, false);
        assertEquals("A12", result1);
        
        //Case 2
        Schedule s2 = new Schedule();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(cal2.getTimeInMillis()+((long)59*1000*60*60*24));
        Date date2 = cal2.getTime();
        s2.setStartDate(date2);
        SeatAvailability sa2 = new SeatAvailability();
        sa2.setBusinessTotal(20);
        sa2.setBusinessBooked(10);
        s2.setSeatAvailability(sa2);
        Flight flight = new Flight();
        Route route = new Route();
        route.setDestinationIATA("KUL");
        route.setOriginIATA("SIN");
        flight.setRoute(route);
        s2.setFlight(flight);
        String result2= pm.getClassCode(s2, "Business", 1, false);
        assertEquals("D23", result2);
        
        //Case 3
        Schedule s3 = new Schedule();
        Calendar cal3 = Calendar.getInstance();
        cal3.setTimeInMillis(cal3.getTimeInMillis()+((long)299*1000*60*60*24));
        Date date3 = cal3.getTime();
        System.out.println("Look Here@@!!!");
        System.out.println(date3);
        s3.setStartDate(date3);
        SeatAvailability sa3 = new SeatAvailability();
        sa3.setFirstClassTotal(10);
        sa3.setFirstClassBooked(0);
        s3.setSeatAvailability(sa3);      
        Route route2 = new Route();
        Flight flight2 = new Flight();
        route2.setDestinationIATA("IPH");
        route2.setOriginIATA("SIN");
        flight2.setRoute(route2);
        s3.setFlight(flight2);
        String result3= pm.getClassCode(s3, "First Class", 1, false);
        assertEquals("E03", result3);
        
        //Case 4
        Schedule s4 = new Schedule();
        Calendar cal4 = Calendar.getInstance();
        cal4.setTimeInMillis(cal4.getTimeInMillis()+((long)4*1000*60*60*24));
        Date date4 = cal4.getTime();
        s4.setStartDate(date4);
        SeatAvailability sa4 = new SeatAvailability();
        sa4.setEconomyPremiumTotal(50);
        sa4.setEconomyPremiumBooked(48);
        s4.setSeatAvailability(sa4);
        s4.setFlight(flight);
        String result4 = pm.getClassCode(s4, "Economy Premium", 1, false);
        assertEquals("C25", result4);
        
        //Case 5
        Schedule s5 = new Schedule();
        Calendar cal5 = Calendar.getInstance();
        cal5.setTimeInMillis(cal5.getTimeInMillis()+((long)4*1000*60*60*24));
        System.out.println(cal5.getTime());
        Date date5 = cal5.getTime();
        System.out.println(date5);
        s5.setStartDate(date5);
        SeatAvailability sa5 = new SeatAvailability();
        sa5.setEconomyBasicTotal(50);
        sa5.setEconomyBasicBooked(10);
        s5.setSeatAvailability(sa5);
        s5.setFlight(flight2);
        String result5 = pm.getClassCode(s5, "Economy Basic", 1, false);
        assertEquals("B01", result5);
     
    }
    
    
    private PricingSessionBeanRemote lookup() 
    {
        try 
        {
            Context c = new InitialContext();
            return (PricingSessionBeanRemote) c.lookup("java:global/AirlineSystem-ejb/PricingSessionBean!Inventory.Session.PricingSessionBeanRemote");
        }
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
  
}

  