package com.example.myproject;

public class ShoppingCartItem {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private int totalPrice;

    public int getId() {
        return id;
    }
    public int getuserId() {
        return userId;
    }

    public int getproductId() {
        return productId;
    }
    public int getquantity() {
        return quantity;
    }
    public int gettotalPrice() {
        return totalPrice;
    }

    public ShoppingCartItem(int id, int userId, int productId, int quantity, int totalPrice) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }


    @Override
    public String toString() {
        return "ShoppingCartItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
