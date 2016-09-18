package me.raptor.resellingapp.model;

import java.util.Date;

/**
 * Created by Lucas on 18/09/2016.
 */
public class Purchase {

    private Integer purchaseID;
    private Date date;

    public Purchase(Integer purchaseID, Date date) {
        this.purchaseID = purchaseID;
        this.date = date;
    }

    public Integer getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Integer purchaseID) {
        this.purchaseID = purchaseID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
