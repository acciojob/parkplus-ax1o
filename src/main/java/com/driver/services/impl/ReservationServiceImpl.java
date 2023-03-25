package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Reservation reservation = new Reservation();

        //search user
        User user;
        try{
            user = userRepository3.findById(userId).get();
        }catch (Exception e){
            throw new Exception("user not found");
        }

        //search parking lot
        ParkingLot parkingLot;
        try{
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }catch (Exception e){
            throw new Exception("parking not found");
        }

        List<Spot> spots = parkingLot.getSpotList();

        if(numberOfWheels > 4){
            numberOfWheels = 5;
        }

        Spot spotParked = new Spot();
        int pricePerHour = Integer.MAX_VALUE;
        boolean found = false;

        //find the best spot
        for(Spot spot : spots){

            if(spot.isOccupied()==false){
                int wheelsForSpot = 0;
                if(spot.getSpotType().equals(SpotType.TWO_WHEELER))
                    wheelsForSpot = 2;
                else if(spot.getSpotType().equals(SpotType.FOUR_WHEELER))
                    wheelsForSpot = 4;
                else
                    wheelsForSpot = 5;

                //check if the vehicle can be parked in the spot or not
                //and also the price per hour is min
                if(numberOfWheels <= wheelsForSpot && pricePerHour > spot.getPricePerHour()){

                    spotParked = spot;
                    pricePerHour = spot.getPricePerHour();
                    found = true;
                }




            }

        }

        //no parking spot available
        if(found == false){
            throw new Exception("Slot not found");
        }

        //spot is occupied
        spotParked.setOccupied(true);
        reservation.setSpot(spotParked);
        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);

        //set reservation in spot list
        List<Reservation> spotParkedReservationListList = spotParked.getReservationList();
        spotParkedReservationListList.add(reservation);
       // spotRepository3.save(spotParked);

        //set user reservation list
        List<Reservation> userReservationList =user.getReservationList();
        userReservationList.add(reservation);
        user.setReservationList(userReservationList);
        //userRepository3.save(user);

        reservationRepository3.save(reservation);

        //set parking spot
        //parkingLotRepository3.save(parkingLot);




        return reservation;
    }
}
