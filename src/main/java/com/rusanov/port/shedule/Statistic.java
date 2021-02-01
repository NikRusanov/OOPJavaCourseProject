package com.rusanov.port.shedule;

import com.rusanov.UtilsDate;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;

public class Statistic {

    List<ShipSchedule> scheduleDays;

    private long unloadedShips;
    private IntSummaryStatistics statisticsQueue;
    private LongSummaryStatistics statisticsDelays;



    private long totalPenalty;


    private long totalBulkPenalty;
    private long totalContainerPenalty;
    private long totalLiquidPenalty;

    public Statistic(List<ShipSchedule> schedules) {
        this.scheduleDays = schedules;
        setAllPenaltiesByTypes();
        setTotalPenalties();
        calculateQueueStatistic();
        calculateDelayStatistic();
        calculateUnloadingShips();
    }

    private void calculateUnloadingShips() {
        unloadedShips = scheduleDays
                .stream()
                .filter(scheduleDay ->
                    (scheduleDay.getUnloadingStartDay() != null) && (scheduleDay.getRealUnloadingEndDay() != null)
                ).count();
    }


    private void setAllPenaltiesByTypes() {
        totalBulkPenalty = calculatePenaltiesByType(CargoType.BULK);
        totalContainerPenalty = calculatePenaltiesByType(CargoType.CONTAINER) +
                calculatePenaltiesByType(CargoType.CONTAINER);
        totalLiquidPenalty = calculatePenaltiesByType(CargoType.LIQUID) +
                calculatePenaltiesByType(CargoType.LIQUID);
    }

    public long calculatePenaltiesByType(CargoType type) {
        return scheduleDays
                .stream()
                .filter(shipSchedule -> (shipSchedule.getArrivedShip().getType() == type))
                .mapToLong(shipSchedule -> (long) shipSchedule.getPenalty()).sum();
    }

    private void setTotalPenalties() {
        totalPenalty = totalBulkPenalty + totalContainerPenalty + totalLiquidPenalty;
    }





    private void calculateQueueStatistic() {
        statisticsQueue = scheduleDays
                .stream()
                .map(ShipSchedule::getTotalDaysPassedInQueue)
                .mapToInt(Integer::intValue)
                .summaryStatistics();
    }

    public int getMaximumDaysInQueue() {
        return statisticsQueue.getMax();
    }

    public double getAverageDaysInQueue() {
        return statisticsQueue.getAverage();
    }



    private void calculateDelayStatistic() {
        List<Long> unloadingDelay = getDelayDays();
        statisticsDelays = unloadingDelay
                .stream()
                .mapToLong(Long::longValue)
                .summaryStatistics();
    }

    private List<Long> getDelayDays() {
        List<Long> unloadingDelay = new ArrayList<>();
        for(var elem : scheduleDays) {
            if(elem.getUnloadingStartDay() != null )
                unloadingDelay.add(UtilsDate.daysBetween(elem.getRealArrival(), elem.getUnloadingStartDay()) );
        }
        return unloadingDelay;
    }

    public double getAverageDelay() {
        return statisticsDelays.getAverage();
    }

    public long getMaximumDelay() {
        return statisticsDelays.getMax();
    }






    @Override
    public String toString() {
        return "Статистика: \n"+
                "\nКоличество разгруженных судов:" + unloadedShips +
                "\nСреднее время ожидания в очереди:" + getAverageDaysInQueue() +
                "\nМаксимальное время ожидания в очереди:" + getMaximumDaysInQueue() +
                "\nСредняя задержка разгрузки:" + getAverageDelay() +
                "\nМаксимальная  задержка разгрузки:" + getMaximumDelay() +
                "\nШтраф по сыпучим грузам: " + totalBulkPenalty +
                "\nШтраф по контейнерным грузам: " + totalContainerPenalty +
                "\nШтраф по жидким грузам: " + totalLiquidPenalty +
                "\nИтоговый Штраф:" + totalPenalty;
    }

}
