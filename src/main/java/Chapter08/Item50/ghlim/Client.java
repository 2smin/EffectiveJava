package Chapter08.Item50.ghlim;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Client {

    public static void main(String[] args){
//        Instant start = Instant.now();
        Date start = new Date();
        Date end = new Date();
        Period p = new Period(start, end);
        end.setYear(78); // Modifies internals of p
    }
}
