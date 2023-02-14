package Chapter03.Item11.ghlim;

import Chapter03.Item10.ghlim.MyProtein;
import Chapter03.Item10.ghlim.ProteinPowder;

import java.awt.*;

public class Client {

    public static void main(String[] args) {
        ProteinPowder pr = new ProteinPowder(33, "chocolate");

        MyProtein myProteinWhey = new MyProtein(pr,"chocolate icecream", "whey", Color.PINK);

        MyProtein myProteinSoy = new MyProtein(pr,"chocolate brownie", "soy", Color.PINK);

        MyProtein myProteinSoy2 = new MyProtein(pr,"chocolate brownie", "soy", Color.PINK);

        MyProtein myProteinNormal = new MyProtein(pr,"chocolate chip", "normal", Color.PINK);


        System.out.println(myProteinSoy.hashCode());

        System.out.println(myProteinSoy2.hashCode());

        System.out.println(pr.hashCode());

        System.out.println(new ProteinPowder(33, "chocolate").hashCode());
    }
}
