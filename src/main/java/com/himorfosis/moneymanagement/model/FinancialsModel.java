package com.himorfosis.moneymanagement.model;

import java.sql.Timestamp;

public class FinancialsModel {

    private String id;
    private String id_category;
    private String id_user;
    private String code;
    private String type_financial;
    private Long nominal;
    private String note;
    private Timestamp created_at;
    private Timestamp updated_at;

    public FinancialsModel(String id, String id_category, String id_user, String code,
        String type_financial, Long nominal, String note, Timestamp created_at, Timestamp updated_at ) {

        this.id = id;
        this.id_category = id_category;
        this.id_user = id_user;
        this.code = code;
        this.type_financial = type_financial;
        this.nominal = nominal;
        this.note = note;
        this.created_at = created_at;
        this.updated_at = updated_at;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType_financial() {
        return type_financial;
    }

    public void setType_financial(String type_financial) {
        this.type_financial = type_financial;
    }

    public Long getNominal() {
        return nominal;
    }

    public void setNominal(Long nominal) {
        this.nominal = nominal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
