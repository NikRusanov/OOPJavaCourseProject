package com.rusanov.port;

import com.rusanov.UtilsDate;
import com.rusanov.cranes.Crane;
import com.rusanov.port.schedule.ShipSchedule;

import java.util.*;

public class Statistic {

    List<ShipSchedule> scheduleDays;
    private HashMap<CargoType, List<Crane>> cranes;

    private long unloadedShips;
    private IntSummaryStatistics statisticsQueue;
    private LongSummaryStatistics statisticsDelays;



    private long totalPenalty;


    private long totalBulkPenalty;
    private long totalContainerPenalty;
    private long totalLiquidPenalty;



    public Statistic(List<ShipSchedule> schedules, HashMap<CargoType, List<Crane>> cranes) {
        this.scheduleDays = schedules;
        this.cranes = cranes;
        init();
    }


    public void init() {
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
        totalContainerPenalty = calculatePenaltiesByType(CargoType.CONTAINER);
        totalLiquidPenalty = calculatePenaltiesByType(CargoType.LIQUID);
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



    public long getCranesCost(CargoType type) {
        return cranes.get(type)
                .stream()
                .mapToLong(Crane::getCost)
                .sum();
    }


    public long getCranesCost() {
        long sum = 0 ;
        for (var type : CargoType.values()) {
            sum+=getCranesCost(type) ;
        }
        return sum;
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
                "\nИтоговый Штраф:" + totalPenalty +
                "\n Стоимость кранов: " + getCranesCost() +
                "\nКоличество жидких кранов: " + getCranesCount(CargoType.LIQUID) +
                "\nКоличество сыпучих кранов: " + getCranesCount(CargoType.BULK) +
                "\nКоличество контейнерных кранов: " + getCranesCount(CargoType.CONTAINER)  ;
    }

    private int getCranesCount(CargoType type) {
        return  cranes.get(type).size() ;
    }
}
