package com.himorfosis.moneymanagement.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "assets")
public class AssetsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long id_assets_category;
    private String image_assets;
    private String image_assets_url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_assets_category() {
        return id_assets_category;
    }

    public void setId_assets_category(Long id_assets_category) {
        this.id_assets_category = id_assets_category;
    }

    public String getImage_assets() {
        return image_assets;
    }

    public void setImage_assets(String image_assets) {
        this.image_assets = image_assets;
    }

    public String getImage_assets_url() {
        return image_assets_url;
    }

    public void setImage_assets_url(String image_assets_url) {
        this.image_assets_url = image_assets_url;
    }


}
