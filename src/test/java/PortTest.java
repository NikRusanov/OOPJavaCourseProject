import com.rusanov.Port;
import com.rusanov.port.shedule.Schedule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PortTest {
    @Test
    public void modelingTest() {

        Date date = new Date(System.currentTimeMillis());
        Schedule schedule = new Schedule(5, date);
        System.out.println(schedule);

        Port port = new Port(schedule, 1,1,1);

       port.modeling();
        System.out.println("end");
    }

    @Test
    public void scheduleCopyTest() {
        Date date = new Date(System.currentTimeMillis());
        Schedule schedule = new Schedule(5, date);
        Schedule scheduleCopy = new Schedule(schedule);
        Port port = new Port(schedule, 1,1,1);

        port.modeling();
        System.out.println("end");
        System.out.println("old Schedule " + schedule);
        System.out.println("===============================" );
        System.out.println("new Schedule " + scheduleCopy);

    }

}
