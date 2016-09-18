package me.raptor.resellingapp.model;

import java.util.Date;

/**
 * Created by Lucas on 18/09/2016.
 */
public class Sale {

    private Integer saleID;
    private Date date;
    private String group;

    public Sale(Integer saleID, Date date, String group) {
        this.saleID = saleID;
        this.date = date;
        this.group = group;
    }

    public Integer getSaleID() {
        return saleID;
    }

    public void setSaleID(Integer saleID) {
        this.saleID = saleID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
