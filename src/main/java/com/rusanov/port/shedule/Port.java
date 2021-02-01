package com.rusanov.port.shedule;

import com.rusanov.UtilsDate;
import com.rusanov.cranes.Crane;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Port {


    private Schedule plannedSchedule;

    private HashMap<CargoType, List<Crane>> cranes;

    private List<ShipSchedule> unloaded;

    private Date currentDate;


    private Statistic statistic;



    public Port(Schedule schedule, int bulkCranesCount, int liquidCranesCount, int containerCranesCount) {
        unloaded = new ArrayList<>();
        initCranes(bulkCranesCount, liquidCranesCount, containerCranesCount);
        this.plannedSchedule = schedule;
        currentDate = new Date(plannedSchedule.getStartModelingDate().getTime());
    }




    private void initCranes(int bulkCranesCount, int liquidCranesCount, int containerCranesCount) {
        cranes = new HashMap<>();
        cranes.put(CargoType.BULK, new ArrayList<>());
        addCranes(CargoType.BULK, bulkCranesCount);
        cranes.put(CargoType.LIQUID, new ArrayList<>());
        addCranes(CargoType.LIQUID, liquidCranesCount);
        cranes.put(CargoType.CONTAINER, new ArrayList<>());
        addCranes(CargoType.CONTAINER, containerCranesCount);

    }


    public void addCranes(CargoType type, int count) {
        if(count <= 0) {
            return;
        }
        for (int i = 0  ; i < count ; ++i) {
            cranes.get(type).add(new Crane(type, (long) i));
        }
    }

    public HashMap<CargoType, List<Crane>> getCranes() {
        return cranes;
    }

    public void modeling() {
        int countOfModelingDays = 30;
        System.out.println("START MODELING");
        for (int day = 0; day < countOfModelingDays ; ++day) {
            List<ShipSchedule> sortedSchedule = plannedSchedule.getSchedulesByListSortedByRealArrival();
            System.out.println("\n=========CURRENT DATE:" + currentDate + "=========");
            for(var currentSchedule : sortedSchedule) {
                if(currentSchedule.isArrival(currentDate) && !currentSchedule.isUnloading()) {

                    CargoType shipType  = currentSchedule.getArrivedShip().getType();
                    cranes
                            .get(shipType)
                            .stream()
                            .filter(crane -> !crane.isBusy() )
                            .findFirst().ifPresentOrElse(crane -> {
                                //            System.out.println("=================Crane TYPE:================\n" + crane.getType());
                                System.out.println("=========================================");
                                System.out.println("==========Added to unload!!!!!===========");
                                System.out.println("=========================================");
                                System.out.println("Unloading Ship: " + currentSchedule.getArrivedShip().getShipName());
                                System.out.println("Current Crane:\n" + crane);
                                crane.setShip(currentSchedule.getArrivedShip());
                                currentSchedule.setUnloadingStartDay(new Date(currentDate.getTime()));
                                currentSchedule.setUnloading(true);
                            },
                            () -> { currentSchedule.addTotalDaysInQueue();
                                System.out.println(currentSchedule.getArrivedShip().getShipName() +" added total days in queue");
                            });



                }

            }
            iterateUnload();
            currentDate = UtilsDate.addDays(currentDate,1);
//
//            System.out.println("===================================================================");
//            System.out.println("Schedule in iterate:\n");
//            System.out.println(plannedSchedule);
//            System.out.println("===================================================================");
//            System.out.println("===================================================================");
//            System.out.println("Cranes status:\n");
//            debugPrintCranes();
            System.out.println("===================================================================");


            if(plannedSchedule.getSchedule().isEmpty()) {
                break;
            }

        }

            plannedSchedule.calculateEveryPenalty(currentDate);



        for (var elem : unloaded) {
            elem.calculatePenalty(elem.getRealUnloadingEndDay());
        }

        calculateStatistic();

    }





    private void debugPrintCranes() {
        cranes
                .forEach((key, value) -> {
                    System.out.println("Type:" + key);
                    value.forEach(crane -> {
                        System.out.println(crane + "\n");

                    });
                });
    }


    private void iterateUnload() {
        cranes.forEach((type, currentCranes) -> currentCranes
                .stream()
                .filter(Crane::isBusy)
                .forEach((unloadingCrane) ->
                {
                    unloadingCrane.unload();
                    if(isUnloaded(unloadingCrane)) {
                        unloadingCrane.setBusy(false);
                        ShipSchedule scheduleDay = getScheduleForShip(unloadingCrane.getShip());

                        if(scheduleDay != null) {
                            scheduleDay.setRealUnloadingEndDay(currentDate);
                            scheduleDay.setUnloading(false);
                            System.out.println("\n" + scheduleDay.getArrivedShip().getShipName() + "unloaded");
                            unloaded.add(scheduleDay);
                            plannedSchedule.getSchedule().remove(scheduleDay);
                        }

                    }
                }));


    }




    private boolean isUnloaded(Crane crane) {
        Ship unloadedShip = crane.getShip();
        if(unloadedShip.getCargoWeight() > 0) {
            return false;
        }
        if(isShipInSchedule(unloadedShip)) {
            ShipSchedule scheduleForCurrentShip = getScheduleForShip(unloadedShip);
            assert scheduleForCurrentShip != null;
            return currentDate.after(scheduleForCurrentShip.getUnloadingDateWithWithDelay());
        }
        return false;
    }

    private ShipSchedule getScheduleForShip(Ship ship) {
        for (var scheduleDay : plannedSchedule.getSchedule()) {
            if (scheduleDay.getArrivedShip() == ship) {
                return scheduleDay;
            }
        }
        return null;
    }


    public boolean isShipInSchedule(Ship ship) {
        for (var scheduleDay : plannedSchedule.getSchedule()) {
            if (scheduleDay.getArrivedShip() == ship) {
                return true;
            }
        }
        return false;
    }



    private void calculateStatistic() {
        statistic = new Statistic(getAllSchedulesAfterUnload());
        System.out.println(statistic);
    }

    public Statistic getStatistic() {
        return statistic;
    }

    private  List<ShipSchedule> getAllSchedulesAfterUnload() {
        List<ShipSchedule> allSchedules   = new ArrayList<>();
        List<ShipSchedule>  notUnloadedShips =  plannedSchedule.getSchedule();
        if(!(notUnloadedShips == null || notUnloadedShips.isEmpty()) ) {
            allSchedules.addAll(notUnloadedShips);
        }

        if(!(unloaded == null || unloaded.isEmpty())) {
            allSchedules.addAll(unloaded);
        }

        return allSchedules;

    }


    public long getCranesCost(CargoType type) {
        return cranes.get(type)
                .stream()
                .mapToLong(Crane::getCost)
                .sum();
    }


    public long getTotalPenaltyByType(CargoType type) {
        if(statistic == null) {
            return 0;
        }
        return this.statistic.calculatePenaltiesByType(type);
    }
}
