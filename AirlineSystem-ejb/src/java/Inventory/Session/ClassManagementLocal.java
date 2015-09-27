/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;
import Inventory.Entity.BookingClass;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author YiQuan
 */
@Local
public interface ClassManagementLocal {
     public String addClassCode(String classcode, int pricePercent, int advancedSales
    , int percentSold, String serviceClass, boolean rebook, boolean cancel, 
    int baggage, int millageAccru);
     
     public void deleteClassCode(String classcode);
     
     public void editClassCode(BookingClass bc);
     
     public List<BookingClass> retrieveBookingClasses();
     
     public BookingClass findClass(String classCode);
}
