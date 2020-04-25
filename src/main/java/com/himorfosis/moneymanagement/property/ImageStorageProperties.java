package com.himorfosis.moneymanagement.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
public class ImageStorageProperties {

    private String uploadImage;
    private String uploadImageAssets;

    public String getUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(String uploadImage) {
        this.uploadImage = uploadImage;
    }

    public String getUploadImageAssets() {
        return uploadImageAssets;
    }

    public void setUploadImageAssets(String uploadImageAssets) {
        this.uploadImageAssets = uploadImageAssets;
    }
}
