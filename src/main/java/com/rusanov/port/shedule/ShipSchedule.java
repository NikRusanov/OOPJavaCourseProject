package com.rusanov.port.shedule;

import java.util.Calendar;
import java.util.Date;

public class ShipSchedule {

    private Ship arrivedShip;

    private Date exceptedArrival;
    private Date realArrival;

    private int plannedUnloadingDays;

   private Date unloadingStartDay;
   //Возможно убрать, потому что можно высчитать с unloadingStartDay + plannedUnloadingDays
   private Date unloadingEndDay;
   private boolean isUnloading;
   // realArrival до начала загразки
   private Integer totalDaysPassedInQueue;
   private double penalty;

    public ShipSchedule(Ship ship,
                        Date exceptedArrival,
                        Date realArrival,
                        int plannedUnloadingDays) {
        this.arrivedShip = ship; 
        this.exceptedArrival = exceptedArrival;
        this.realArrival = realArrival;
        this.plannedUnloadingDays = plannedUnloadingDays;
        this.unloadingStartDay = realArrival;

        this.unloadingEndDay = getEndDate(exceptedArrival, plannedUnloadingDays);
        this.isUnloading = false;
        this.totalDaysPassedInQueue = 0;
        this.penalty = 0;
    }

    private Date getEndDate(Date StartDate, int countOfDates) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(realArrival);
        cal.add(Calendar.DATE, plannedUnloadingDays );
        return cal.getTime();
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

    public Date getUnloadingEndDay() {
        return unloadingEndDay;
    }

    public void setUnloadingEndDay(Date unloadingEndDay) {
        this.unloadingEndDay = unloadingEndDay;
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

    public void setTotalDaysPassedInQueue(Integer totalDaysPassedInQueue) {
        this.totalDaysPassedInQueue = totalDaysPassedInQueue;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }


    @Override
    public String toString() {
        return "ShipShedule{" +
                "arrivedShip=" + arrivedShip +
                "\n exceptedArrival=" + exceptedArrival +
                "\n realArrival=" + realArrival +
                "\n plannedUnloadingDays=" + plannedUnloadingDays +
                "\n unloadingStartDay=" + unloadingStartDay +
                "\n unloadingEndDay=" + unloadingEndDay +
                "\n isUnloading=" + isUnloading +
                "\n totalDaysPassedInQueue=" + totalDaysPassedInQueue +
                "\n penalty=" + penalty +
                '\n';
    }
}
