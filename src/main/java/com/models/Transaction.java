package com.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type { INCOME, EXPENSE, TRANSFER }

    private String id;
    private String title;
    private LocalDate date;
    private double amount;
    private Type type;
    private String accountId;
    private String userId;
    private boolean isTransfer;
    private String toAccountId; // For transfers - the destination account

    public Transaction() {}

    public Transaction(String id, String title, LocalDate date, double amount, Type type, String accountId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.accountId = accountId;
        this.userId = null; // For backward compatibility
    }
    
    public Transaction(String id, String title, LocalDate date, double amount, Type type, String accountId, String userId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.accountId = accountId;
        this.userId = userId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public boolean isTransfer() { return isTransfer; }
    public void setTransfer(boolean transfer) { this.isTransfer = transfer; }
    
    public String getToAccountId() { return toAccountId; }
    public void setToAccountId(String toAccountId) { this.toAccountId = toAccountId; }
}
