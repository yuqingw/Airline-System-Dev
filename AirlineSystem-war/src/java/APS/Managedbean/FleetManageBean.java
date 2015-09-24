/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import APS.Entity.Schedule;
import APS.Session.FleetSessionBeanLocal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Yunna
 */
@Named(value = "fleetManageBean")
@ManagedBean
@SessionScoped

public class FleetManageBean {

    @EJB
    private FleetSessionBeanLocal fleetSessionBean;

    Date datePurchased;
    Date lastMaintained;
    String aircraftTypeId;
    Long tailNo;
    String status;
    String hub;

    AircraftType aircraftType = new AircraftType();

    String id;
    double speed;
    int travelRange;
    int firstSeats;
    int businessSeats;
    int economySeats;
    int totalStaff;
    double cost;
    double fuelCost;
    FacesMessage message;

    private List<AircraftType> aircraftTypes = new ArrayList<AircraftType>();
    private List<Aircraft> aircrafts;
    private Aircraft selectedAircraft;
    private List<String> aircraftTypeIds = new ArrayList<String>();
    private List<Aircraft> reserves = new ArrayList<Aircraft>();

    public FleetManageBean() {
    }

    @PostConstruct
    public void retrieve() {

        setAircraftTypes(fleetSessionBean.retrieveAircraftTypes());
        setAircrafts(fleetSessionBean.retrieveAircrafts());
    }

    /*This is for admin to acquire new aircraft*/
    public void acquireAircraft(ActionEvent event) {

        if (aircraftTypeId == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select aircraft type!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (hub == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select location of the aircraft!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (datePurchased == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter date of purchase!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (lastMaintained == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter date of last maintenance!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (status == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select status of the aircraft!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        fleetSessionBean.acquireAircraft(datePurchased, lastMaintained, aircraftTypeId, hub, status);
    }

    public void retireAircraft(ActionEvent event) {

//        if (!selectedAircraft.getStatus().equals("Stand-By") || !selectedAircraft.getStatus().equals("Reserve")) {
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aircraft is not in a status to delete!", "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            return;
//        }
        reserves = fleetSessionBean.getReserveAircrafts("Reserve");

        System.out.println("LOOK HERE");
        System.out.println(reserves.get(1));

        for (int i = 0; i < selectedAircraft.getSchedules().size(); i++) {
            selectedAircraft.getSchedules().get(i).setAircraft(reserves.get(1));
            fleetSessionBean.persistSchedule(selectedAircraft.getSchedules().get(i));
            System.out.println("LOOK HERE");
            System.out.println(selectedAircraft.getSchedules().get(i).getAircraft());
        }
        reserves.get(1).setStatus("Stand-By");
        List<Schedule> temp = selectedAircraft.getSchedules();
        reserves.get(1).setSchedules(temp);
        fleetSessionBean.persistAircraft(reserves.get(1));

        List<Aircraft> temp1 = selectedAircraft.getAircraftType().getAircrafts();
        temp1.remove(selectedAircraft);
        selectedAircraft.getAircraftType().setAircrafts(temp1);
        selectedAircraft.setAircraftType(null);

        System.out.println("Hurray");

        aircrafts.remove(selectedAircraft);
        fleetSessionBean.retireAircraft(selectedAircraft.getTailNo());
        selectedAircraft = null;
    }

    public String getDateFormat(Date date) {
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");
        return df.format(date);
    }

    public List<String> getAircraftTypeIds() {
        return aircraftTypeIds;
    }

    public void setAircraftTypeIds(List<String> aircraftTypeIds) {
        this.aircraftTypeIds = aircraftTypeIds;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public List<Aircraft> getReserves() {
        return reserves;
    }

    public void setReserves(List<Aircraft> reserves) {
        this.reserves = reserves;
    }

    public Aircraft getSelectedAircraft() {
        return selectedAircraft;
    }

    public void setSelectedAircraft(Aircraft selectedAircraft) {
        this.selectedAircraft = selectedAircraft;
    }

    public Date getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }

    public Date getLastMaintained() {
        return lastMaintained;
    }

    public void setLastMaintained(Date lastMaintained) {
        this.lastMaintained = lastMaintained;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getAircraftTypeId() {
        return aircraftTypeId;
    }

    public void setAircraftTypeId(String aircraftTypeId) {
        this.aircraftTypeId = aircraftTypeId;
    }

    public Long getTailNo() {
        return tailNo;
    }

    public void setTailNo(Long tailNo) {
        this.tailNo = tailNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public List<AircraftType> getAircraftTypes() {
        return aircraftTypes;
    }

    public void setAircraftTypes(List<AircraftType> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getTravelRange() {
        return travelRange;
    }

    public void setTravelRange(int travelRange) {
        this.travelRange = travelRange;
    }

    public int getTotalStaff() {
        return totalStaff;
    }

    public void setTotalStaff(int totalStaff) {
        this.totalStaff = totalStaff;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public int getFirstSeats() {
        return firstSeats;
    }

    public void setFirstSeats(int firstSeats) {
        this.firstSeats = firstSeats;
    }

    public int getBusinessSeats() {
        return businessSeats;
    }

    public void setBusinessSeats(int businessSeats) {
        this.businessSeats = businessSeats;
    }

    public int getEconomySeats() {
        return economySeats;
    }

    public void setEconomySeats(int economySeats) {
        this.economySeats = economySeats;
    }

}
