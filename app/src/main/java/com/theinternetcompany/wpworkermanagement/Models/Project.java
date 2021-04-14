package com.theinternetcompany.wpworkermanagement.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Project implements Serializable {
    private String id,name,company,location,period,expenses;



    private Integer wage, conveyance, cash, cheque, total, balance;
    private HashMap<String, WorkerProfile> workerList = new HashMap<>();
    private HashMap<String, CashExpense> cashExpenseList = new HashMap<>();



    private HashMap<String, ChequeExpense> chequeExpenseList = new HashMap<>();

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
        this.balance = 0;


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

    public HashMap<String, ChequeExpense> getChequeExpenseList() {
        return chequeExpenseList;
    }

    public void setChequeExpenseList(HashMap<String, ChequeExpense> chequeExpenseList) {
        this.chequeExpenseList = chequeExpenseList;
    }

    public HashMap<String, WorkerProfile> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(HashMap<String, WorkerProfile> workerList) {
        this.workerList = workerList;
    }
    public Integer calculateTotal() {
        Integer total = calculateWage()+ calculateConveyance()+ calculateCashExpense() + calculateChequeExpense();
        this.total = total;
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

    public Integer calculateCashExpense() {
        Integer totalCashExpense = 0;
        if (this.getCashExpenseList().size() != 0){
            for (CashExpense cashExpense: this.getCashExpenseList().values()){
                if( cashExpense.getDebit() != null){
                    totalCashExpense += cashExpense.getDebit();
                }
//                else if (cashExpense.getCredit() != null){
//                    totalCashExpense += cashExpense.getCredit();
//                }

            }
        }
        this.cash = totalCashExpense;
        return totalCashExpense;
    }

    public Integer calculateBalance() {
        Integer totalBalance = 0;
        if (this.getCashExpenseList().size() != 0){
            for (CashExpense cashExpense: this.getCashExpenseList().values()){
                if (cashExpense.getCredit() != null){
                    totalBalance += cashExpense.getCredit();
                }

            }
        }
        this.balance = totalBalance - calculateTotal();
        return this.balance;
    }

    public Integer calculateChequeExpense(){
        Integer totalChequeExpense = 0;
        if (this.getChequeExpenseList().size() != 0){
            for (ChequeExpense chequeExpense: this.getChequeExpenseList().values()){
                totalChequeExpense += chequeExpense.calculateFinalAmount();
            }
        }
        this.cheque = totalChequeExpense;
        return totalChequeExpense;
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

    public HashMap<String, CashExpense> getCashExpenseList() {
        return cashExpenseList;
    }

    public void setCashExpenseList(HashMap<String, CashExpense> cashExpenseList) {
        this.cashExpenseList = cashExpenseList;
    }
}
