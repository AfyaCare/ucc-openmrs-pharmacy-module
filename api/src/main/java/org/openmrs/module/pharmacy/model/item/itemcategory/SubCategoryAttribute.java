package org.openmrs.module.pharmacy.model.item.itemcategory;

import org.openmrs.module.pharmacy.model.item.itemattribute.AttributeType;

import java.util.List;

public class SubCategoryAttribute {
    private int subCategoryId;
    private String name;
    private List<AttributeType> attributes;
    private int retired;

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttributeType> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeType> attributes) {
        this.attributes = attributes;
    }

    public int getRetired() {
        return retired;
    }

    public void setRetired(int retired) {
        this.retired = retired;
    }
}
