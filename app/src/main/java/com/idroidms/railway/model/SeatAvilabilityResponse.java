package com.idroidms.railway.model;

import java.util.List;

/**
 * Created by Prabhat on 27/03/16.
 */
public class SeatAvilabilityResponse {

    String response_code;
    String train_number;
    String train_name;
    String error;
    From from;
    From to;
    List<Avilability> availability;
    Quota quota;
    LastUpadte last_updated;

    public LastUpadte getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(LastUpadte last_updated) {
        this.last_updated = last_updated;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getTrain_number() {
        return train_number;
    }

    public void setTrain_number(String train_number) {
        this.train_number = train_number;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public From getTo() {
        return to;
    }

    public void setTo(From to) {
        this.to = to;
    }

    public List<Avilability> getAvailability() {
        return availability;
    }

    public void setAvailability(List<Avilability> availability) {
        this.availability = availability;
    }

    public Quota getQuota() {
        return quota;
    }

    public void setQuota(Quota quota) {
        this.quota = quota;
    }
}
