package com.himorfosis.moneymanagement.model.response;

import com.himorfosis.moneymanagement.entity.AssetsEntity;

import java.util.List;

public class AssetsCategoryResponse {

    private String id;
    private String title;
    public AssetsCategoryResponse(String id, String title) {
        this.id = id;
        this.title = title;
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
}
