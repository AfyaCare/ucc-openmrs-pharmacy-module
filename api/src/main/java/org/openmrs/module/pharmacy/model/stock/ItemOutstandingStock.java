package org.openmrs.module.pharmacy.model.stock;

import org.openmrs.module.pharmacy.model.item.Item;
import org.openmrs.module.pharmacy.model.price.PriceCategory;
import org.openmrs.module.pharmacy.model.store.Store;

import java.util.Date;

public class ItemOutstandingStock {
    private int outstandingStockId;
    private Store storeId;
    private Item itemId;
    private String batchNo;
    private PriceCategory priceCategoryId;
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

    public Store getStoreId() {
        return storeId;
    }

    public void setStoreId(Store storeId) {
        this.storeId = storeId;
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public PriceCategory getPriceCategoryId() {
        return priceCategoryId;
    }

    public void setPriceCategoryId(PriceCategory priceCategoryId) {
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
