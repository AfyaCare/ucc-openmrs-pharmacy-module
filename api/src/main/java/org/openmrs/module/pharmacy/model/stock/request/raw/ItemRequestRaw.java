package org.openmrs.module.pharmacy.model.stock.request.raw;

import java.util.Date;

public class ItemRequestRaw {
    private int requestId;
    private String requestGroupId;
    private int itemId;
    private int priceCategoryId;
    private int requestingStore;
    private int requestedStore;
    private Date requestDate;
    private int quantityRequested;
    private Date dateIssued;
    private int quantityIssued;
    private int statusCode;
    private String uuid;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getRequestGroupId() {
        return requestGroupId;
    }

    public void setRequestGroupId(String requestGroupId) {
        this.requestGroupId = requestGroupId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getPriceCategoryId() {
        return priceCategoryId;
    }

    public void setPriceCategoryId(int priceCategoryId) {
        this.priceCategoryId = priceCategoryId;
    }

    public int getRequestingStore() {
        return requestingStore;
    }

    public void setRequestingStore(int requestingStore) {
        this.requestingStore = requestingStore;
    }

    public int getRequestedStore() {
        return requestedStore;
    }

    public void setRequestedStore(int requestedStore) {
        this.requestedStore = requestedStore;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public int getQuantityIssued() {
        return quantityIssued;
    }

    public void setQuantityIssued(int quantityIssued) {
        this.quantityIssued = quantityIssued;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
