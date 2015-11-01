/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import APS.Entity.Schedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Schedules;

/**
 *
 * @author Yunna
 */
@Local
public interface FleetSessionBeanLocal {

    public void acquireAircraft(Date datePurchased, Date lastMaintained, String aircraftTypeId, String hub, String status);
    public void retireAircraft(Long tailNo);
    public AircraftType getAircraftType(String aircraftTypeId);
    public Aircraft getAircraft(Long tailNum);
    public List<AircraftType> retrieveAircraftTypes();
    public List<Aircraft> retrieveAircrafts();
    public List<Aircraft> getReserveAircrafts(String status);
    public void persistSchedule(Schedule schedule);
    public void persistAircraft(Aircraft aircraft);
}
