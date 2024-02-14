package com.example.myproject;

public class Product {
    private int id;
    private String name;
    private String description;
    private byte[] image;
    private String barcode;
    private int category;
    private int price;
    private String quantity;

    public Product(int id, String name, String description, byte[] image,String barcode, int category, int price, String quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.barcode = barcode;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }
    public String getbarcodeImage() {
        return barcode;
    }

    public int getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }
}