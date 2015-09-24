/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;


import FOS.Entity.ChecklistItem;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface ChecklistSessionBeanLocal {

    public void addChecklistItem (String checklistName, String itemName);
    public List<String> retrieveAllChecklists();
    public List<ChecklistItem> retrieveChecklistItems (String checklistName);
    public ChecklistItem findItem (Long key);
    public void editChecklistItem (ChecklistItem item);
    public void deleteChecklistItem (Long key, String checklistName);
}
