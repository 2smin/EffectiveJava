package Chapter03.Item12.ghlim;

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

        System.out.println(pr.toString());

        System.out.println(myProteinSoy.toString());

        System.out.println(myProteinSoy2.toString());

    }
}
