package com.himorfosis.moneymanagement.model;

public class ReportCategoryResponse {

    private String id;
    private String title;
    private Long total_nominal;
    private Long total_percentage;
    private String image_category_url;

    public ReportCategoryResponse(
            String id, String title, Long total_nominal,
            Long total_percentage, String image_category_url) {

        this.id = id;
        this.title = title;
        this.total_nominal = total_nominal;
        this.total_percentage = total_percentage;
        this.image_category_url = image_category_url;

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

    public long getTotal_nominal() {
        return total_nominal;
    }

    public void setTotal_nominal(Long total_nominal) {
        this.total_nominal = total_nominal;
    }

    public Long getTotal_percentage() {
        return total_percentage;
    }

    public void setTotal_percentage(Long total_percentage) {
        this.total_percentage = total_percentage;
    }

    public String getImage_category_url() {
        return image_category_url;
    }

    public void setImage_category_url(String image_category_url) {
        this.image_category_url = image_category_url;
    }



}
