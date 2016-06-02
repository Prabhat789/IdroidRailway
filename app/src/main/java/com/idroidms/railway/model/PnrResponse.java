package com.idroidms.railway.model;

import java.util.List;

/**
 * Created by Prabhat on 26/03/16.
 */
public class PnrResponse {

    String response_code;
    String error;
    String train_name;
    String train_num;
    String pnr;
    String doj;
    String chart_prepared;
    String total_passengers;
    FromStation from_station;
    FromStation boarding_point;
    FromStation to_station;
    TrainStartDate train_start_date;

    public TrainStartDate getTrain_start_date() {
        return train_start_date;
    }

    public void setTrain_start_date(TrainStartDate train_start_date) {
        this.train_start_date = train_start_date;
    }

    FromStation reservation_upto;
    List<Passengers> passengers;

    public List<Passengers> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passengers> passengers) {
        this.passengers = passengers;
    }

    public FromStation getFrom_station() {
        return from_station;
    }

    public void setFrom_station(FromStation from_station) {
        this.from_station = from_station;
    }

    public FromStation getBoarding_point() {
        return boarding_point;
    }

    public void setBoarding_point(FromStation boarding_point) {
        this.boarding_point = boarding_point;
    }

    public FromStation getTo_station() {
        return to_station;
    }

    public void setTo_station(FromStation to_station) {
        this.to_station = to_station;
    }

    public FromStation getReservation_upto() {
        return reservation_upto;
    }

    public void setReservation_upto(FromStation reservation_upto) {
        this.reservation_upto = reservation_upto;
    }


    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTrain_num() {
        return train_num;
    }

    public void setTrain_num(String train_num) {
        this.train_num = train_num;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getChart_prepared() {
        return chart_prepared;
    }

    public void setChart_prepared(String chart_prepared) {
        this.chart_prepared = chart_prepared;
    }

    public String getTotal_passengers() {
        return total_passengers;
    }

    public void setTotal_passengers(String total_passengers) {
        this.total_passengers = total_passengers;
    }

}
