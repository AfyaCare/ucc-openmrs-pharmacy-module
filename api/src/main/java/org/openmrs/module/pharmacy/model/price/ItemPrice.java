package org.openmrs.module.pharmacy.model.price;

public class ItemPrice {
    private int itemPriceId;
    private String itemName;
    private PriceCategory priceCategory;
    private double sellingPrice;
    private String lastlyChanged;
    private String changedBy;

    public int getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(int itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public PriceCategory getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(PriceCategory priceCategory) {
        this.priceCategory = priceCategory;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getLastlyChanged() {
        return lastlyChanged;
    }

    public void setLastlyChanged(String lastlyChanged) {
        this.lastlyChanged = lastlyChanged;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }
}
