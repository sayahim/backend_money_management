package com.himorfosis.moneymanagement.model.response;

import java.sql.Timestamp;

public class CategoryModel {

    private String id;
    private String title;
    private String description;
    private String type_category;
    private String image_category;
    private String image_category_url;
    private Timestamp created_at;
    private Timestamp updated_at;

    public CategoryModel (
            String id, String title, String description, String type_category, String image_category,
            String image_category_url, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type_category = type_category;
        this.image_category = image_category;
        this.image_category_url = image_category_url;
        this.created_at = created_at;
        this.updated_at =updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_category() {
        return type_category;
    }

    public void setType_category(String type_category) {
        this.type_category = type_category;
    }

    public String getImage_category() {
        return image_category;
    }

    public void setImage_category(String image_category) {
        this.image_category = image_category;
    }

    public String getImage_category_url() {
        return image_category_url;
    }

    public void setImage_category_url(String image_category_url) {
        this.image_category_url = image_category_url;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }


}
