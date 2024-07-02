package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {

    private int transferId;
    private int transferStatusId;
    private int accountFrom;
    private int accountTo;
    private String fromUsername;
    private String toUsername;
    private BigDecimal amount;

    public Transfers(int transferId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount, String toUsername, String fromUsername) {
        this.transferId = transferId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {

        this.toUsername = toUsername;
}

}
