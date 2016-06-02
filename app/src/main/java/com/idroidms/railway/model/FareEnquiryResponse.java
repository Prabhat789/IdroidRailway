package com.idroidms.railway.model;

import java.util.List;

/**
 * Created by Prabhat on 13/04/16.
 */
public class FareEnquiryResponse {

    String response_code;
    String failure_rate;
    List<Fare> fare;
    Train train;
    From from;
    From quota;
    From to;

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getFailure_rate() {
        return failure_rate;
    }

    public void setFailure_rate(String failure_rate) {
        this.failure_rate = failure_rate;
    }

    public List<Fare> getFare() {
        return fare;
    }

    public void setFare(List<Fare> fare) {
        this.fare = fare;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public From getQuota() {
        return quota;
    }

    public void setQuota(From quota) {
        this.quota = quota;
    }

    public From getTo() {
        return to;
    }

    public void setTo(From to) {
        this.to = to;
    }
}
