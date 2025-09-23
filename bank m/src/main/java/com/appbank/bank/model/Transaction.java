package com.appbank.bank.model;

import java.time.LocalDateTime;

public class Transaction {
    private String type;
    private Money amount;
    private String account;
    private LocalDateTime timestamp;

    
    public Transaction(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    
    public Transaction(String type, Money amount, String account) {
        this.type = type;
        this.amount = amount;
        this.account = account;
        this.timestamp = LocalDateTime.now(); 
    }

    // Getters y setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
