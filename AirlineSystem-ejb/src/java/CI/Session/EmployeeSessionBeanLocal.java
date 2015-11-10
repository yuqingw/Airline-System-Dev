/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.Employee;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface EmployeeSessionBeanLocal {
    
    public void addEmployee(String userID,String employeeID,String employeeDisplayFirstName,String employeeDisplayLastName,
                            String  employeeRole,String employeeDepartment,Date employeeDOB,
                            String employeeGender,String employeeHpNumber,String employeeMailingAddress,
                            String employeeOfficeNumber,String employeePrivateEmail,double employeeMonthlySalary);
    
    public Employee getEmployee(String employeeUserName);
    public Employee getEmployeeUseID(String employeeID); //get employee by using ID
    public void employeeActivateWithId(String employeeID);
    
    public String generateUserName(String employeeFirstName, String employeeLastName);
    public void hashPwd(String userName);
    public boolean isSameHash(String userName, String pwd);
    public void hashNewPwd(String userName, String pwd);
    public void employeeActivate(String userName);
    
    public void updateInfo(String employeeUserName, Date employeeDOB,String employeeGender, String employeeHomeAddress, String employeeOfficeNumber,
    String employeeHpNumber, String employeePrivateEmail);
    
    public void addCabinCrew(String employeeID, String employeeDisplayFirstName, String employeeDisplayLastName,
            String employeeDepartment,Date employeeDOB,String employeeGender, String employeeHpNumber, 
            String employeeMailingAddress, String employeeOfficeNumber, String employeePrivateEmail,
            String experience, List<String>language, String position, double employeeMonthlySalary);
    
    public void addPilot(String employeeID, String employeeDisplayFirstName, String employeeDisplayLastName,
            String employeeDepartment,Date employeeDOB,String employeeGender, String employeeHpNumber, 
            String employeeMailingAddress, String employeeOfficeNumber, String employeePrivateEmail,
            String experience, List<String>skills, String position, double employeeMonthlySalary);
    
    public void addGroundCrew(String employeeID, String employeeDisplayFirstName, String employeeDisplayLastName,
            String employeeDepartment,Date employeeDOB,String employeeGender, String employeeHpNumber, 
            String employeeMailingAddress, String employeeOfficeNumber, String employeePrivateEmail,
             String position,String mainCrewExp, double employeeMonthlySalary);
    
    public List<Employee> retrieveAllEmployees();
    public Boolean lockoutEmployee(String employeeID);
    public List<Employee> retrieveAllActiveEmployees();
    public String getEmployeeFullName(String employeeUserName);
    public String getEmployeeDepartmentName(String employeeUserName);
    
}
