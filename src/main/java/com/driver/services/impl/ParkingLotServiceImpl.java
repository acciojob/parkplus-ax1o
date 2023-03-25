package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);

        parkingLotRepository1.save(parkingLot);

        return parkingLot;

    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot = new Spot();
        ParkingLot parkingLot;
        parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        //setting spot
        spot.setParkingLot(parkingLot);
        spot.setOccupied(false);
        spot.setPricePerHour(pricePerHour);
        if(numberOfWheels == 4){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else if(numberOfWheels == 2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }else{
            spot.setSpotType(SpotType.OTHERS);
        }

        //adding spot to the list of spots in parking lot
        List<Spot> spots = parkingLot.getSpotList();
        spots.add(spot);
        parkingLot.setSpotList(spots);

        spotRepository1.save(spot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        //updating spot
        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);

        //saving spot
        parkingLotRepository1.save(parkingLot);

        //check if the spot is updated or not
        //check if set spot stores the correct parking lot
//        Spot spot1 = spotRepository1.findById(spotId).get();
//        ParkingLot pl = spot1.getParkingLot();
//        System.out.println(pl.getAddress());


        return spot;

    }

    @Override
    public void deleteParkingLot(int parkingLotId) {

        parkingLotRepository1.deleteById(parkingLotId);


    }
}
