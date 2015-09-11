/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.OrganizationUnit;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface DepartmentSessionBeanLocal {
    public void addDepartment(String departName, String departLocation);
    public List<String> retrive();
    public List<OrganizationUnit> retrieveAllDepts();
}
