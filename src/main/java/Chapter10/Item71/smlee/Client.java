package Chapter10.Item71.smlee;

public class Client {

    public static void main(String[] args) {

        int code = 0;
        if(isPermitted(code)){
            action();
        }else{
            //exception handling
        }
    }

    public static boolean isPermitted(int code){

        if(code > 10){
            return true;
        }else{
            return false;
        }
    }

    public static void action(){
        //do something
    }
}
