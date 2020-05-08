package com.himorfosis.moneymanagement.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
public class ImageStorageProperties {

    private String uploadImage;
    private String uploadImageAssets;
    private String uploadImageProfile;

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

    public String getUploadImageProfile() {
        return uploadImageProfile;
    }

    public void setUploadImageProfile(String uploadImageProfile) {
        this.uploadImageProfile = uploadImageProfile;
    }
}
