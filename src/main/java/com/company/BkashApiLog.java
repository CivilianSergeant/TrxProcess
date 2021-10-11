package com.company;

public class BkashApiLog {
    private long ID;
    private String BusinessDate;
    private long OfficeID;

    public BkashApiLog(long id){
        this.ID = id;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getBusinessDate() {
        return BusinessDate;
    }

    public void setBusinessDate(String businessDate) {
        BusinessDate = businessDate;
    }

    public long getOfficeID() {
        return OfficeID;
    }

    public void setOfficeID(long officeID) {
        OfficeID = officeID;
    }
}
