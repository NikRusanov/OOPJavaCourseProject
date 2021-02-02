package com.rusanov.port.schedule;

import java.util.Date;

public class ScheduleFactory {

    private final Schedule schedule;


    public ScheduleFactory(int shipCount, Date date) {
        schedule = new Schedule(shipCount, date);
    }


    public Schedule getScheduleCopy() {
        return new Schedule(schedule);
    }
}
