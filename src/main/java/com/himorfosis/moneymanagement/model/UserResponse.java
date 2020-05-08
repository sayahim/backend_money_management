package com.himorfosis.moneymanagement.model;

import java.sql.Timestamp;
import java.util.Date;

public class UserResponse {

    private String id;
    private String name;
    private String email;
    private String phone_number;
    private Date born;
    private String gender;
    private String image;
    private String image_url;
    private String token;
    private Integer active;
    private Timestamp created_at;
    private Timestamp updated_at;

    public UserResponse(String id, String name, String email, String phone_number,
                        Date born, String gender, String image_url,
                        String image, String token, Integer active,
                        Timestamp created_at, Timestamp updated_at
                        ) {
        this.id = id ;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.born = born;
        this.gender = gender;
        this.image = image;
        this.image_url = image_url;
        this.token = token;
        this.active = active;
        this.created_at = created_at;
        this.updated_at = updated_at;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
