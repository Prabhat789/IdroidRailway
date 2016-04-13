package com.pktworld.railway.model;

import java.util.List;

/**
 * Created by Prabhat on 14/04/16.
 */
public class TrainRouteResponse {

    String response_code;
    List<Route> route;
    TrainRoute train;

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public List<Route> getRoute() {
        return route;
    }

    public void setRoute(List<Route> route) {
        this.route = route;
    }

    public TrainRoute getTrain() {
        return train;
    }

    public void setTrain(TrainRoute train) {
        this.train = train;
    }
}
