package com.example.myproject;

public class Category {
    private int id;
    private String name;
    private byte[] image;

    public Category(int id, String name,byte[] image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getImage() {
        return image;
    }

    @Override
    public String toString() {
        return name;
    }

}
