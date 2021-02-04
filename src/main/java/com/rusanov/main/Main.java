package com.rusanov.main;

import com.rusanov.port.CargoType;
import com.rusanov.port.Port;
import com.rusanov.port.schedule.ScheduleFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Date date = new Date(System.currentTimeMillis());

        ScheduleFactory scheduleFactory = new ScheduleFactory(5, date);

        Map<CargoType, Integer> cranesCount = new HashMap<>();
        cranesCount.put(CargoType.BULK, 1 );
        cranesCount.put(CargoType.LIQUID, 1);
        cranesCount.put(CargoType.CONTAINER, 1);

        boolean isModeling = true;
        while (isModeling) {

            int liquidCount = cranesCount.get(CargoType.LIQUID);
            int bulkCount = cranesCount.get(CargoType.BULK);
            int containerCount = cranesCount.get(CargoType.CONTAINER);

            Port port = new Port(scheduleFactory.getScheduleCopy(),
                    bulkCount,
                    liquidCount,
                    containerCount);
//            System.out.println("Port:");
//            System.out.println(port);
            port.modeling();


            for (var type : CargoType.values()) {
                var penalty = port.getTotalPenaltyByType(type);
                long cranesCost = port.getCranesCost(type);

                if(penalty > cranesCost) {
                    int count = cranesCount.get(type);
                    cranesCount.put(type,  count + 1);

                }
            }

            int sumCranes = 0;

            for (var type : CargoType.values()) {
                sumCranes += cranesCount.get(type);
            }

            if (sumCranes == (bulkCount +
                    liquidCount +
                    containerCount) ) {
                isModeling = false;
            }

        }

        System.out.println("end");
    }



}
