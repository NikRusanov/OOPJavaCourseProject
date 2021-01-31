package com.rusanov;

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
}
