package com.himorfosis.moneymanagement.entity;

public class CategoryData {

    private String name;
    private String image;
    private String type;

    public CategoryData(String name, String image, String type) {
        this.name = name;
        this.image = image;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
