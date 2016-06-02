package com.idroidms.railway.model;

/**
 * Created by ubuntu1 on 24/3/16.
 */
public class NavDrawerModel {

    String title;
    int icon;
    int id;


    public NavDrawerModel(String title, int icon, int id) {
        this.title = title;
        this.icon = icon;
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
