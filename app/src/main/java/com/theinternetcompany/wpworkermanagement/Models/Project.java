package com.theinternetcompany.wpworkermanagement.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Project implements Serializable {
    private String id,name,company,location,period,expenses;



    private Integer wage, conveyance, cash, cheque, total;
    private HashMap<String, WorkerProfile> workerList = new HashMap<>();

    public Project(){
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Project(String id,String name,String company,String location,String period, String expenses){

        this.id = id;
        this.name = name;
        this.company = company;
        this.location = location;
        this.period = period;
        this.expenses = expenses;
        this.workerList = workerList;
        this.wage = 0;
        this.conveyance = 0;
        this.cash = 0;
        this.cheque = 0;
        this.total = 0;


    }

    public Integer getWage() {
        return wage;
    }

    public void setWage(Integer wage) {
        this.wage = wage;
    }

    public Integer getConveyance() {
        return conveyance;
    }

    public void setConveyance(Integer conveyance) {
        this.conveyance = conveyance;
    }

    public Integer getCash() {
        return cash;
    }

    public void setCash(Integer cash) {
        this.cash = cash;
    }

    public Integer getCheque() {
        return cheque;
    }

    public void setCheque(Integer cheque) {
        this.cheque = cheque;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getExpenses() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

    public HashMap<String, WorkerProfile> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(HashMap<String, WorkerProfile> workerList) {
        this.workerList = workerList;
    }
    public Integer calculateTotal() {
        Integer total = this.wage+ this.conveyance+ this.cash + this.cheque;
        return total;
    }
    public Integer calculateWage() {
        Integer totalWage = 0;
        if (this.getWorkerList().size() != 0){
            for (WorkerProfile worker: this.getWorkerList().values()){
                if (worker.getAttendance() != null){
                    for (String shift : worker.getAttendance().values()){
                        totalWage += Integer.parseInt(shift) * Integer.parseInt(worker.getRate());
                    }
                }

            }
        }
        this.wage = totalWage;
        return totalWage;
    }

    public Integer calculateConveyance() {
        Integer conveyance = 0;
        if (this.getWorkerList().size() != 0){
            for (WorkerProfile worker: this.getWorkerList().values()){
                conveyance += worker.getConveyance();
            }
        }
        this.conveyance = conveyance;
        return conveyance;
    }
}
