package com.himorfosis.moneymanagement.model.response;

import com.himorfosis.moneymanagement.repository.ReportsRepository;

import java.sql.Timestamp;
import java.util.List;

public class ReportDetailResponse {

    private long totalNominalReport;
    private List<ReportDay> reportDay;
    private List<Data> data;

    public ReportDetailResponse(long totalNominalReport,List<ReportDay>  reportDay, List<Data>  data ) {
        this.totalNominalReport = totalNominalReport;
        this.reportDay = reportDay;
        this.data = data;
    }

    public long getTotalNominalReport() {
        return totalNominalReport;
    }

    public void setTotalNominalReport(long totalNominalReport) {
        this.totalNominalReport = totalNominalReport;
    }

    public List<ReportDay> getReportDay() {
        return reportDay;
    }

    public void setReportDay(List<ReportDay> reportDay) {
        this.reportDay = reportDay;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class ReportDay {
        private int day;
        private long total;
        private long percent;

        public ReportDay(int day, long total, long percent) {
            this.day = day;
            this.total = total;
            this.percent = percent;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public long getPercent() {
            return percent;
        }

        public void setPercent(long percent) {
            this.percent = percent;
        }
    }

    public static class Data {
        private String id;
        private String id_category;
        private String type_finance;
        private Long nominal;
        private String note;
        private Timestamp date;
        private String title;
        private String image_category;
        private String image_category_url;

        public Data(
                String id, String id_category, String type_finance, Long nominal,
                String note, Timestamp date, String title, String image_category,
                String image_category_url) {
            this.id = id;
            this.id_category = id_category;
            this.type_finance = type_finance;
            this.nominal = nominal;
            this.note = note;
            this.date = date;
            this.title = title;
            this.image_category = image_category;
            this.image_category_url = image_category_url;
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

        public String getType_finance() {
            return type_finance;
        }

        public void setType_finance(String type_finance) {
            this.type_finance = type_finance;
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

        public Timestamp getDate() {
            return date;
        }

        public void setDate(Timestamp date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
    }

}
