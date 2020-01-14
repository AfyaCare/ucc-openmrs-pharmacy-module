package org.openmrs.module.pharmacy.model.item;

import org.openmrs.module.pharmacy.model.item.itemattribute.Attribute;

import java.util.List;

public class ItemNonDrug extends Item{
    List<Attribute> attributes;

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
