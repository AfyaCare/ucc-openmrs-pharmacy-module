package org.openmrs.module.pharmacy.model.ledger;

import java.util.Date;

public class LedgerRawLine {
    private int entryId;
    private int ledgerTypeId;
    private int itemId;
    private int priceCategoryId;
    private String batchNo;
    private String invoiceNo;
    private double buyingPrice;
    private int quantity;
    private Date dateMoved;
    private Date expiryDate;
    private int dosingUnitsId;
    private String remarks;
    private int creator;
    private String uuid;


    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public int getLedgerTypeId() {
        return ledgerTypeId;
    }

    public void setLedgerTypeId(int ledgerTypeId) {
        this.ledgerTypeId = ledgerTypeId;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDateMoved() {
        return dateMoved;
    }

    public void setDateMoved(Date dateMoved) {
        this.dateMoved = dateMoved;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getDosingUnitsId() {
        return dosingUnitsId;
    }

    public void setDosingUnitsId(int dosingUnitsId) {
        this.dosingUnitsId = dosingUnitsId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
