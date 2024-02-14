package com.example.myproject;

public class UserOrder {
    private int id;
    private int userId;
    private int proId;
    private int quantity;
    private int totalprice;
    private String feedback;
    private int rating;

    public UserOrder(int id, int userId, int proId, int quantity, int totalprice, String feedback, int rating) {
        this.id = id;
        this.userId = userId;
        this.proId = proId;
        this.quantity = quantity;
        this.totalprice = totalprice;
        this.feedback = feedback;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getProId() {
        return proId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public String getFeedback() {
        return feedback;
    }

    public int getRating() {
        return rating;
    }

}
