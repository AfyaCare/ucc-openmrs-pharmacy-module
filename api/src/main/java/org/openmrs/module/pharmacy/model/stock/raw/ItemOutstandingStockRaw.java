package org.openmrs.module.pharmacy.model.stock.raw;

import java.util.Date;

public class ItemOutstandingStockRaw {
    private int outstandingStockId;
    private int storeId;
    private int itemId;
    private String batchNo;
    private int priceCategoryId;
    private int quantity;
    private int requestId;
    private int creator;
    private Date dateCreated;

    public int getOutstandingStockId() {
        return outstandingStockId;
    }

    public void setOutstandingStockId(int outstandingStockId) {
        this.outstandingStockId = outstandingStockId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public int getPriceCategoryId() {
        return priceCategoryId;
    }

    public void setPriceCategoryId(int priceCategoryId) {
        this.priceCategoryId = priceCategoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
