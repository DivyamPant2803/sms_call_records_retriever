package com.example.smsreciever;

import java.util.Date;

public class CallLogs {
    String phoneNo, callType, callDuration;
    Date callDayTime;
    public CallLogs(){

    }

    public CallLogs(String phoneNo, String callType, Date callDayTime, String callDuration) {
        this.phoneNo = phoneNo;
        this.callType = callType;
        this.callDayTime = callDayTime;
        this.callDuration = callDuration;
    }

    public Date getCallDayTime() {
        return callDayTime;
    }

    public void setCallDayTime(Date callDayTime) {
        this.callDayTime = callDayTime;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }
}
