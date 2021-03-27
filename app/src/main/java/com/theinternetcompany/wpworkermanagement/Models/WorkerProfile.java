package com.theinternetcompany.wpworkermanagement.Models;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class WorkerProfile implements Serializable {

    private String name, cardNo, rate, baseRate, workType, id;
    private Integer totalWage;
    private Integer Total;



    private Integer totalShifts;
    private Integer conveyance;
    private HashMap<String, String> Attendance = new HashMap<>();


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
        this.conveyance = 0;
        this.totalShifts = 0;
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

    public void setConveyance(Integer conveyance) {
        this.conveyance = conveyance;
    }
    public Integer getConveyance() {
        return conveyance;
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


    public String getTotalConveyance() {
        return String.valueOf(conveyance);
    }

    public String getTotalWage() {
        totalWage = 0;
        for (String shift : Attendance.values()){
            totalWage += Integer.parseInt(shift)* Integer.parseInt(rate);
        }
        return String.valueOf(totalWage);
    }

    public String getTotal() {
        Total = 0;
        Total = conveyance + totalWage;
        return String.valueOf(Total);
    }

    public String getTotalShifts() {
        totalShifts = 0;
        for (String shift : Attendance.values()){
            totalShifts += Integer.parseInt(shift);
        }
        return String.valueOf(totalShifts);
    }

    public void setTotalShifts(Integer totalShifts) {
        this.totalShifts = totalShifts;
    }

}
