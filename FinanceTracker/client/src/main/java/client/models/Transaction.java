package client.models;

import java.time.LocalDate;

public class Transaction {
    private double amount;
    private String type; // INCOME or EXPENSE
    private String category;
    private LocalDate date;
    private String note;

    public Transaction(double amount, String type, String category, LocalDate date, String note) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    // Getters & Setters
    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}