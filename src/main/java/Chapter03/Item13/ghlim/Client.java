package Chapter03.Item13.ghlim;

import Chapter03.Item10.ghlim.MyProtein;
import Chapter03.Item10.ghlim.ProteinPowder;

import java.awt.*;

public class Client {

    public static void main(String[] args) throws CloneNotSupportedException {
        ProteinPowder pr = new ProteinPowder(33, "chocolate");

        // proteinPowder의 implements 제거시 error 발생
        ProteinPowder pr2 = pr.clone();

        System.out.println(pr.equals(pr2));

        System.out.println(pr.hashCode());
        System.out.println(pr2.hashCode());

        System.out.println(pr.toString());
        System.out.println(pr2.toString());

        MyProtein myProteinNormal = new MyProtein(pr,"chocolate chip", "normal", Color.PINK);

        MyProtein myProteinNormal2 = MyProtein.newInstance(myProteinNormal);

        System.out.println(myProteinNormal.equals(myProteinNormal2));

        MyProtein myProteinWhey = new MyProtein(pr,"chocolate icecream", "whey", Color.PINK);

        MyProtein myProteinWhey2 = new MyProtein(myProteinWhey);

        System.out.println(myProteinWhey2.equals(myProteinWhey));

    }
}
