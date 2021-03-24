package com.theinternetcompany.wpworkermanagement.Models;

import java.io.Serializable;
import java.util.HashMap;

public class WorkerProfile implements Serializable {

    private String name, cardNo, rate, baseRate, workType, id;
    private HashMap<String, String> Attendance;
    private HashMap<String, String> Conveyance;


    public WorkerProfile(){
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public WorkerProfile(String id, String name, String cardNo, String rate, String baseRate, String workType) {
        this.id = id;
        this.name = name;
        this.cardNo = cardNo;
        this.rate = rate;
        this.baseRate = baseRate;
        this.workType = workType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(String baseRate){
        this.baseRate = baseRate;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString()
    {
        return this.getName();
    }

    public HashMap<String, String> getAttendance() {
        return Attendance;
    }

    public void setAttendance(HashMap<String, String> attendance) {
        this.Attendance = attendance;
    }

    public HashMap<String,String> getConveyance() {
        return Conveyance;
    }

    public void setConveyance(HashMap<String,String> conveyance) {
        this.Conveyance = conveyance;
    }
}
