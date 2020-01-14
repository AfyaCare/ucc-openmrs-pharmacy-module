package org.openmrs.module.pharmacy.model.stock.dispense;

public class DispensingPoint {
    private int dispensingPointId;
    private String name;

    public int getDispensingPointId() {
        return dispensingPointId;
    }

    public void setDispensingPointId(int dispensingPointId) {
        this.dispensingPointId = dispensingPointId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
