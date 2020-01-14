package org.openmrs.module.pharmacy.model.stock.dispense.raw;

import java.util.Date;

public class PrescriptionOrderRaw {
    private int prescriptionOrderId;
    private int categoryId;
    private int patientId;
    private int visitId;
    private int itemId;
    private int paymentCategoryId;
    private int targetDispensingPointId;
    private int dispensed;
    private int dispenser;
    private int pointDispensed;
    private Date dispensedDate;
    private Date dateCreated;
    private String uuid;

    public int getPrescriptionOrderId() {
        return prescriptionOrderId;
    }

    public void setPrescriptionOrderId(int prescriptionOrderId) {
        this.prescriptionOrderId = prescriptionOrderId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getPaymentCategoryId() {
        return paymentCategoryId;
    }

    public void setPaymentCategoryId(int paymentCategoryId) {
        this.paymentCategoryId = paymentCategoryId;
    }

    public int getTargetDispensingPointId() {
        return targetDispensingPointId;
    }

    public void setTargetDispensingPointId(int targetDispensingPointId) {
        this.targetDispensingPointId = targetDispensingPointId;
    }

    public int getDispensed() {
        return dispensed;
    }

    public void setDispensed(int dispensed) {
        this.dispensed = dispensed;
    }

    public int getDispenser() {
        return dispenser;
    }

    public void setDispenser(int dispenser) {
        this.dispenser = dispenser;
    }

    public int getPointDispensed() {
        return pointDispensed;
    }

    public void setPointDispensed(int pointDispensed) {
        this.pointDispensed = pointDispensed;
    }

    public Date getDispensedDate() {
        return dispensedDate;
    }

    public void setDispensedDate(Date dispensedDate) {
        this.dispensedDate = dispensedDate;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
