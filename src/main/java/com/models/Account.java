package com.models;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private double balance;
    private String userId; // Associate account with a specific user

    public Account() {}

    public Account(String id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.userId = null; // For backward compatibility
    }
    
    public Account(String id, String name, double balance, String userId) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.userId = userId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
