package com.theinternetcompany.wpworkermanagement.Models;

import java.io.Serializable;

public class CashExpense implements Serializable {
    private String particulars,partyName, id;

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getDebit() {
        return debit;
    }

    public void setDebit(Integer debit) {
        this.debit = debit;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    private Integer credit, debit, balance;

    public CashExpense(){}

    public CashExpense(String partyName, String particulars, Integer credit, Integer debit){
        this.partyName = partyName;
        this.particulars = particulars;
        this.credit = credit;
        this.debit = debit;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
