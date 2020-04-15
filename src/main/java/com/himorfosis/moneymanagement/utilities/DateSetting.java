package com.himorfosis.moneymanagement.utilities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateSetting {

//    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    public static Timestamp timestamp() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

        //method 1
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);

        //format timestamp
        System.out.println(sdf.format(timestamp));

        return timestamp;

    }

    public static Timestamp generateDateToTimestamp(String date) {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
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
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatted = myDateObj.format(myFormatObj);

        return formatted;
    }

    public static String generateNameByDateTime() {

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String formatted = myDateObj.format(myFormatObj);

        return formatted;
    }

    public static Date convertStringToDate(String date) {

        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
            return formatter.parse(date);

        } catch (Exception e) {

        }

        return null;

    }

}
