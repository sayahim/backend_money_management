package com.himorfosis.moneymanagement.model.response;

import com.himorfosis.moneymanagement.entity.AssetsEntity;

import java.util.List;

public class AssetsResponse {

    private String id;
    private String title;
    private List<AssetsEntity> assets;
    public AssetsResponse(String id, String title, List<AssetsEntity> assets) {
        this.id = id;
        this.title = title;
        this.assets = assets;
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

    public List<AssetsEntity> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetsEntity> assets) {
        this.assets = assets;
    }

}
