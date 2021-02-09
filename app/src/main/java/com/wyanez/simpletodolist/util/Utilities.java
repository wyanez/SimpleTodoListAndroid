package com.wyanez.simpletodolist.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilities {

    public static String CalendarToStringYmd(Calendar calendar){
       return String.format("%4d-%02d-%02d",
               calendar.get(Calendar.YEAR),
               calendar.get(Calendar.MONTH)+1,
               calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static Calendar getCalendarFromString(String strYmd){
        Log.d("getCalendarFromStr",strYmd);
        String pattern = strYmd.contains("-") ? "yyyy-MM-dd" : "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(strYmd);
            calendar.setTime(date);
        } catch (ParseException e) {
            Log.d("getCalendarFromStr", e.getMessage());

        }
        return calendar;
    }

    public static boolean CalendarEquals(Calendar cal1, Calendar cal2){
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
               cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

}
