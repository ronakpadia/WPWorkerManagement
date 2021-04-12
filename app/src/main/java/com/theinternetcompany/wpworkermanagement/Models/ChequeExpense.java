package com.theinternetcompany.wpworkermanagement.Models;

import java.io.Serializable;

public class ChequeExpense implements Serializable {
    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean paid;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getFinalAmnt() {
        return finalAmount;
    }

    public void setFinalAmnt(Integer finalAmnt) {
        this.finalAmount = finalAmnt;
    }

    private String name,id;
    private Integer amount, discount, finalAmount;

    public ChequeExpense(){
        this.paid = false;
    }

    public ChequeExpense(String name, String id, Integer amount, Integer discount){
        this.name = name;
        this.id = id;
        this.amount = amount;
        this.discount = discount;
        this.paid = false;
    }

    public Integer calculateFinalAmount(){
        this.finalAmount = this.amount - this.discount;
        return this.finalAmount;
    }

}
