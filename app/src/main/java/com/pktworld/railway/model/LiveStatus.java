package com.pktworld.railway.model;

import java.util.List;

/**
 * Created by Prabhat on 26/03/16.
 */
public class LiveStatus {

    String response_code;
    String error;
    String train_number;
    String position;

    List<RouteList> route;

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

    public String getTrain_number() {
        return train_number;
    }

    public void setTrain_number(String train_number) {
        this.train_number = train_number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<RouteList> getRoute() {
        return route;
    }

    public void setRoute(List<RouteList> route) {
        this.route = route;
    }
}
