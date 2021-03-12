package com.theinternetcompany.wpworkermanagement.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Project implements Serializable {
    private String id,name,company,location,period,expenses;
    private List<WorkerProfile> workerList = new ArrayList();

    public Project(){
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Project(String id,String name,String company,String location,String period, String expenses, ArrayList<WorkerProfile> workerList){

        this.id = id;
        this.name = name;
        this.company = company;
        this.location = location;
        this.period = period;
        this.expenses = expenses;
        this.workerList = workerList;


    }

    public Project(String projectID, String toString, String toString1, String toString2, String toString3, String zero) {
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
}
