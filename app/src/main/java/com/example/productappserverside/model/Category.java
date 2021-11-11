package com.example.productappserverside.model;

public class Category {

    private String name;
    private String ImageUrl;
    private String categoryId;


    public Category(String name, String imageUrl) {
        this.name = name;
        this.ImageUrl = imageUrl;

    }
    public Category(){}

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
