package org.openmrs.module.pharmacy.model.stock.dispense;

import org.openmrs.module.pharmacy.model.price.PriceCategory;
import java.util.Date;

public class DispenseMap {
    private int mapId;
    private VisitType visitType;
    private PriceCategory priceCategory;
    private DispensingPoint dispensingPoint;
    private int retired;
    private Date dateCreated;
    private String uuid;

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public VisitType getVisitType() {
        return visitType;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }

    public PriceCategory getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(PriceCategory priceCategory) {
        this.priceCategory = priceCategory;
    }

    public DispensingPoint getDispensingPoint() {
        return dispensingPoint;
    }

    public void setDispensingPoint(DispensingPoint dispensingPoint) {
        this.dispensingPoint = dispensingPoint;
    }

    public int getRetired() {
        return retired;
    }

    public void setRetired(int retired) {
        this.retired = retired;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
