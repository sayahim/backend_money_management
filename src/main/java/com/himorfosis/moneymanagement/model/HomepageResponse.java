package com.himorfosis.moneymanagement.model;

import com.himorfosis.moneymanagement.entity.FinancialEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class HomepageResponse {

    private TotalFinanceUser totalFinanceUser;
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public TotalFinanceUser getTotalFinanceUser() {
        return totalFinanceUser;
    }

    public void setTotalFinanceUser(TotalFinanceUser totalFinanceUser) {
        this.totalFinanceUser = totalFinanceUser;
    }

    public static class TotalFinanceUser {

        private long total_income;
        private long total_spend;

        public TotalFinanceUser(long total_income, long total_spend) {
            this.total_income = total_income;
            this.total_spend = total_spend;
        }

        public long getTotal_income() {
            return total_income;
        }

        public void setTotal_income(long total_income) {
            this.total_income = total_income;
        }

        public long getTotal_spend() {
            return total_spend;
        }

        public void setTotal_spend(long total_spend) {
            this.total_spend = total_spend;
        }
    }

    public static class Data {

        private String date;
        private long income;
        private long spend;
        private List<Financial> financePerDay;

        public Data(String date, long total_income, long total_spend, List<Financial> financePerDay) {
            this.date = date;
            this.income = total_income;
            this.spend = total_spend;
            this.financePerDay = financePerDay;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public long getIncome() {
            return income;
        }

        public void setIncome(long income) {
            this.income = income;
        }

        public long getSpend() {
            return spend;
        }

        public void setSpend(long spend) {
            this.spend = spend;
        }

        public List<Financial> getFinancePerDay() {
            return financePerDay;
        }

        public void setFinancePerDay(List<Financial> financePerDay) {
            this.financePerDay = financePerDay;
        }
    }

    public static class Financial {
        private String id;
        private String id_category;
        private String code;
        private String type_financial;
        private Long nominal;
        private Timestamp date;
        private String note;
        private String title_category;
        private String image_category;
        private String image_category_url;
        private Timestamp created_at;
        private Timestamp updated_at;

        public Financial(
                 String id,
                 String id_category,
                 String code,
                 String type_financial,
                 Timestamp date,
                 Long nominal,
                 String note,
                 String title_category,
                 String image_category,
                 String image_category_url,
                 Timestamp created_at,
                 Timestamp updated_at
        ) {
            this.id = id;
            this.id_category = id_category;
            this.code = code;
            this.type_financial = type_financial;
            this.date = date;
            this.nominal = nominal;
            this.note = note;
            this.title_category = title_category;
            this.image_category = image_category;
            this.image_category_url = image_category_url;
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

        public String getTitleCategory() {
            return title_category;
        }

        public void setTitleCategory(String title_category) {
            this.title_category = title_category;
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

        public Timestamp getDate() {
            return date;
        }

        public void setDate(Timestamp date) {
            this.date = date;
        }
    }

}



