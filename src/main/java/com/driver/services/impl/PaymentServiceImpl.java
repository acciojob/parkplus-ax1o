package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Payment payment = new Payment();

        Reservation reservation = reservationRepository2.findById(reservationId).get();

        int hours = reservation.getNumberOfHours();
        Spot spot = reservation.getSpot();

        int bill = hours * spot.getPricePerHour();

        if(bill > amountSent){
            throw new Exception("Insufficient Amount");
        }

        mode = mode.toUpperCase();
        //System.out.println(mode);

        if(mode.equals("UPI")){

            payment.setPaymentMode(PaymentMode.UPI);

        } else if (mode.equals("CARD")  ) {
            payment.setPaymentMode(PaymentMode.CARD);
        }
        else if(mode.equals("CASH")){
            payment.setPaymentMode(PaymentMode.CASH);
        }
        else{
            throw new Exception("Payment mode not detected");
        }

        //setting payment reference in reservation
        reservation.setPayment(payment);

        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);
        paymentRepository2.save(payment);

        return payment;
    }
}
