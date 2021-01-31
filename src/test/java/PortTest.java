import com.rusanov.Port;
import com.rusanov.port.shedule.Schedule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PortTest {
    @Test
    public void modelingTest() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        Schedule schedule = new Schedule(5, date);
        System.out.println(schedule);

        Port port = new Port(schedule, 1,1,1);

       port.modeling();
        System.out.println("end");
    }

}
