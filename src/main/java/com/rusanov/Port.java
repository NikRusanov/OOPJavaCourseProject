package com.rusanov;

import com.rusanov.cranes.Crane;
import com.rusanov.port.shedule.CargoType;
import com.rusanov.port.shedule.Schedule;
import com.rusanov.port.shedule.Ship;
import com.rusanov.port.shedule.ShipSchedule;

import java.util.*;

public class Port {


    Schedule plannedSchedule;

    private HashMap<CargoType, List<Crane>> cranes;

    private List<ShipSchedule> unloaded;

    private Date currentDate;

    public Port(Schedule schedule, int bulkCranesCount, int liquidCranesCount, int containerCranesCount) {
        unloaded = new ArrayList<>();
        initCranes(bulkCranesCount, liquidCranesCount, bulkCranesCount);
        this.plannedSchedule = schedule;
        currentDate = new Date(plannedSchedule.getStartModelingDate().getTime());
    }




    private void initCranes(int bulkCranesCount, int liquidCranesCount, int cranesCount) {
        cranes = new HashMap<>();
        cranes.put(CargoType.BULK, new ArrayList<>());
        addCranes(CargoType.BULK, bulkCranesCount);
        cranes.put(CargoType.LIQUID, new ArrayList<>());
        addCranes(CargoType.LIQUID, bulkCranesCount);
        cranes.put(CargoType.CONTAINER, new ArrayList<>());
        addCranes(CargoType.CONTAINER, bulkCranesCount);

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

        long totalPenalty = 0 ;
        long totalBulkPenalty = 0 ;
        long totalContainerPenalty = 0 ;
        long totalLiquidPenalty = 0 ;
        if (! plannedSchedule.getSchedule().isEmpty()) {
            plannedSchedule.calculateEveryPenalty(currentDate);
        }


        for (var elem : unloaded) {
            elem.calculatePenalty(elem.getRealUnloadingEndDay());
        }

        totalBulkPenalty = getPenaltiesByType(CargoType.BULK, plannedSchedule.getSchedule()) +
                getPenaltiesByType(CargoType.BULK, unloaded);
        totalContainerPenalty = getPenaltiesByType(CargoType.CONTAINER, plannedSchedule.getSchedule()) +
                getPenaltiesByType(CargoType.CONTAINER, unloaded);
        totalLiquidPenalty = getPenaltiesByType(CargoType.LIQUID, plannedSchedule.getSchedule()) +
                getPenaltiesByType(CargoType.LIQUID, unloaded);




        System.out.println("===================================================================");
        System.out.println("NOT UNLOADED SCHEDULE:");
        System.out.println(plannedSchedule);
        System.out.println("===================================================================");
        System.out.println("UNLOADED SCHEDULE:");
        System.out.println(unloaded);


        System.out.println("\nTotal Bulk penalty: " + totalBulkPenalty);
        System.out.println("Total Container penalty: " + totalContainerPenalty);
        System.out.println("Total Liquid penalty: " + totalLiquidPenalty);
        totalPenalty = totalBulkPenalty + totalContainerPenalty + totalLiquidPenalty;
        System.out.println("Total penalty " + totalPenalty) ;



    }

    private long getPenaltiesByType(CargoType type, List<ShipSchedule> schedules) {
        return schedules
                .stream()
                .filter(shipSchedule -> (shipSchedule.getArrivedShip().getType() == type))
                .mapToLong(shipSchedule -> (long) shipSchedule.getPenalty()).sum();
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
}
