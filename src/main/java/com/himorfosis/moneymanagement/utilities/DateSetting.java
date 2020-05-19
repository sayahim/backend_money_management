package com.himorfosis.moneymanagement.utilities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateSetting {

//    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    public static Timestamp timestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        return timestamp;

    }

    public static Timestamp generateDateToTimestamp(String date) {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dataTime = formatter.parse(date);
            Timestamp timestamp = new Timestamp(dataTime.getTime());
            return timestamp;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String dateTimeGenerator() {

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatted = myDateObj.format(myFormatObj);
        return formatted;
    }

    public static String generateNameByDateTime() {

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String formatted = myDateObj.format(myFormatObj);
        return formatted;
    }

    public static java.sql.Date convertStringToDateSql(String date) {
        try {
            Date formatter = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            java.sql.Date response = new java.sql.Date(formatter.getTime());
            return response;
        } catch (Exception e) {
            Util.log("convertStringToDateSql", e.getMessage());
        }
        return null;
    }

}
