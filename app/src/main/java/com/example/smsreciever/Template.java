package com.example.smsreciever;


public class Template {
    private String message, phoneNo ;

    public Template(){

    }

    public Template(String message, String phoneNo) {
        this.message = message;
        this.phoneNo = phoneNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
