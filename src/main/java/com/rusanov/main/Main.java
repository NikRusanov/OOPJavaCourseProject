package com.rusanov.main;

import com.rusanov.port.shedule.Port;
import com.rusanov.port.shedule.Schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        Schedule schedule = new Schedule(3,date);
        System.out.println(schedule);

        Port port = new Port(schedule,  1, 1, 1);

        var cranesTest = port.getCranes();
        System.out.println("end");
    }



}
