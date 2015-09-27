/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import APS.Entity.Location;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import Inventory.Entity.BookingClass;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author YiQuan
 */
@Stateless
public class ClassManagement implements ClassManagementLocal {
    
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    public String addClassCode(String classcode, int pricePercent, int advancedSales
    , int percentSold, String serviceClass, boolean rebook, boolean cancel, 
    int baggage, int millageAccru){
        BookingClass bc = new BookingClass();
        BookingClass bc1 = em.find(BookingClass.class,classcode);
        if (bc1==null){
            bc.createClass(classcode, pricePercent, advancedSales, percentSold,
                    serviceClass, rebook, cancel, baggage, millageAccru);
            em.persist(bc);
            return "Fare Class Added";
        }
        else{
            return "Fare Class Already Exists";
        }
        
    }
    
    public BookingClass findClass(String classCode){
        BookingClass bc = em.find(BookingClass.class, classCode);
        return bc;
    }
    
    public List<BookingClass> retrieveBookingClasses(){
        List<BookingClass> allClasses = new ArrayList<BookingClass>();
        
        try{
            Query q = em.createQuery("SELECT a from BookingClass a");
            
            List<BookingClass> results = q.getResultList();
            if (!results.isEmpty()){
                
                allClasses = results;
                
            }else
            {
                allClasses = null;
                System.out.println("No Classes!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       
        return allClasses;
    }
    
    public void deleteClassCode(String classcode){
        try{
            BookingClass bc = em.find(BookingClass.class, classcode);
            em.remove(bc);
        }
        catch(EntityNotFoundException ex){
            System.out.println("\nEntity Not Found" );
            System.out.println(ex);
        }
    }
    
    public void editClassCode(BookingClass bc){
        em.merge(bc);
    }
    
}
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

