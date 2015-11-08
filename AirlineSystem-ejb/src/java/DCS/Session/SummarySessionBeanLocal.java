/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import APS.Entity.Schedule;
import DCS.Entity.CheckInRecord;
import Inventory.Entity.Booking;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface SummarySessionBeanLocal {

    public Schedule retrieveSchedule(String flightNumber, Date date);

    public List<CheckInRecord> retrieveRecords(List<Booking> bookings);
}
