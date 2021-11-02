package com.example.fresher.core;

import java.io.Serializable;

public class ExpensesModel implements Serializable {

    private int expenseId;

    private String expense_Id;

    private String title;

    private String date;

    private float amount;

    private String paidVia;

    public ExpensesModel() {
    }

    public ExpensesModel(int expenseId, String expense_Id, String title, String date, float amount, String paidVia) {
        this.expenseId = expenseId;
        this.expense_Id = expense_Id;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.paidVia = paidVia;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getPaidVia() {
        return paidVia;
    }

    public void setPaidVia(String paidVia) {
        this.paidVia = paidVia;
    }

    public String getExpense_Id() {
        return expense_Id;
    }

    public void setExpense_Id(String expense_Id) {
        this.expense_Id = expense_Id;
    }

}