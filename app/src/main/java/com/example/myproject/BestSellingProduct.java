package com.example.myproject;

public class BestSellingProduct {
    private int productId;
    private int totalQuantity;

    public BestSellingProduct(int productId, int totalQuantity) {
        this.productId = productId;
        this.totalQuantity = totalQuantity;
    }

    public int getProductId() {
        return productId;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }
}
