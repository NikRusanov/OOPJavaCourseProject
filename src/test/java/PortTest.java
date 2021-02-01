import com.rusanov.port.shedule.CargoType;
import com.rusanov.port.shedule.Port;
import com.rusanov.port.shedule.Schedule;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PortTest {
    @Test
    public void modelingTest() {

        Date date = new Date(System.currentTimeMillis());
        Schedule schedule = new Schedule(10, date);


        Map<CargoType, Integer> cranesToAdd = new HashMap<>();
        cranesToAdd.put(CargoType.BULK, 0 );
        cranesToAdd.put(CargoType.LIQUID, 0);
        cranesToAdd.put(CargoType.CONTAINER, 0);



        Port port = new Port(schedule, 1, 10, 1);
        port.modeling();
        boolean isModeling = true;
//Изменения в кранах
   //     while (isModeling) {
//            Schedule scheduleCopy = new Schedule(schedule);
//            for (var type : CargoType.values()) {
//                var penalty = port.getTotalPenaltyByType(type);
//                long countToAddCrane = 0;
//                if(penalty != 0)
//                    countToAddCrane = port.getCranesCost(type) / port.getTotalPenaltyByType(type);
//                System.out.println("count to add "  + countToAddCrane);
//                if (port.getCranesCost(type) <= countToAddCrane) {
//                    int count = cranesToAdd.getOrDefault(type, 0);
//                    cranesToAdd.put(type, count + 1);
//
//                }
//            }
//            if(!cranesToAdd.isEmpty()) {
//                Port newPort = new Port(scheduleCopy,
//                        cranesToAdd.get(CargoType.BULK),
//                        cranesToAdd.get(CargoType.LIQUID),
//                        cranesToAdd.get(CargoType.CONTAINER));
//                        newPort.modeling();
//            } else {
//                isModeling = false;
//            }

   //     }



        System.out.println("end");

    }

    @Test
    public void scheduleCopyTest() {
        Date date = new Date(System.currentTimeMillis());
        Schedule schedule = new Schedule(5, date);
        Schedule scheduleCopy = new Schedule(schedule);
        Port port = new Port(schedule, 1, 1, 1);

        port.modeling();
        System.out.println("end");
        System.out.println("old Schedule " + schedule);
        System.out.println("===============================");
        System.out.println("new Schedule " + scheduleCopy);
    }
}
