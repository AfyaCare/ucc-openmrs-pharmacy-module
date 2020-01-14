package org.openmrs.module.pharmacy.model.item.drug;

public class DosageForm {
    private String name;
    private int conceptId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConceptId() {
        return conceptId;
    }

    public void setConceptId(int conceptId) {
        this.conceptId = conceptId;
    }
}
