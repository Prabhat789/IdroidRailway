package com.pktworld.railway.model;

/**
 * Created by Prabhat on 05/05/16.
 */
public class ServiceResponse {

    String response;
    String Message;
    String UserId;
    String EmailId;
    String FirstName;
    String LastName;
    String CellNo;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCellNo() {
        return CellNo;
    }

    public void setCellNo(String cellNo) {
        CellNo = cellNo;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
