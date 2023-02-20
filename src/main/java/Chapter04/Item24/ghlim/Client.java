package Chapter04.Item24.ghlim;

public class Client {

    public static void main(String[] args) {

        Calculator c = new Calculator();


        // non static inner class는 바깥 클래스에 대한 참조 갖는다
        Calculator.innerClass i = c.createInnerClass();

        c.toString();
    }
}
