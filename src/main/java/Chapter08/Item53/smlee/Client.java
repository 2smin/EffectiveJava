package Chapter08.Item53.smlee;

import java.security.InvalidParameterException;

public class Client {

    public static void main(String[] args) {

    }

    public void method1(int... a){
        if (a.length == 0) {
            throw new InvalidParameterException("need parameters");
        }
    }

    public void method2(int firstParam, int... remainedParam){
        //custom logic
    }

    public void method3(int firstParam){}
    public void method4(int firstParam, int secondParam){}
    public void method5(int firstParam, int secondParam, int thirdParam){}
}
