package com.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type { INCOME, EXPENSE }

    private String id;
    private String title;
    private LocalDate date;
    private double amount;
    private Type type;
    private String accountId;

    public Transaction() {}

    public Transaction(String id, String title, LocalDate date, double amount, Type type, String accountId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.accountId = accountId;
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
}
