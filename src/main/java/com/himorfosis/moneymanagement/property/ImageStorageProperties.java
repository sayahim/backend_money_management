package com.himorfosis.moneymanagement.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
public class ImageStorageProperties {

//    private String imageDir;
//
//    public String getImageDir() {
//        return imageDir;
//    }
//
//    public void setImageDir(String imageDir) {
//        this.imageDir = imageDir;
//    }

    private String uploadImage;

    public String getUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(String uploadImage) {
        this.uploadImage = uploadImage;
    }
}
