package com.rusanov.port.shedule;

import com.rusanov.UtilsDate;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ShipSchedule  {

    private Ship arrivedShip;

    private Date exceptedArrival;
    private Date realArrival;

    private int plannedUnloadingDays;
    private int unloadingDelay;

    private Date unloadingStartDay;
    private Date plannedUnloadingEndDay;




    private Date realUnloadingEndDay;

    private boolean isUnloading;


    private Integer totalDaysPassedInQueue;
    private double penalty;

    public Date getRealUnloadingEndDay() {
        return realUnloadingEndDay;
    }

    public void setRealUnloadingEndDay(Date realUnloadingEndDay) {
        this.realUnloadingEndDay = realUnloadingEndDay;
    }

    public ShipSchedule(Ship ship,
                        Date exceptedArrival,
                        Date realArrival,
                        int plannedUnloadingDays,
                        int unloadingDelay) {
        this.arrivedShip = ship;
        this.exceptedArrival = exceptedArrival;
        this.realArrival = realArrival;
        this.plannedUnloadingDays = plannedUnloadingDays;
        this.unloadingDelay = unloadingDelay;

        this.plannedUnloadingEndDay = addDaysInDate(exceptedArrival, plannedUnloadingDays);
        this.isUnloading = false;
        this.totalDaysPassedInQueue = 0;
        this.penalty = 0;
    }

    private Date addDaysInDate(Date dateToAdd, int add) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToAdd);
        cal.add(Calendar.DATE, add );
        return cal.getTime();
    }

    private int calculateUnloadingDelay() {
        Random random = new Random();
        return random.nextInt(10);
    }

    public Ship getArrivedShip() {
        return arrivedShip;
    }

    public void setArrivedShip(Ship arrivedShip) {
        this.arrivedShip = arrivedShip;
    }

    public Date getExceptedArrival() {
        return exceptedArrival;
    }

    public void setExceptedArrival(Date exceptedArrival) {
        this.exceptedArrival = exceptedArrival;
    }

    public Date getRealArrival() {
        return realArrival;
    }

    public void setRealArrival(Date realArrival) {
        this.realArrival = realArrival;
    }


    public int getPlannedUnloadingDays() {
        return plannedUnloadingDays;
    }

    public void setPlannedUnloadingDays(int plannedUnloadingDays) {
        this.plannedUnloadingDays = plannedUnloadingDays;
    }

    public Date getUnloadingStartDay() {
        return unloadingStartDay;
    }

    public void setUnloadingStartDay(Date unloadingStartDay) {
        this.unloadingStartDay = unloadingStartDay;
    }



    public Date getPlannedUnloadingEndDay() {
        return plannedUnloadingEndDay;
    }

    public void setPlannedUnloadingEndDay(Date plannedUnloadingEndDay) {
        this.plannedUnloadingEndDay = plannedUnloadingEndDay;
    }

    public boolean isUnloading() {
        return isUnloading;
    }

    public void setUnloading(boolean unloading) {
        isUnloading = unloading;
    }

    public Integer getTotalDaysPassedInQueue() {
        return totalDaysPassedInQueue;
    }

    public void addTotalDaysInQueue() {
        totalDaysPassedInQueue+=1;
    }

    public void setTotalDaysPassedInQueue(Integer totalDaysPassedInQueue) {
        this.totalDaysPassedInQueue = totalDaysPassedInQueue;
    }

    public double getPenalty() {
        return penalty;
    }

    //TODO отнимаем оставшиеся дни до разгрузки
    // Возможно для этого завести отдельное поле (см. строку 14)

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public  void calculatePenalty(Date realDate) {
        long daysDelay = 0 ;
             daysDelay = ChronoUnit.DAYS.between(plannedUnloadingEndDay.toInstant(), realDate.toInstant());
             penalty = daysDelay * 1000;
    }


    @Override
    public String toString() {
        return "ShipShedule{" +
                "arrivedShip=" + arrivedShip +
                "\n exceptedArrival=" + exceptedArrival +
                "\n realArrival=" + realArrival +
                "\n plannedUnloadingDays=" + plannedUnloadingDays +
                "\n unloadingStartDay=" + unloadingStartDay +
                "\n plannedUnloadingEndDay=" + plannedUnloadingEndDay +
                "\n calculated unloading Date =" + getUnloadingDateWithWithDelay() +
                "\n real unloading Date =" + realUnloadingEndDay +
                "\n isUnloading=" + isUnloading +
                "\n totalDaysPassedInQueue=" + totalDaysPassedInQueue +
                "\n penalty=" + penalty +
                "\n delay=" + unloadingDelay;
    }


    public boolean isArrival (Date currentDate) {
        return  currentDate.after(realArrival) || (currentDate.compareTo(realArrival) >= 0);
    }

    public Date getUnloadingDateWithWithDelay() {
        return UtilsDate.addDays(plannedUnloadingEndDay, unloadingDelay);
    }
}
