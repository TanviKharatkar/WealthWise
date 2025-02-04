package entity;

import java.util.Date;

public class Expense {
    private int expenseId;
    private int userId;
    private double amount;
    private int categoryId;
    private Date expense_date;
    private String description;

    public Expense() { }

    public Expense(int expenseId, int userId, double amount, int categoryId, Date expense_date, String description) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.amount = amount;
        this.categoryId = categoryId;
        this.expense_date = expense_date;
        this.description = description;
    }

    // Getters and Setters
    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Date getDate() {
        return expense_date;
    }

    public void setDate(Date expense_date) {
        this.expense_date = expense_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
