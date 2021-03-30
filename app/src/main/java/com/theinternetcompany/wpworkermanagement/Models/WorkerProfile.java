package com.theinternetcompany.wpworkermanagement.Models;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class WorkerProfile implements Serializable {

    private String name, cardNo, rate, baseRate, workType, id;




    private Integer totalWage;
    private Integer total;



    private Integer totalShifts;
    private Integer conveyance;
    private HashMap<String, String> Attendance;


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
        this.totalWage = 0;
        this.total = 0;
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


    public Integer getTotalShifts() {
        return totalShifts;
    }

    public void setTotalShifts(Integer totalShifts) {
        this.totalShifts = totalShifts;
    }

    public Integer getTotalWage() {
        return totalWage;
    }

    public void setTotalWage(Integer totalWage) {
        this.totalWage = totalWage;
    }

    public Integer calculateTotalShifts(){
        Integer tempShifts = 0;
        if( Attendance != null){
            for (String shift : Attendance.values()){
                tempShifts += Integer.parseInt(shift);
            }
        }
        setTotalShifts(tempShifts);
        return tempShifts;
    }

    public Integer calculateTotalWage(){
        Integer tempWage = 0;
        Integer shifts = this.totalShifts;
        tempWage += shifts * Integer.parseInt(rate);
        setTotalWage(tempWage);
        return tempWage;
    }

    public Integer calculateTotal(){
        Integer tempTotal = 0;
        tempTotal += this.totalWage + this.conveyance;
        setTotal(tempTotal);
        return tempTotal;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
