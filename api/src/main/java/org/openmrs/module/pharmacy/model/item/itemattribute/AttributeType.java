package org.openmrs.module.pharmacy.model.item.itemattribute;

public class AttributeType {
    private int attributeTypeId;
    private String name;
    private String uuid;
    private String subCategoryUuid;
    private int retired;

    public int getAttributeTypeId() {
        return attributeTypeId;
    }

    public void setAttributeTypeId(int attributeTypeId) {
        this.attributeTypeId = attributeTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public int getRetired() {
        return retired;
    }

    public String getSubCategoryUuid() {
        return subCategoryUuid;
    }

    public void setSubCategoryUuid(String subCategoryUuid) {
        this.subCategoryUuid = subCategoryUuid;
    }

    public void setRetired(int retired) {
        this.retired = retired;
    }


}
