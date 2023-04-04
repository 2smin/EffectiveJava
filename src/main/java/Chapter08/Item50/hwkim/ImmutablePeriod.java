package Chapter08.Item50.hwkim;

import java.util.Date;

public class ImmutablePeriod {
    private final Date start;
    private final Date end;


    public ImmutablePeriod(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());

        if(this.start.compareTo(this.end) > 0){
            throw new IllegalArgumentException(this.start + "가 " + this.end + "보다 늦다.");
        }
    }

//    public Date start(){
//        return start;
//    }
//
//    public Date end(){
//        return end;
//    }

    public Date start(){
        return new Date(start.getTime());
    }
    public Date end(){
        return new Date(end.getTime());
    }
}
