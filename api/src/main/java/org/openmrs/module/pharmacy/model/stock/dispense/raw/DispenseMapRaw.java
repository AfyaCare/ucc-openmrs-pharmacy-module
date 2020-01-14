package org.openmrs.module.pharmacy.model.stock.dispense.raw;

import java.util.Date;

public class DispenseMapRaw {
    private int mapId;
    private int visitTypeId;
    private int paymentCategoryId;
    private int dispensingLocationId;
    private int retired;
    private Date dateCreated;
    private String uuid;

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getVisitTypeId() {
        return visitTypeId;
    }

    public void setVisitTypeId(int visitTypeId) {
        this.visitTypeId = visitTypeId;
    }

    public int getPaymentCategoryId() {
        return paymentCategoryId;
    }

    public void setPaymentCategoryId(int paymentCategoryId) {
        this.paymentCategoryId = paymentCategoryId;
    }

    public int getDispensingLocationId() {
        return dispensingLocationId;
    }

    public void setDispensingLocationId(int dispensingLocationId) {
        this.dispensingLocationId = dispensingLocationId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getRetired() {
        return retired;
    }

    public void setRetired(int retired) {
        this.retired = retired;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
