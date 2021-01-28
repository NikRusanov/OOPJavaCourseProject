package com.rusanov.main;

import com.rusanov.port.shedule.Shedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        Shedule shedule = new Shedule(5,date);
        System.out.println(shedule);

    }



}
