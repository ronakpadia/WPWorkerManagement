package com.theinternetcompany.wpworkermanagement.Models;

public class WorkerProfile {

    private String name, cardNo, rate, workType, id;

    public WorkerProfile(String id, String name, String cardNo,String rate, String workType) {
        this.id = id;
        this.name = name;
        this.cardNo = cardNo;
        this.rate = rate;
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
}
