package Chapter04.Item19.smlee;

public class Helper {

    public void overrideMe1(){
        overrideMe2();
    }
    public void overrideMe2(){
        System.out.println("실제 기능");
    }

    public void overrideMe3(){
        helperMethod();
    }

    private void helperMethod(){
        System.out.println("실제 기능");
    }

    public void directCall(){
        helperMethod();
    }
}
