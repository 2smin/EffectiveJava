package Chapter03.Item10.ghlim;

import java.awt.*;
import java.util.Set;

public class Client {
    public static void main(String[] args) {

        ProteinPowder pr = new ProteinPowder(33, "chocolate");

        MyProtein myProteinWhey = new MyProtein(pr,"chocolate icecream", "whey", Color.PINK);

        MyProtein myProteinSoy = new MyProtein(pr,"chocolate brownie", "soy", Color.PINK);

        MyProtein myProteinNormal = new MyProtein(pr,"chocolate chip", "normal", Color.PINK);

        System.out.println(myProteinSoy == myProteinNormal);

        // ProteinPowder 의 equals 주석 처리하고 실행
        System.out.println(pr.equals(new ProteinPowder(33, "chocolate")));

        System.out.println(pr.equals(myProteinNormal));
    }
}
