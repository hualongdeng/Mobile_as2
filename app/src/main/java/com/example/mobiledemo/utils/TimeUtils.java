package com.example.mobiledemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public static long getCurrentLongTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static String dateToStamp(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    public static String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static long getLastDayTimestamp() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        Date beginOfDate = calendar1.getTime();
        return beginOfDate.getTime();
    }

    public static long getLastWeekTimestamp() {
        Calendar calendar = Calendar.getInstance();
        int min = calendar.getActualMinimum(Calendar.DAY_OF_WEEK); //Get Start benchmark of the Week
        int current = calendar.get(Calendar.DAY_OF_WEEK); //Get the number of days in the current week
        calendar.add(Calendar.DAY_OF_WEEK, min - current);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        Date beginOfDate = calendar.getTime();
        return beginOfDate.getTime();
    }

    public static long getLastMonthTimestamp() {
        Calendar calendar = Calendar.getInstance();
        int min = calendar.getActualMinimum(Calendar.DAY_OF_MONTH); //Get Start benchmark of the month
        int current = calendar.get(Calendar.DAY_OF_MONTH); //Get the number of days in the current month
        calendar.add(Calendar.DAY_OF_MONTH, min - current);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        Date beginOfDate = calendar.getTime();
        return beginOfDate.getTime();
    }

}
