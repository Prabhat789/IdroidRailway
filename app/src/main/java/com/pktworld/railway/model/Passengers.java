package com.pktworld.railway.model;

/**
 * Created by Prabhat on 26/03/16.
 */
public class Passengers {
    String booking_status;
    String current_status;
    String coach_position;

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(String current_status) {
        this.current_status = current_status;
    }

    public String getCoach_position() {
        return coach_position;
    }

    public void setCoach_position(String coach_position) {
        this.coach_position = coach_position;
    }
}
