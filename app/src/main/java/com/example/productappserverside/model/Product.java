package com.example.productappserverside.model;

public class Product {
    private String name;
    private String price;
    private String image;
    private String discount;
    private String categoryId;
    private String description;


    public Product(String name, String price, String image, String discount, String categoryId, String description){
        this.name = name;
        this.price = price;
        this.image = image;
        this.discount = discount;
        this.categoryId = categoryId;
        this.description = description;
    }
    public Product(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
