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

//        //method 2 - via Date
//        Date date = new Date();
//        System.out.println(new Timestamp(date.getTime()));
//
//        //return number of milliseconds since January 1, 1970, 00:00:00 GMT
//        System.out.println(timestamp.getTime());

        //format timestamp
        System.out.println(sdf.format(timestamp));

        return timestamp;

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
