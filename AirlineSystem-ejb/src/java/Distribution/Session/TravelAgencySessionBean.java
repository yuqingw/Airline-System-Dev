/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;


import CI.Entity.Salt;
import Distribution.Entity.TravelAgency;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import APS.Entity.Flight;
import Distribution.Entity.PNR;
import Distribution.Entity.TravelAgency;
import Inventory.Entity.Booking;
import Inventory.Entity.SeatAvailability;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import javax.mail.NoSuchProviderException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author parthasarthygupta
 */
@Stateless
public class TravelAgencySessionBean implements TravelAgencySessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    
    private TravelAgency travelAgency;
    
    @Override
    public String addTravelAgency(String name, double maxCredit, double currentCredit, double commission, String email, String address, String contactNo, String password, String primaryContact) {

        try {

            travelAgency = new TravelAgency();
            String saltCode = generateSalt();
            String hashedPwd = getSecurePassword(password, saltCode);
            Salt salt = new Salt();
            salt.create(saltCode);
            travelAgency.setSalt(salt);
            em.persist(salt);

            travelAgency.createTravelAgent(name, maxCredit, maxCredit, 0.0, email, address, contactNo, hashedPwd, primaryContact);
            
            em.persist(travelAgency);

        } catch (NoSuchAlgorithmException ex) {

        } catch (NoSuchProviderException ex) {

        } catch (java.security.NoSuchProviderException ex) {

        }

        return "Sign up successful!";
    }
    
        // Password encryption use MD 5 hashing
    @Override
    public String generateSalt() throws NoSuchAlgorithmException, NoSuchProviderException, java.security.NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt.toString();
    }

    @Override
    public String getSecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    
    @Override
    public boolean isSameHash(String email, String pwd) {

        TravelAgency agency = getAgencyUseEmail(email);
        String saltCode = agency.getSalt().getSaltCode();
        String rehash = getSecurePassword(pwd, saltCode);

        if (agency.getPassword().equals(rehash)) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public TravelAgency getAgencyUseEmail(String agencyEmail) {
        TravelAgency agency = new TravelAgency();
        try {

            Query q = em.createQuery("SELECT a FROM TravelAgency " + "AS a WHERE a.email=:email");
            q.setParameter("email", agencyEmail);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                agency = (TravelAgency) results.get(0);

            } else {
                agency = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return agency;
    }
    

    @Override
    public void persistTravelAgency(TravelAgency travelAgency) {
        em.persist(travelAgency);
    }

    @Override
    public void resetCreditsAndCommission(TravelAgency travelAgency) {
        travelAgency.setCommission(0.0);
        travelAgency.setCurrentCredit(travelAgency.getMaxCredit());
        em.merge(travelAgency);
        em.flush();
    }

    @Override
    public void changeCreditLimit(TravelAgency travelAgency, double newLimit) {
        travelAgency.setMaxCredit(newLimit);
        em.merge(travelAgency);
        em.flush();
    }

    @Override
    public List<PNR> retrievePendingPNRs(TravelAgency travelAgency) {
        if (travelAgency.getPnrs() == null || travelAgency.getPnrs().isEmpty()) {
            return null;
        } else {
            List<PNR> pendingPNRs = new ArrayList();
            for (PNR eachTravelAgencyPNR : travelAgency.getPnrs()) {
                if (eachTravelAgencyPNR.getPnrStatus().equalsIgnoreCase("Pending")) {
                    pendingPNRs.add(eachTravelAgencyPNR);
                }
            }
            return pendingPNRs;
        }
    }

    @Override
    public int noOfDaysSinceDate(Date date) {
        Date currentDate = new Date();
        long diff = currentDate.getTime() - date.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public void deletePNR(PNR pnr) {
        SeatAvailability seatAvailForBooking = new SeatAvailability();
        String serviceType;
        int noOfSeatsBooked;

        for (Booking eachPnrBooking : pnr.getBookings()) {
            seatAvailForBooking = eachPnrBooking.getSeatAvail();
            serviceType = eachPnrBooking.getServiceType();
            if (serviceType.equalsIgnoreCase("Economy Saver")) {
                noOfSeatsBooked = seatAvailForBooking.getEconomySaverBooked();
                seatAvailForBooking.setEconomySaverBooked(noOfSeatsBooked - 1);
            } else if (serviceType.equalsIgnoreCase("Economy Basic")) {
                noOfSeatsBooked = seatAvailForBooking.getEconomyBasicBooked();
                seatAvailForBooking.setEconomyBasicBooked(noOfSeatsBooked - 1);
            } else if (serviceType.equalsIgnoreCase("Economy Premium")) {
                noOfSeatsBooked = seatAvailForBooking.getEconomyPremiumBooked();
                seatAvailForBooking.setEconomyPremiumBooked(noOfSeatsBooked - 1);
            } else if (serviceType.equalsIgnoreCase("Business")) {
                noOfSeatsBooked = seatAvailForBooking.getBusinessBooked();
                seatAvailForBooking.setBusinessBooked(noOfSeatsBooked - 1);
            } else if (serviceType.equalsIgnoreCase("First Class")) {
                noOfSeatsBooked = seatAvailForBooking.getFirstClassBooked();
                seatAvailForBooking.setFirstClassBooked(noOfSeatsBooked - 1);
            }
            seatAvailForBooking.getBookings().remove(eachPnrBooking);
            em.merge(seatAvailForBooking);
            eachPnrBooking.setBookingStatus("Cancelled");
            eachPnrBooking.setPnr(null);
            em.merge(eachPnrBooking);
        }
        pnr.setPnrStatus("Cancelled");
        em.merge(pnr);
        em.flush();
    }

    @Override
    public void deletePendingPNRs() {
        List<PNR> pnrsForEachTravelAgency = new ArrayList();
        List<TravelAgency> allTravelAgencies = getAllTravelAgencies();
        if (allTravelAgencies != null) {
            for (TravelAgency eachTravelAgency : allTravelAgencies) {
                pnrsForEachTravelAgency = eachTravelAgency.getPnrs();
                if (pnrsForEachTravelAgency != null && !pnrsForEachTravelAgency.isEmpty()) {
                    for (PNR eachPNR : pnrsForEachTravelAgency) {
                        if (eachPNR.getPnrStatus().equalsIgnoreCase("Pending") && noOfDaysSinceDate(eachPNR.getDateOfBooking()) > 14) {
                            deletePNR(eachPNR);
                        }
                    }

                }
            }
        }

    }

    @Override
    public List<TravelAgency> getAllTravelAgencies() {
        List<TravelAgency> allTravelAgencies = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a from TravelAgency a");

            List<TravelAgency> results = q.getResultList();
            if (!results.isEmpty()) {

                allTravelAgencies = results;

            } else {
                allTravelAgencies = null;
                System.out.println("no travel agencies!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allTravelAgencies;
    }

}