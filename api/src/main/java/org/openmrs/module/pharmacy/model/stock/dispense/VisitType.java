package org.openmrs.module.pharmacy.model.stock.dispense;

public class VisitType {
    private int visitTypeId;
    private String name;

    public int getVisitTypeId() {
        return visitTypeId;
    }

    public void setVisitTypeId(int visitTypeId) {
        this.visitTypeId = visitTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
