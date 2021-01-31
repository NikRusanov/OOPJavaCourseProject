package com.rusanov.port.shedule;

import com.rusanov.UtilsDate;

import java.util.*;
import java.util.stream.Collectors;

public class Schedule {




    private List<ShipSchedule> schedule = new ArrayList<>();
    private  Date startModelingDate;

    private  final ArrayList<CargoType>  cargoTypes= new ArrayList<>();
    public Schedule(int shipCount, Date startModelingDay) {
        cargoTypes.add(CargoType.BULK);
        cargoTypes.add(CargoType.CONTAINER);
        cargoTypes.add(CargoType.LIQUID);
        this.startModelingDate = startModelingDay;
        if(shipCount < 0) {
            shipCount = 2;
        }
        for(int i = 0; i < shipCount; ++i) {
            ShipSchedule curShipSchedule = getRandomSchedule();

            //TODO изменить имена
            curShipSchedule.getArrivedShip().setName("Ship №" + i);
            schedule.add(curShipSchedule);
        }
    }


    public Schedule(Schedule old) {
        List<ShipSchedule> newSchedule = new ArrayList<>();
        for (var oldShipSchedules : old.getSchedule()) {
            Ship oldShip = oldShipSchedules.getArrivedShip();
            Ship newShip = new Ship(oldShip.getShipName(), oldShip.getCargoWeight(), oldShip.getType());

            newSchedule.add(new ShipSchedule(newShip,
                    new Date( oldShipSchedules.getExceptedArrival().getTime()),
                    new Date( oldShipSchedules.getRealArrival().getTime()),
                    oldShipSchedules.getPlannedUnloadingDays(),
                    oldShipSchedules.getDelay()));
        }
            startModelingDate = new Date(old.getStartModelingDate().getTime());
            schedule = new ArrayList<>(newSchedule);

    }




    public long calculateTotalPenalties() {
        long totalPenalties = 0;
        for(var scheduleDay : schedule) {
            totalPenalties+=scheduleDay.getPenalty();
        }
        return totalPenalties;
    }

    public void calculateEveryPenalty(Date date) {
        for(var scheduleDay : schedule) {
            scheduleDay.calculatePenalty(date);
        }
    }



    private ShipSchedule getRandomSchedule() {


        Ship newShip = getNewShip();
        double loadSpeed = newShip.getType().getLoadSpeed();
        double weight = newShip.getCargoWeight();
        int plannedUnloading = (int) Math.round((weight/loadSpeed)) +  1;
        int unloadingDelay = calculateUnloadingDelay();
        Date exceptedArrival = generateRandomArrival(startModelingDate);
        Date actualArrivalDate = generateRandomArrival(exceptedArrival);
        return  new ShipSchedule( newShip, exceptedArrival, actualArrivalDate ,plannedUnloading, unloadingDelay );

    }

    private int calculateUnloadingDelay() {
        //TODO delete seed , added for debug
        Random random = new Random(253);
        return random.nextInt(10);
    }

    private Ship getNewShip() {

        int randomCargoType =(int) Math.round(getRandom(0, 2));
        CargoType currentType = cargoTypes.get(randomCargoType);
        double weight =  getRandomWeight(currentType);
        Ship newShip =  new  Ship("ShipName", weight, currentType);
        return newShip;
    }



    private  double getRandom(int min, int max) {
        // TODO delete seed
        Random random = new Random(253);
        double randomNormalValue =  random.nextGaussian() * 3.5;
        if( randomNormalValue < min ) randomNormalValue = min;
        if (randomNormalValue > max) randomNormalValue = max;
        return randomNormalValue;
    }


    private double getRandomWeight(CargoType currentType) {
        Random random = new Random();
        int min = currentType.getLoadSpeed() * 2 ;
        int max = currentType.getLoadSpeed() * 6 ;
        return  min + random.nextDouble()*( max -min );
    }


    private Date generateRandomArrival(Date exceptedArrival) {

        int min = -7;
        int max = 7;

        int randomDeviation=(int)Math.round(getRandom(min,max));
        return UtilsDate.addDays(exceptedArrival, randomDeviation);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var elem : schedule) {
            sb.append(elem).append("\n");
        }
        return sb.toString();
    }

    public Date getStartModelingDate() {
        return startModelingDate;
    }


    public List<ShipSchedule> getSchedulesByListSortedByRealArrival() {
        return schedule
                .stream()
                .sorted(Comparator.comparing(ShipSchedule::getRealArrival))
                .collect(Collectors.toList());
    }


    public List<ShipSchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ShipSchedule> schedule) {
        this.schedule = schedule;
    }






}

