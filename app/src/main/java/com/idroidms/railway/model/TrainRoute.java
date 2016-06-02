package com.idroidms.railway.model;

import java.util.List;

/**
 * Created by Prabhat on 14/04/16.
 */
public class TrainRoute {

    String number;
    String name;
    List<Days> days;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Days> getDays() {
        return days;
    }

    public void setDays(List<Days> days) {
        this.days = days;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
