package com.models;

import java.io.Serializable;

public class Budget implements Serializable {
    private static final long serialVersionUID = 1L;

    private String category;
    private double limit;
    private String userId;

    public Budget() {}

    public Budget(String category, double limit) {
        this.category = category;
        this.limit = limit;
        this.userId = null; // For backward compatibility
    }
    
    public Budget(String category, double limit, String userId) {
        this.category = category;
        this.limit = limit;
        this.userId = userId;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getLimit() { return limit; }
    public void setLimit(double limit) { this.limit = limit; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
