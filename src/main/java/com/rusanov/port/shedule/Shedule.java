    package com.rusanov.port.shedule;

import java.util.*;

    public class Shedule {

    private List<ShipSchedule> shedule = new ArrayList<>();
    private final Date startModelingDay;

    private  final ArrayList<CargoType>  cargoTypes= new ArrayList<>();
    public Shedule(int shipCount, Date startModelingDay) {
        cargoTypes.add(CargoType.BULK);
        cargoTypes.add(CargoType.CONTAINER);
        cargoTypes.add(CargoType.LIQUID);
        this.startModelingDay = startModelingDay;
        if(shipCount < 0) {
            shipCount = 2;
        }
        for(int i = 0; i < shipCount; ++i) {
            ShipSchedule curShipShedule = getRandomSchedule();
            shedule.add(curShipShedule);
        }
    }


    private ShipSchedule getRandomSchedule() {

        int plannedUnloading =  2;
//TODO gen plannedUnloading, gen unloadingEndDay
        Ship newShip = getNewShip();
        Date exceptedArrival = generateRandomArrival(startModelingDay);
        Date actualArrivalDate = generateRandomArrival(exceptedArrival);
        return  new ShipSchedule( newShip, exceptedArrival, actualArrivalDate ,plannedUnloading );

    }

    private Ship getNewShip() {

        System.out.println(cargoTypes.size());
        int random =(int) getRandom(0, 2);
        CargoType currentType = cargoTypes.get((int) getRandom(0, 2));
        double weight =  getRandomWeight(currentType);
        Ship newShip =  new  Ship("ShipName", weight, currentType);
        System.out.println("new Ship : " + newShip);
        return newShip;
    }



        private  double getRandom(int min, int max) {
            Random random = new Random();
            double randomNormalValue =  random.nextGaussian() * 3.5;
            if( randomNormalValue < min ) randomNormalValue = min;
            if (randomNormalValue > max) randomNormalValue = max;
            return randomNormalValue;
        }


    private double getRandomWeight(CargoType currentType) {
        int min = currentType.getLoadSpeed() * 2 ;
        int max = currentType.getLoadSpeed() * 5 ;
        return  getRandom(min, max);
    }


    private Date generateRandomArrival(Date exceptedArrival) {
        GregorianCalendar calendar= new GregorianCalendar();
        calendar.setTime(exceptedArrival);
        int min = -7;
        int max = 7;
        int randomDeviation=(int)Math.round(getRandom(min,max));
        calendar.add(Calendar.DATE, randomDeviation);
        return calendar.getTime();
    }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (var elem : shedule) {
                sb.append(elem).append("\n");
            }
            return sb.toString();
        }
    }
