package com.rusanov;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public  class UtilsDate {

    public static Date addDays(Date currentDate, int add) {
        GregorianCalendar calendar= new GregorianCalendar();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, add);
        return calendar.getTime();
    }


    public static long daysBetween(Date first, Date second) {

        return ChronoUnit.DAYS.between(first.toInstant(), second.toInstant());
    }
}
