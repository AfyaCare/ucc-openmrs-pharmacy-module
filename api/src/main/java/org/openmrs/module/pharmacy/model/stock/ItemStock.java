package org.openmrs.module.pharmacy.model.stock;

import org.openmrs.module.pharmacy.model.item.Item;
import org.openmrs.module.pharmacy.model.price.PriceCategory;
import org.openmrs.module.pharmacy.model.store.Store;

public class ItemStock {
    private Store store;
    private Item item;
    private PriceCategory priceCategoryId;
    private int quantity;
    private String uuid;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
