package org.openmrs.module.pharmacy.model.stock.request;

import org.openmrs.module.pharmacy.model.item.Item;
import org.openmrs.module.pharmacy.model.price.PriceCategory;
import org.openmrs.module.pharmacy.model.store.Store;

import java.util.Date;

public class ItemRequest {
    private int requestId;
    private String requestGroupId;
    private Item item;
    private PriceCategory priceCategory;
    private Store requestingStore;
    private Store requestedStore;
    private int quantityRequested;
    private Date requestDate;
    private int quantityIssued;
    private Date dateIssued;
    private RequestStatusCode status;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public PriceCategory getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(PriceCategory priceCategory) {
        this.priceCategory = priceCategory;
    }

    public Store getRequestingStore() {
        return requestingStore;
    }

    public void setRequestingStore(Store requestingStore) {
        this.requestingStore = requestingStore;
    }

    public Store getRequestedStore() {
        return requestedStore;
    }

    public void setRequestedStore(Store requestedStore) {
        this.requestedStore = requestedStore;
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

    public RequestStatusCode getStatus() {
        return status;
    }

    public void setStatus(RequestStatusCode status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
