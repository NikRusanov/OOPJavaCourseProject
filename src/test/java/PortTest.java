import com.rusanov.port.CargoType;
import com.rusanov.port.Port;
import com.rusanov.port.schedule.Schedule;
import com.rusanov.port.schedule.ScheduleFactory;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PortTest {
    @Test
    public void modelingTest() {

        Date date = new Date(System.currentTimeMillis());

        ScheduleFactory scheduleFactory = new ScheduleFactory(10, date);

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

    @Test
    public void scheduleCopyTest() {
        Date date = new Date(System.currentTimeMillis());
        Schedule schedule = new Schedule(5, date);
        Schedule scheduleCopy = new Schedule(schedule);
        Port port = new Port(schedule, 1, 1, 1);
        port.modeling();



    }
}
