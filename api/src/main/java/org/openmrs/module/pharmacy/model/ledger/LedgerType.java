package org.openmrs.module.pharmacy.model.ledger;

public class LedgerType {
    private int ledgerTypeId;
    private String name;
    private String operation;
    private String uuid;
    private int retired;

    public int getLedgerTypeId() {
        return ledgerTypeId;
    }

    public void setLedgerTypeId(int ledgerTypeId) {
        this.ledgerTypeId = ledgerTypeId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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

    public void setRetired(int retired) {
        this.retired = retired;
    }
}
