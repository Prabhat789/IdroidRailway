package com.pktworld.railway.model;

import java.util.List;

/**
 * Created by Prabhat on 16/04/16.
 */
public class CancelTrainResponse {
    public String response_code;
    List<Trains> trains;

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public List<Trains> getTrains() {
        return trains;
    }

    public void setTrains(List<Trains> trains) {
        this.trains = trains;
    }
}
