package Chapter08.Item50.hwkim;

import java.util.Date;

public class Client {
    public static void main(String[] args){
        Date start = new Date();
        Date end = new Date();
        start.setYear(100);
        //end.setYear(78);
        end.setYear(101);
        Period p = new Period(start, end);
        end.setYear(78);
        System.out.println(p.end());

        end.setYear(101);
        ImmutablePeriod ip = new ImmutablePeriod(start, end);
        end.setYear(78);
        System.out.println(ip.end());
        ip.end().setYear(78);
        System.out.println(ip.end());
    }
}
