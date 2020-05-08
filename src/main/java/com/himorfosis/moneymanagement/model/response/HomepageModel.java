package com.himorfosis.moneymanagement.model.response;

import com.himorfosis.moneymanagement.entity.FinancialEntity;
import com.himorfosis.moneymanagement.model.HomepageResponse;

import java.sql.Timestamp;
import java.util.List;

public class HomepageModel {

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
        private List<FinancialEntity> financePerDay;

        public Data(String date, long total_income, long total_spend, List<FinancialEntity> financePerDay) {
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

        public List<FinancialEntity> getFinancePerDay() {
            return financePerDay;
        }

        public void setFinancePerDay(List<FinancialEntity> financePerDay) {
            this.financePerDay = financePerDay;
        }
    }

}
