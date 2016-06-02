package com.idroidms.railway.model;

/**
 * Created by ubuntu1 on 29/5/16.
 */
public class RingtoneList {

    String notificationTitle;
    String notificationUri;

    public RingtoneList(String notificationTitle, String notificationUri) {
        this.notificationTitle = notificationTitle;
        this.notificationUri = notificationUri;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationUri() {
        return notificationUri;
    }

    public void setNotificationUri(String notificationUri) {
        this.notificationUri = notificationUri;
    }
}
