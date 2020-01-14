package org.openmrs.module.pharmacy.model.misc;

import org.openmrs.module.pharmacy.model.store.Store;

public class StockPoint {
    private int id;
    private String name;
    private Store store;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
