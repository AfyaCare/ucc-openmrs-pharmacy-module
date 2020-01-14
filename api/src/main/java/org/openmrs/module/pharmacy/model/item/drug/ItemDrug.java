package org.openmrs.module.pharmacy.model.item.drug;

import org.openmrs.module.pharmacy.model.item.Item;
import org.openmrs.module.pharmacy.model.item.drug.Drug;

public class ItemDrug extends Item {
    private Drug drug;

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }
}
